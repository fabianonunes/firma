package tc.fab.pdf.signer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import tc.fab.pdf.signer.message.Envelope;
import tc.fab.pdf.signer.options.PDFTextPosition;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.security.CertificateInfo;

public class SignatureAppearance {

	private PdfReader reader;
	private Float signatureWidth;
	private Float signatureHeight;
	private PdfSignatureAppearance appearance;
	private PDFTextPosition textPosition;
	private DocumentSigner signer;

	public SignatureAppearance(DocumentSigner signer) {
		this.signer = signer;
	}

	public void sign(File inputFile, File outputFile) throws Exception {

		if (options != null && (!getOptions().isAbsolute() || getOptions().getOutputMaskRegex())) {
			textPosition = new PDFTextPosition(inputFile);
		}

		RandomAccessSource factory = new RandomAccessSourceFactory().createBestSource(inputFile
			.getAbsolutePath());

		RandomAccessFileOrArray raf = new RandomAccessFileOrArray(factory);

		reader = new PdfReader(raf, null);

		File tmpFile = File.createTempFile("pdf", "sgn");

		// TODO: implement memory outputstream
		FileOutputStream fout = new FileOutputStream(outputFile != null ? outputFile
			: getOutputFile(inputFile));

		PdfStamper stamper = PdfStamper.createSignature(reader, fout, '\0', tmpFile, true);
		appearance = stamper.getSignatureAppearance();

		signer.sign(appearance, false);

	}

	public Envelope signBlank(File inputFile, File outputFile, Certificate[] chain)
		throws Exception {

		reader = new PdfReader(inputFile.getPath());

		File tmpFile = File.createTempFile("pdf", "sgn");

		// TODO: implement memory outputstream
		FileOutputStream fout = new FileOutputStream(outputFile != null ? outputFile
			: getOutputFile(inputFile));

		PdfStamper stamper = PdfStamper.createSignature(reader, fout, '\0', tmpFile, true);

		appearance = stamper.getSignatureAppearance();
		appearance.setCertificate(chain[0]);
		appearance.setReason("I've written this.");
		appearance.setLocation("Foobar");
		appearance.setVisibleSignature(new Rectangle(220, 732, 400, 780), 1, "Signature1");

		appearance.setLayer2Text(CertificateInfo.getSubjectFields((X509Certificate) chain[0])
			.getField("CN"));

		appearance.setRenderingMode(RenderingMode.NAME_AND_DESCRIPTION);

		return signer.getSignableStream(appearance, chain);

	}

	// TODO: tirar este método daqui
	public static Float cmToPoint(Number cm) {
		return (cm.floatValue() / 2.54F) * 72;
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

			pBottom = cmToPoint(getOptions().getFooterDistance() + getOptions().getMarginBottom());

			pTop = pBottom + signatureHeight;

		} else {

			Float marginBottom = pageHeight - cmToPoint(getOptions().getMarginBottom());

			Float lastLineDistance = cmToPoint(getOptions().getLastLineDistance());

			Float yOffset;

			if (getOptions().getReferenceLastLine()) {
				yOffset = textPosition.getLastLinePosition(pageToSign, marginBottom);
			} else {

				yOffset = textPosition.getCharacterPosition(pageToSign, marginBottom, getOptions()
					.getReferenceText(), true);

			}

			Float sigHeight = signatureHeight;

			if (lastLineDistance < 0) {
				sigHeight = 0F;
			}

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

		Rectangle pCoords = new Rectangle(pLeft, pBottom, pRight, pTop);

		appearance.setVisibleSignature(pCoords, pageToSign, null);

	}

	public void addImageToLayer(PdfTemplate n2, Image im) throws DocumentException,
		MalformedURLException, IOException {

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

	public void addTextToLayer(Integer layer, String text) throws IOException, DocumentException {

		Float fontSize = 10F;

		BaseFont bf = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
		Float tWidth = bf.getWidthPoint(text, fontSize);

		// TODO: Estudar altura de caracteres para melhor determinação da margem
		// 1.2F = 20% de margem de segurança para caracteres fora de linha
		signatureHeight = fontSize * 1.2F;
		signatureWidth = tWidth + 2F;

		offsetSignature();

		PdfTemplate n2 = appearance.getLayer(layer);

		n2.beginText();
		n2.setFontAndSize(bf, fontSize);
		n2.showTextAligned(PdfTemplate.ALIGN_CENTER, text, signatureWidth / 2, signatureHeight
			- fontSize, 0);
		n2.endText();

	}

	public File getOutputFile(File inputFile) throws IOException {

		String inputFilename = inputFile.getName().substring(0,
			inputFile.getName().lastIndexOf("."));

		if (options != null && options.getOutputMaskRegex()) {

			String r = options.getMaskRegex();

			if (!(r == null || r.length() < 1)) {

				String text = textPosition.getText(1);

				Matcher m = Pattern.compile(r).matcher(text);

				if (m.find()) {

					inputFilename = m.group();

				}

			}

		}

		String inputParent = inputFile.getParent();

		String outputFilename = inputParent + File.separator + getOptions().getOutputPreffix()
			+ inputFilename + getOptions().getOutputSuffix() + ".pdf";

		if (!options.getOutputOverwriteOriginal()) {

			String baseName = FilenameUtils.getBaseName(outputFilename);

			String extension = FilenameUtils.getExtension(outputFilename);

			File outputFile = new File(outputFilename);

			int i = 1;

			while (outputFile.exists()) {

				outputFile = new File(outputFile.getParentFile(), baseName + "(" + i++ + ")."
					+ extension);

			}

			outputFilename = outputFile.getAbsolutePath();

		}

		return new File(outputFilename);

	}

}
