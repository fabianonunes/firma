package tc.fab.mechanisms;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

public interface Mechanism {

	void login() throws Exception;

	void logout() throws Exception;

	PrivateKey getPrivateKey() throws Exception;

	X509Certificate getCertificate() throws Exception;

	Certificate[] getCertificateChain() throws Exception;

	ArrayList<String> aliases() throws Exception;

}
