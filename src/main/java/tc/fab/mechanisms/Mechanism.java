package tc.fab.mechanisms;

import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;

public interface Mechanism {

	public enum Type {
		PKCS11, MSCAPI
	}

	void login() throws Exception;

	void logout() throws Exception;

	PrivateKey getPrivateKey() throws Exception;

	X509Certificate getCertificate() throws Exception;

	Certificate[] getCertificateChain() throws Exception;

	public class Entry {

		Mechanism.Type type;
		String name;

		public Entry(Mechanism.Type type, String name) {
			this.name = name;
			this.type = type;
		}

		@Override
		public String toString() {
			return this.name;
		}

	}

	List<String> aliases() throws KeyStoreException;

	String getAlias();

	void setAlias(String alias);

}
