package tc.fab.mechanisms;

import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;

public interface Mechanism extends AutoCloseable {

	void login() throws Exception;

	void logout() throws Exception;

	PrivateKey getPrivateKey() throws UnrecoverableKeyException, KeyStoreException,
		NoSuchAlgorithmException;

	X509Certificate getCertificate() throws Exception;

	Certificate[] getCertificateChain() throws KeyStoreException;

	List<String> aliases() throws KeyStoreException;

	String getAlias();

	void setAlias(String alias);

	byte[] sign(byte[] data) throws NoSuchAlgorithmException, KeyStoreException,
		InvalidKeyException, UnrecoverableKeyException, SignatureException;

}
