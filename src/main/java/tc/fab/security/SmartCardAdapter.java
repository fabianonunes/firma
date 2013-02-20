package tc.fab.security;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.security.auth.callback.CallbackHandler;

public class SmartCardAdapter extends AbstractKeyStoreAdapter {

    public KeyStore login(CallbackHandler handler) throws KeyStoreException,
	    NoSuchAlgorithmException, CertificateException, IOException {

	KeyStore.Builder builder = KeyStore.Builder.newInstance("PKCS11",
		provider, new KeyStore.CallbackHandlerProtection(handler));

	keystore = builder.getKeyStore();

	keystore.load(null, null);
	
	return keystore;

    }

}
