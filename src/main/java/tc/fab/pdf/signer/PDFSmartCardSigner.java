package tc.fab.pdf.signer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.apache.commons.io.FilenameUtils;

import tc.fab.pdf.PDFTextPosition;
import tc.fab.pdf.signer.options.PlainDescription;
import tc.fab.pdf.signer.options.SignerOptions;
import tc.fab.pdf.signer.options.SignerOptions.RenderMode;
import tc.fab.security.KeyStoreAdapter;
import tc.fab.security.SmartCard;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.TSAClient;
import com.itextpdf.text.pdf.security.TSAClientBouncyCastle;

public class PDFSmartCardSigner implements IPDFSigner {

	private SignerOptions options;
	private KeyStoreAdapter keyStore;
	private PdfReader reader;
	private Float signatureWidth;
	private Float signatureHeight;
	private PdfSignatureAppearance ap;
	private Rectangle pCoords = null;
	private File outputFile;
	private FileOutputStream outputStream;
	private PDFTextPosition tpos;
	private PrivateKey pkey;
	private Certificate[] chain;

	public PDFSmartCardSigner(CallbackHandler handler)
			throws KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException, UnrecoverableKeyException {

		keyStore = new SmartCard();
		keyStore.login(handler);

		pkey = keyStore.getPrivateKey(keyStore.getAlias(0));
		chain = keyStore.getKeystore()
				.getCertificateChain(keyStore.getAlias(0));

	}

	@Override
	public void sign(File inputFile) throws IOException, DocumentException,
			GeneralSecurityException {

		if (!getOptions().isAbsolute() || getOptions().getOutputMaskRegex()) {

			tpos = new PDFTextPosition(inputFile);

		}

		setOutputFile(new File(getOutputFilename(inputFile)));

		RandomAccessFileOrArray raf = new RandomAccessFileOrArray(
				inputFile.getAbsolutePath(), false, true);

		reader = new PdfReader(raf, null);

		setOutputStream(new FileOutputStream(getOutputFile()));

		FileOutputStream fout = getOutputStream();
		File tmpFile = File.createTempFile("pdf", "sgn");
		PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0',
				tmpFile, true);

		ap = stp.getSignatureAppearance();

		configAppearance();

		// If we add a time stamp:
		TSAClient tsc = null;
		boolean withTS = false;
		if (withTS) {
			// TODO: proxy management
			System.getProperties().put("http.proxyHost", "localhost");
			System.getProperties().put("http.proxyPort", "8888");
			String tsa_url = "http://tsa.swisssign.net";
			String tsa_login = "";
			String tsa_passw = "";
			tsc = new TSAClientBouncyCastle(tsa_url, tsa_login, tsa_passw);
		}

