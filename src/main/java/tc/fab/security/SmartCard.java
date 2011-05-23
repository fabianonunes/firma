package tc.fab.security;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.security.auth.callback.CallbackHandler;

public class SmartCard extends AbstractKeyStoreAdapter {

	public SmartCard() {
		reader = new SmartCardReader();
	}


	public void login(CallbackHandler handler) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException {

		KeyStore.Builder builder = KeyStore.Builder.newInstance("PKCS11",
				reader.getProvider(), new KeyStore.CallbackHandlerProtection(
						handler));
		
		KeyStore store = builder.getKeyStore();

		setKeystore(store);

		getKeystore().load(null, null);

		setAliases(getKeystore().aliases());

	}

	private void setKeystore(KeyStore keystore) {
		this.keystore = keystore;
	}


	@Override
	public String getProvider() {
		return reader.getProvider().getName();
	}


	
}
