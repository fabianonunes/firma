package tc.fab.pdf.signer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import tc.fab.mechanisms.Mechanism;
import tc.fab.mechanisms.Pkcs11Config;
import tc.fab.mechanisms.callback.SimplePasswordCallback;

import com.itextpdf.text.pdf.PdfReader;

public class SignerTest extends TestCase {

	String eTPassword;

	public void setUp() throws IOException {
		InputStream is = getClass().getResourceAsStream("/eTPassword.text");
		eTPassword = IOUtils.toString(is);
	}

	public void test() throws Exception {

		File file_to_sign = new File("/tmp/ram/sign.pdf");
		File file_to_save = new File("/tmp/ram/save_blank.pdf");

		String pkcs11Module = "/usr/lib/libeTPkcs11.so";
		List<String> lib = new ArrayList<>();
		lib.add(pkcs11Module);

		Pkcs11Config config = new Pkcs11Config(lib, new SimplePasswordCallback(
			eTPassword.toCharArray()));

		config.loadPkcs11Wrapper();

		ArrayList<String> aliases = config.aliases(pkcs11Module);

		Mechanism m = config.getMechanism(pkcs11Module, aliases.get(0));
		m.login();

		Signer signer = new Signer(m);

		SignatureAppearance sapp = new SignatureAppearance(signer);

		byte[] data_to_sign = sapp.signBlank(file_to_sign, file_to_save, m.getCertificateChain());

		byte[] signed = m.sign(data_to_sign);

		signer.signDeferred(new PdfReader("/tmp/ram/save_blank.pdf"), new File("/tmp/ram/signed.pdf"),
			signed, m.getCertificate());

	}

}
