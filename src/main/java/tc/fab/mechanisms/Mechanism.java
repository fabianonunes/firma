package tc.fab.mechanisms;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.security.auth.login.LoginException;

public interface Mechanism {

	void login() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
		IOException;

	void logout() throws LoginException;

	PrivateKey getPrivateKey(String alias) throws UnrecoverableKeyException, KeyStoreException,
		NoSuchAlgorithmException;

	X509Certificate getCertificate(String alias) throws KeyStoreException;

	Certificate[] getCertificateChain(String alias) throws KeyStoreException;

}
