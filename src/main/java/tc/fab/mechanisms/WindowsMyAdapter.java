package tc.fab.mechanisms;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.security.auth.login.LoginException;

public class WindowsMyAdapter extends CommonMechanism {

	@Override
	public void login() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
		IOException {

	}

	@Override
	public void logout() throws LoginException {
	}

}
