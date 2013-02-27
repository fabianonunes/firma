package tc.fab.mechanisms;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.List;

import javax.security.auth.login.LoginException;

public class MscapiConfig {

	private KeyStore keystore;

	public MscapiConfig() throws KeyStoreException, NoSuchProviderException,
		NoSuchAlgorithmException, CertificateException, IOException {
		keystore = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
		keystore.load(null, null);
	}

	public Mechanism getMechanism(String alias) {
		return new WindowsMyMechanism(alias, keystore);
	}

	private class WindowsMyMechanism extends CommonMechanism {

		public WindowsMyMechanism(String alias, KeyStore keystore) {
			this.keystore = keystore;
			setAlias(alias);
		}

		@Override
		public void login() {
		}

		@Override
		public void logout() throws LoginException {
		}

	}

	public List<String> aliases() throws KeyStoreException {
		return Collections.list(keystore.aliases());
	}

}
