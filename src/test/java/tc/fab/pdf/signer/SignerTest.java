package tc.fab.pdf.signer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import tc.fab.mechanisms.Mechanism;
import tc.fab.mechanisms.Pkcs11Config;
import tc.fab.mechanisms.callback.SimplePasswordCallback;
import tc.fab.pdf.signer.message.Envelope;
import tc.fab.pdf.signer.message.Message;
import tc.fab.pdf.signer.message.MessageAdapter;
import tc.fab.pdf.signer.message.SignatureClaim;
import tc.fab.pdf.signer.options.AppearanceOptions;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;

public class SignerTest extends TestCase {

	String eTPassword;
	private Pkcs11Config config;
	private Mechanism m;
	private ArrayList<String> libs;
	File file_to_sign = new File("/tmp/ram/sign.pdf");
	File output_file = new File("/tmp/ram/signed.pdf");

	public void setUp() throws Exception {

		InputStream is = getClass().getResourceAsStream("/eTPassword.text");
		eTPassword = IOUtils.toString(is);

		String pkcs11Module = "/usr/lib/libaetpkss.so";
		pkcs11Module = "/usr/lib/libeTPkcs11.so";

		libs = new ArrayList<>();
		libs.add(pkcs11Module);

		config = new Pkcs11Config(libs, new SimplePasswordCallback(eTPassword.toCharArray()));
		config.loadPkcs11Wrapper();

		ArrayList<String> aliases = config.aliases(pkcs11Module);
		m = config.getMechanism(pkcs11Module, aliases.get(0));
		m.login();

	}

	public void test() throws Exception {

		File file_to_save = new File("/tmp/ram/save_blank.pdf");

		// AppearanceOptions options = new AppearanceOptions();

		PdfReader reader = new PdfReader(file_to_sign.getAbsolutePath());
		PdfStamper stamper = PdfStamper.createSignature(reader, null, '\0', file_to_save, true);
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();

		Message envelope = SteppedSigner.getSignableStream(appearance, m.getCertificateChain());

		String jsonMessage = envelope.toJson();

		System.out.println(jsonMessage);

		envelope = MessageAdapter.fromJson(jsonMessage, Envelope.class);

		byte[] signed = m.sign(envelope.getMessage());

		SignatureClaim claim = new SignatureClaim(envelope.getHash(), signed,
			m.getCertificateChain(), envelope.getTime());

		String jsonClaim = claim.toJson();

		System.out.println(jsonClaim);

		claim = (SignatureClaim) MessageAdapter.fromJson(jsonClaim, SignatureClaim.class);

		System.out.println(claim.getChain()[0]);

		SteppedSigner.signDeferred(new PdfReader("/tmp/ram/save_blank.pdf"), output_file, claim);

	}

	public void testDocumentSigner() throws IOException, DocumentException,
		GeneralSecurityException {

		AppearanceOptions options = new AppearanceOptions();
		options.setReferenceText("Page 1");
		try (DocumentSigner signer = new DocumentSigner(options, file_to_sign)) {
			signer.sign(m, output_file);
		}

	}

	@Override
	protected void tearDown() throws Exception {
		System.out.println("tearing down...");
		config.finalizeModules();
	}

}
