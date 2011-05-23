package tc.fab.pdf.signer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.antlr.stringtemplate.StringTemplate;
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
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.TSAClient;
import com.itextpdf.text.pdf.TSAClientBouncyCastle;

public class PDFSmartCardSigner implements IPDFSigner {

	private SignerOptions options;
	private KeyStoreAdapter keyStore;
	// private PDDocument pddoc;
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

	public static void main(String args[]) throws Exception {
	}

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

		// TODO: remover conversão daqui
		// if (!FilenameUtils.getExtension(inputFile.getName()).toLowerCase()
		// .equals("pdf")) {
		// URL serverURL = getOptions().getURL();
		// if (PDFSignerApp.getApplication().getServerMonitor().isOnline(
		// serverURL)) {
		// Converter converter = Converter.getInstance();
		// inputFile = converter.convert(inputFile, getOptions().getURL());
		// setOutputFile(new File(getOutputFilename(inputFile)));
		// if (getOutputFile().exists()) {
		// FileUtils.deleteQuietly(getOutputFile());
		// }
		// FileUtils.moveFile(inputFile, getOutputFile());
		// inputFile = getOutputFile();
		// }
		// } else {
		// }

		URL fileURL = inputFile.toURI().toURL();

		// setPddoc(PDDocument.load(fileURL));

		if (!getOptions().isAbsolute() || getOptions().getOutputMaskRegex()) {

			tpos = new PDFTextPosition(inputFile);

		}

		setOutputFile(new File(getOutputFilename(inputFile)));

		reader = new PdfReader(fileURL);

		setOutputStream(new FileOutputStream(getOutputFile()));

		FileOutputStream fout = getOutputStream();
		PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0', null, true);

		ap = stp.getSignatureAppearance();

		// sap.setVisibleSignature(new Rectangle(72, 732, 144, 780), 1,
		// "Signature");
		ap.setCrypto(null, chain, null, PdfSignatureAppearance.SELF_SIGNED);

		configAppearance();

		PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, new PdfName(
				"adbe.pkcs7.detached"));
		dic.setReason(ap.getReason());
		dic.setLocation(ap.getLocation());
		dic.setContact(ap.getContact());
		dic.setDate(new PdfDate(ap.getSignDate()));
		ap.setCryptoDictionary(dic);

		// preserve some space for the contents
		int contentEstimated = 15000;
		HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
		exc.put(PdfName.CONTENTS, new Integer(contentEstimated * 2 + 2));
		ap.preClose(exc);

		// make the digest
		InputStream data = ap.getRangeStream();
		MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
		byte buf[] = new byte[8192];
		int n;
		while ((n = data.read(buf)) > 0) {
			messageDigest.update(buf, 0, n);
		}
		byte hash[] = messageDigest.digest();
		Calendar cal = Calendar.getInstance();
		// If we add a time stamp:
		TSAClient tsc = null;
		boolean withTS = false;
		if (withTS) {
			String tsa_url = "http://tsa.swisssign.net";
			String tsa_login = "";
			String tsa_passw = "";
			tsc = new TSAClientBouncyCastle(tsa_url, tsa_login, tsa_passw);
		}

		// Create the signature
		PdfPKCS7 sgn = new PdfPKCS7(pkey, chain, null, "SHA1",
				keyStore.getProvider(), false);

		byte sh[] = sgn.getAuthenticatedAttributeBytes(hash, cal, null);
		sgn.update(sh, 0, sh.length);
		byte[] encodedSig = sgn.getEncodedPKCS7(hash, cal, tsc, null);

		if (contentEstimated + 2 < encodedSig.length)
			throw new DocumentException("Not enough space");

		byte[] paddedSig = new byte[contentEstimated];
		System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
		// Replace the contents
		PdfDictionary dic2 = new PdfDictionary();
		dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
		ap.close(dic2);

		// PdfStamper stamper = PdfStamper.createSignature(reader,
		// getOutputStream(), '\0', null, true);
		//
		// ap = stamper.getSignatureAppearance();
		// ap.setCrypto(null,
		// keyStore.getKeystore()
		// .getCertificateChain(keyStore.getAlias(0)), null,
		// PdfSignatureAppearance.SELF_SIGNED);
		//
		// configAppearance();
		//
		// ap.setExternalDigest(new byte[128], new byte[20], "RSA");
		// ap.preClose();
		//
		// PrivateKey pkey = keyStore.getPrivateKey(keyStore.getAlias(0));
		// Provider pr = keyStore.getKeystore().getProvider();
		// Signature sign = Signature.getInstance("SHA1withRSA", pr);
		// sign.initSign(pkey);
		//
		// byte[] content = IOUtils.toByteArray(ap.getRangeStream());
		// sign.update(content);
		//
		// PdfPKCS7 signer = ap.getSigStandard().getSigner();
		// signer.setExternalDigest(sign.sign(), null, "RSA");
		//
		// PdfDictionary dic = new PdfDictionary();
		// dic.put(PdfName.CONTENTS,
		// new PdfString(signer.getEncodedPKCS1()).setHexWriting(true));
		// ap.close(dic);
		//
		// getOutputStream().close();

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

		ap.setAcro6Layers(true);

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

			ap.setImage(img);
			ap.setLayer2Text("");

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
				name = PdfPKCS7.getSubjectFields(keyStore.getCertificate(0))
						.getField("CN");
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

			String name = PdfPKCS7.getSubjectFields(keyStore.getCertificate(0))
					.getField("CN");
			desc.setName(name);

			StringTemplate description = new StringTemplate(desc.getTemplate());

			description.setAttribute("description", desc);

			if (desc.getLocation() != null) {
				ap.setLocation(desc.getLocation());
			}

			if (desc.getReason() != null) {
				ap.setReason(desc.getReason());
			}

			ap.setLayer2Text(description.toString());

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

	// private void setPddoc(PDDocument pddoc) {
	// this.pddoc = pddoc;
	// }
	//
	// public PDDocument getPddoc() {
	// return pddoc;
	// }

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