		ExternalSignature es = new PrivateKeySignature(pkey, "SHA-1", null);
		MakeSignature.signDetached(ap, es, chain, null, null, tsc, null, 15000,
				MakeSignature.CMS);

	}

	// TODO: tirar este método daqui
	public static Float cmToPoint(Number cm) {

		Float returnf = (cm.floatValue() / 2.54F) * 72;

		return returnf;

	}

	public void setOptions(SignerOptions options) {
		this.options = options;
	}

	public SignerOptions getOptions() {
		return options;
	}

	protected void configAppearance() throws IOException, DocumentException,
			KeyStoreException {

		RenderMode renderMode = getOptions().getRenderMode();

		Image img = null;

		signatureHeight = getOptions().getSignatureHeight();
		signatureWidth = getOptions().getSignatureWidth();

		// ap.setAcro6Layers(true);

		if (getOptions().hasGraphic()) {
			img = Image.getInstance(getOptions().getImage().getAbsolutePath());
		}

		if (renderMode.equals(RenderMode.Graphic)) {

			ap.setRenderingMode(RenderingMode.GRAPHIC);

			Float scale = getOptions().getImageScale();

			if (getOptions().isSignatureAutomaticSize()) {
				signatureWidth = img.getWidth() * scale;
				signatureHeight = img.getHeight() * scale;
			} else {
				// TODO: nesse caso, expande a imagem ao tamanho total da
				// assinatura
				scale = -1F;
			}

			ap.setSignatureGraphic(img);
			// ap.setImage(img);
			// ap.setLayer2Text("");

			offsetSignature();

			// TODO: descobrir pq o Acrobat 7 não mostra a imagem qdo na layer2
			// addImageToLayer(ap.getLayer(2), img);

		} else if (renderMode.equals(RenderMode.GraphicAndDescription)) {

			ap.setRenderingMode(RenderingMode.GRAPHIC_AND_DESCRIPTION);

			ap.setSignatureGraphic(img);

			offsetSignature();

		} else if (renderMode.equals(RenderMode.Name)) {

			ap.setRenderingMode(RenderingMode.DESCRIPTION);

			String name;

			if (getOptions().getImageCustom()) {
				name = getOptions().getImageCustomText();
			} else {
				name = CertificateInfo.getSubjectFields(
						keyStore.getCertificate(0)).getField("CN");
			}

			addTextToLayer(2, name);

		} else if (renderMode.equals(RenderMode.Description)) {

			ap.setRenderingMode(RenderingMode.DESCRIPTION);
			offsetSignature();

		} else if (renderMode.equals(RenderMode.NameAndDescription)) {

			ap.setRenderingMode(RenderingMode.NAME_AND_DESCRIPTION);
			offsetSignature();

		}

		if (getOptions().hasDescription()) {

			// TODO: implementar description

			PlainDescription desc = getOptions().getDescription();

			String name = CertificateInfo.getSubjectFields(
					keyStore.getCertificate(0)).getField("CN");
			
			desc.setName(name);

			// StringTemplate description = new
			// StringTemplate(desc.getTemplate());

			// description.setAttribute("description", desc);

			if (desc.getLocation() != null) {
				ap.setLocation(desc.getLocation());
			}

			if (desc.getReason() != null) {
				ap.setReason(desc.getReason());
			}

			// ap.setLayer2Text(description.toString());

		}

	}

	protected void offsetSignature() throws IOException {

		Float pLeft;
		Float pRight;
		Float pTop;
		Float pBottom;

		Integer pageToSign = getOptions().getPageToSign(reader);
		Rectangle pageSize = reader.getPageSize(pageToSign);

		Float pageHeight = pageSize.getHeight();
		Float pageWidth = pageSize.getWidth();

		if (getOptions().isAbsolute()) {

			pBottom = cmToPoint(getOptions().getFooterDistance()
					+ getOptions().getMarginBottom());

			pTop = pBottom + signatureHeight;

		} else {

			Float marginBottom = pageHeight
					- cmToPoint(getOptions().getMarginBottom());
			Float lastLineDistance = cmToPoint(getOptions()
					.getLastLineDistance());

			Float yOffset;

			if (getOptions().getReferenceLastLine()) {

				yOffset = tpos.getLastLinePosition(pageToSign, marginBottom);
			} else {

				yOffset = tpos.getCharacterPosition(pageToSign, marginBottom,
						getOptions().getReferenceText(), true);

			}

			Float sigHeight = signatureHeight;
			if (lastLineDistance < 0) {
				sigHeight = 0F;
			}

			// pTop = (pageHeight - yOffset) + lastLineDistance + sigHeight;

			pTop = yOffset + lastLineDistance + sigHeight;

			pBottom = pTop - signatureHeight;

		}

		if (getOptions().getSignatureAlign().equals("center")) {
			pLeft = pageWidth / 2 - signatureWidth / 2;
		} else if (getOptions().getSignatureAlign().equals("left")) {
			pLeft = cmToPoint(1.0F);
		} else {
			pLeft = pageWidth - cmToPoint(0.5F) - signatureWidth;
		}
		pRight = pLeft + signatureWidth;

		pCoords = new Rectangle(pLeft, pBottom, pRight, pTop);

		ap.setVisibleSignature(pCoords, pageToSign, null);

	}

	public void addImageToLayer(PdfTemplate n2, Image im)
			throws DocumentException, MalformedURLException, IOException {

		n2.beginText();

		Float pSize = im.getWidth() / im.getHeight();

		Float w = im.getWidth();
		Float h = im.getHeight();

		if (im.getWidth() > signatureWidth) {
			w = signatureWidth;
			h = w / pSize;
		} else if (im.getHeight() > signatureHeight) {
			h = signatureHeight;
			w = h * pSize;
		}

		Float pbLeft = signatureWidth / 2 - w / 2;
		Float pbTop = signatureHeight / 2 - h / 2;

		n2.addImage(im, w, 0, 0, h, pbLeft, pbTop);

		n2.endText();

		offsetSignature();

	}

	public void addTextToLayer(Integer layer, String text) throws IOException,
			DocumentException {

		Float fontSize = 10F;

		BaseFont bf = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
		Float tWidth = bf.getWidthPoint(text, fontSize);

		// TODO: Estudar altura de caracteres para melhor determinação da margem
		// 1.2F = 20% de margem de segurança para caracteres fora de linha
		signatureHeight = fontSize * 1.2F;
		signatureWidth = tWidth + 2F;

		offsetSignature();

		PdfTemplate n2 = ap.getLayer(layer);

		n2.beginText();
		n2.setFontAndSize(bf, fontSize);
		n2.showTextAligned(PdfTemplate.ALIGN_CENTER, text, signatureWidth / 2,
				signatureHeight - fontSize, 0);
		n2.endText();

	}

	public String getOutputFilename(File inputFile) throws IOException {

		String inputFilename = inputFile.getName().substring(0,
				inputFile.getName().lastIndexOf("."));

		if (options.getOutputMaskRegex()) {

			String r = options.getMaskRegex();

			if (!(r == null || r.length() < 1)) {

				String text = tpos.getText(1);

				Matcher m = Pattern.compile(r).matcher(text);

				if (m.find()) {

					inputFilename = m.group();

				}

			}

		}

		String inputParent = inputFile.getParent();

		String outputFilename = inputParent + File.separator
				+ getOptions().getOutputPreffix() + inputFilename
				+ getOptions().getOutputSuffix() + ".pdf";

		if (!options.getOutputOverwriteOriginal()) {

			String baseName = FilenameUtils.getBaseName(outputFilename);

			String extension = FilenameUtils.getExtension(outputFilename);

			File outputFile = new File(outputFilename);

			int i = 1;

			while (outputFile.exists()) {

				outputFile = new File(outputFile.getParentFile(), baseName
						+ "(" + i++ + ")." + extension);

			}

			outputFilename = outputFile.getAbsolutePath();

		}

		return outputFilename;

	}

	public KeyStoreAdapter getCard() {
		return keyStore;
	}

	private void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	public File getOutputFile() {
		return outputFile;
	}

	private void setOutputStream(FileOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public FileOutputStream getOutputStream() {
		return outputStream;
	}

	public void logout() throws LoginException {

		keyStore.logout();

	}

}
