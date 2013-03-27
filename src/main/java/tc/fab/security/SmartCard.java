package tc.fab.security;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Collections;

import javax.security.auth.callback.CallbackHandler;

import tc.fab.mechanisms.callback.SimplePasswordCallback;

public class SmartCard {

	private SmartCardReader reader;

	public SmartCard() {
		reader = new SmartCardReader();
	}

	public void login(CallbackHandler handler) throws KeyStoreException, NoSuchAlgorithmException,
		CertificateException, IOException {

		KeyStore.Builder builder = KeyStore.Builder.newInstance("PKCS11", reader.getProvider(),
			new KeyStore.CallbackHandlerProtection(handler));

		KeyStore store = builder.getKeyStore();

		store.load(null, null);

		for (String alias : Collections.list(store.aliases())) {
			System.out.println(alias);
		}

	}

	public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException,
		CertificateException, IOException {
		SmartCard sc = new SmartCard();
		sc.login(new SimplePasswordCallback("zxcsde7".toCharArray()));
	}

}
