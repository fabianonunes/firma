package tc.fab.mechanisms;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

public interface Mechanism {

	void login() throws Exception;

	void logout() throws Exception;

	PrivateKey getPrivateKey(String alias) throws Exception;

	X509Certificate getCertificate(String alias) throws Exception;

	Certificate[] getCertificateChain(String alias) throws Exception;

	ArrayList<String> aliases() throws Exception;

}
