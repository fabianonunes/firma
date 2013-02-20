package tc.fab.security;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public interface KeyStoreAdapter {

    public KeyStore login(CallbackHandler handler) throws KeyStoreException,
	    NoSuchAlgorithmException, CertificateException, IOException;

    public void logout() throws LoginException;

    // public KeyStore getKeystore();

    public String getAlias(Integer index) throws KeyStoreException;

    public PrivateKey getPrivateKey(String alias)
	    throws UnrecoverableKeyException, KeyStoreException,
	    NoSuchAlgorithmException;

    public PublicKey getPublicKey(String alias) throws KeyStoreException;

    public X509Certificate getCertificate(Integer index)
	    throws KeyStoreException;

    // public String getProvider();

}