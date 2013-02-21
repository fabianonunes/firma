package tc.fab.mechanisms;

import java.security.AuthProvider;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public abstract class CommonMechanism implements Mechanism {

	protected AuthProvider	provider;
	protected KeyStore		keystore;
	protected String		alias;

	@Override
	public PrivateKey getPrivateKey() throws UnrecoverableKeyException, KeyStoreException,
		NoSuchAlgorithmException {
		return (PrivateKey) keystore.getKey(alias, null);

	}

	@Override
	public X509Certificate getCertificate() throws KeyStoreException {
		return (X509Certificate) keystore.getCertificate(alias);

	}

	@Override
	public Certificate[] getCertificateChain() throws KeyStoreException {
		return keystore.getCertificateChain(alias);
	}

	public KeyStore getKeystore() {
		return keystore;
	}

}
