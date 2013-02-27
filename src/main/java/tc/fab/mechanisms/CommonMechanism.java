package tc.fab.mechanisms;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;

public abstract class CommonMechanism implements Mechanism {

	protected KeyStore keystore;
	protected String alias;

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

	@Override
	public List<String> aliases() throws KeyStoreException {
		return Collections.list(keystore.aliases());
	}

	@Override
	public void login() throws Exception {
	}

	@Override
	public void logout() throws Exception {
	};

	@Override
	public String getAlias() {
		return alias;
	}

	@Override
	public void setAlias(String alias) {
		this.alias = alias;
	}

}
