package tc.fab.pdf.signer;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.commons.io.FileUtils;

import tc.fab.mechanisms.Mechanism;
import tc.fab.pdf.signer.options.AppearanceOptions;

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

public class DocumentSigner {

	private Mechanism mechanism;
	private PdfStamper stamper;
	private PdfSignatureAppearance appearance;
	private PdfReader reader;
	private AppearanceOptions options;

	public DocumentSigner(Mechanism mechanism, AppearanceOptions options) {
		this.mechanism = mechanism;
		this.options = options;
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
	public void sign(File inputFile, File outputFile)
		throws GeneralSecurityException, IOException, DocumentException {

		File tmpFile = File.createTempFile("pdf", "sgn");

		RandomAccessSource factory = new RandomAccessSourceFactory().createBestSource(inputFile
			.getAbsolutePath());
		RandomAccessFileOrArray raf = new RandomAccessFileOrArray(factory);

		reader = new PdfReader(raf, null);
		stamper = PdfStamper.createSignature(reader, null, '\0', tmpFile, true);
		appearance = stamper.getSignatureAppearance();

		applyOptions(options);

		ExternalSignature external = new PrivateKeySignature(mechanism.getPrivateKey(), "SHA-1",
			null);

		ExternalDigest externalDigest = new ProviderDigest(null);

		MakeSignature.signDetached(appearance, externalDigest, external,
			mechanism.getCertificateChain(), null, null, null, 0, CryptoStandard.CMS);

		FileUtils.moveFile(tmpFile, outputFile);

	}

	private void applyOptions(AppearanceOptions options) {

		Float pLeft;
		Float pRight;
		Float pTop;
		Float pBottom;
		Integer pageToSign = getOptions().getPageToSign(reader);
		Rectangle pageSize = reader.getPageSize(pageToSign);

		Float pageHeight = pageSize.getHeight();
		Float pageWidth = pageSize.getWidth();

	}

	// TSAClient tsc = null;
	// if (timestamp) {
	// String tsa_url = "http://tsa.swisssign.net";
	// tsc = new TSAClientBouncyCastle(tsa_url, "", "");
	// }

}
