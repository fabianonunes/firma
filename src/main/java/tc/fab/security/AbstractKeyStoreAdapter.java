package tc.fab.security;

import java.security.AuthProvider;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;

import javax.security.auth.login.LoginException;

public abstract class AbstractKeyStoreAdapter implements KeyStoreAdapter {

    protected KeyStore keystore;
    protected AuthProvider provider;

    public String getAlias(Integer index) throws KeyStoreException {
	// TODO: respect index
	return keystore.aliases().nextElement();
    }

    public PrivateKey getPrivateKey(String alias)
	    throws UnrecoverableKeyException, KeyStoreException,
	    NoSuchAlgorithmException {

	return (PrivateKey) keystore.getKey(alias, null);

    }

    public PublicKey getPublicKey(String alias) throws KeyStoreException {
	return keystore.getCertificate(alias).getPublicKey();
    }

    public X509Certificate getCertificate(Integer index)
	    throws KeyStoreException {
	return (X509Certificate) keystore.getCertificate(getAlias(index));
    }

    public void logout() throws LoginException {
	// TODO: estudar método logout da classe Token
	provider.logout();
	Security.removeProvider(provider.getName());
    }


}
