package tc.fab.mechanisms;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateException;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import sun.security.pkcs11.SunPKCS11;

import com.google.inject.Inject;

@SuppressWarnings("restriction")
public class SmartCardAdapter extends CommonMechanism {

	CallbackHandler	handler;
	String			alias;

	@Inject
	public SmartCardAdapter(CallbackHandler handler) {

		this.handler = handler;

		// TODO: smell constructor: doing things?
		boolean isLinux = System.getProperty("os.name").equals("Linux");

		String resourceName = isLinux ? "resources/linux-pkcs11.cfg" : "resources/pkcs11.cfg";

		provider = new SunPKCS11(getClass().getResourceAsStream(resourceName));

		Provider previousProvider = Security.getProvider("SunPKCS11-Firma");

		if (previousProvider != null) {
			Security.removeProvider(provider.getName());
		}

		Security.addProvider(provider);

	}

	@Override
	public void login() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
		IOException {

		KeyStore.Builder builder = KeyStore.Builder.newInstance("PKCS11", provider,
			new KeyStore.CallbackHandlerProtection(handler));

		keystore = builder.getKeyStore();
		keystore.load(null, null);
		// TODO: alias selection
		alias = keystore.aliases().nextElement();

	}

	public void logout() throws LoginException {
		provider.logout();
		Security.removeProvider(provider.getName());
	}

}
