package tc.fab.pdf.signer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import tc.fab.mechanisms.Mechanism;
import tc.fab.mechanisms.Pkcs11Config;
import tc.fab.mechanisms.callback.PINCallback;
import tc.fab.mechanisms.callback.SimplePasswordCallback;

public class SignerTest {

	@Test
	public void test() throws Exception {
		
		File file_to_sign = new File("/tmp/ram/sign.pdf");
		File file_to_save = new File("/tmp/ram/save.pdf");
		
		String pkcs11Module = "/usr/lib/libaetpkss.so";
		List<String> lib = new ArrayList<>();
		lib.add(pkcs11Module);
		
		Pkcs11Config config = new Pkcs11Config(
			lib,
			new PINCallback(null)
		);
		
		config.loadPkcs11Wrapper();
		
		ArrayList<String> aliases = config.aliases(pkcs11Module);
		
		Mechanism m = config.getMechanism(pkcs11Module, aliases.get(0));
		
		m.login();
		
		Signer signer = new Signer(m);
		
		SignatureAppearance sapp = new SignatureAppearance(signer);
		
		sapp.sign(file_to_sign, file_to_save);
		
		
		
		
	}

}
