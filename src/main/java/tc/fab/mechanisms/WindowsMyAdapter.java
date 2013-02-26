package tc.fab.mechanisms;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import javax.security.auth.login.LoginException;

public class WindowsMyAdapter extends CommonMechanism {

	public WindowsMyAdapter() {
	}

	@Override
	public void login() throws KeyStoreException, NoSuchProviderException,
			NoSuchAlgorithmException, CertificateException, IOException {
		keystore = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
		keystore.load(null, null);
	}

	@Override
	public void logout() throws LoginException {
	}

}
