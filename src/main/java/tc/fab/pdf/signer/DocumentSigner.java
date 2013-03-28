package tc.fab.pdf.signer;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import tc.fab.mechanisms.Mechanism;
import tc.fab.pdf.signer.options.AppearanceOptions;
import tc.fab.pdf.signer.options.PdfTextPosition;
import tc.fab.pdf.signer.options.ReferencePosition;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.ProviderDigest;

public class DocumentSigner implements AutoCloseable {

	private AppearanceOptions options;

	private PdfReader reader;
	private PdfStamper stamper;
	private PdfTextPosition textPosition;
	private PdfSignatureAppearance appearance;

	private File inputFile;

	public DocumentSigner(AppearanceOptions options, File inputFile) throws IOException,
		DocumentException {
		this.options = options;
		this.inputFile = inputFile;
	}

	/**
	 * Signs a PDF file
	 * 
	 * @param mechanism
	 * @param inputFile
	 * @param options
	 * 
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void sign(Mechanism mechanism, String suffix) throws GeneralSecurityException,
		IOException, DocumentException {

		close();

		File tmpFile = File.createTempFile("pdf", "sgn");

		reader = new PdfReader(makeRaf(), null);
		stamper = PdfStamper.createSignature(reader, null, '\0', tmpFile, true);
		textPosition = new PdfTextPosition(reader, getPageToSign());
		appearance = stamper.getSignatureAppearance();

		applyOptions(options);

		ExternalSignature external = new PrivateKeySignature(mechanism.getPrivateKey(), "SHA-1",
			null);

		ExternalDigest externalDigest = new ProviderDigest(null);

		MakeSignature.signDetached(appearance, externalDigest, external,
			mechanism.getCertificateChain(), null, null, null, 0, CryptoStandard.CMS);

		FileUtils.moveFile(tmpFile, getOutputFile(inputFile, suffix));

	}

	private File getOutputFile(File inputFile, String suffix) {

		String baseName = FilenameUtils.getBaseName(inputFile.getName());
		String extension = FilenameUtils.getExtension(inputFile.getName());

		File outputFile = new File(inputFile.getParent(), baseName + suffix + "." + extension);

		baseName = FilenameUtils.getBaseName(outputFile.getAbsolutePath());
		extension = FilenameUtils.getExtension(outputFile.getAbsolutePath());

		int i = 1;
		while (outputFile.exists()) {
			outputFile = new File(outputFile.getParentFile(), baseName + "(" + i++ + ")."
				+ extension);
		}

		return outputFile;

	}

	private RandomAccessFileOrArray makeRaf() throws IOException {
		RandomAccessSource factory = new RandomAccessSourceFactory().createBestSource(inputFile
			.getAbsolutePath());
		return new RandomAccessFileOrArray(factory);
	}

	private void applyOptions(AppearanceOptions options) throws IOException {

		String reference = options.getReferenceText();
		Integer pageToSign = getPageToSign();

		float signatureWidth = cmToPoint(options.getSignatureWidth());
		float signatureHeight = cmToPoint(options.getSignatureHeight());

		Rectangle pageSize = reader.getPageSize(pageToSign);
		float pageWidth = pageSize.getWidth();

		float yOffset = (reference != null) ? textPosition.getCharacterPosition(reference)
			: textPosition.getLastLinePosition();

		float pLeft = pageWidth / 2 - signatureWidth / 2;
		float pRight = pLeft + signatureWidth;
		float pTop;

		if (options.getReferencePosition().equals(ReferencePosition.ABOVE)) {
			pTop = yOffset + cmToPoint(options.getReferenceDistance()) + signatureHeight;
		} else {
			pTop = yOffset - cmToPoint(options.getReferenceDistance());
		}
		
		float pBottom = pTop - signatureHeight;

		Rectangle pCoords = new Rectangle(pLeft, pBottom, pRight, pTop);

		appearance.setRenderingMode(options.getRenderMode());
		appearance.setLocation(options.getLocal());
		appearance.setReason(options.getReason());
		appearance.setVisibleSignature(pCoords, pageToSign, null);

	}

	/**
	 * utility methodo to convert a measure in cm to pdf points (72 dpi)
	 * 
	 * @param cm
	 * @return
	 */
	private static Float cmToPoint(Number cm) {
		return (cm.floatValue() / 2.54F) * 72;
	}

	private Integer getPageToSign() {
		Integer pageToSign = options.getPageToSign();
		Integer totalPages = reader.getNumberOfPages();
		if (pageToSign <= 0) {
			return totalPages;
		} else {
			return (pageToSign > totalPages) ? totalPages : pageToSign;
		}
	}

	@Override
	public void close() {
		if (reader != null) {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
		if (stamper != null) {
			try {
				stamper.close();
			} catch (Exception e) {
			}
		}
	}

	// TSAClient tsc = null;
	// if (timestamp) {
	// String tsa_url = "http://tsa.swisssign.net";
	// tsc = new TSAClientBouncyCastle(tsa_url, "", "");
	// }

}
