package tc.fab.mechanisms;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.apache.commons.io.IOUtils;

import sun.security.pkcs11.SunPKCS11;

import com.google.inject.Inject;

@SuppressWarnings("restriction")
public class SmartCardAdapter extends CommonMechanism {

	CallbackHandler handler;
	String alias;

	@Inject
	public SmartCardAdapter(CallbackHandler handler) {
		this.handler = handler;
	}

	public void registerProvider(String libraryPath) {

		Provider previousProvider = Security.getProvider("SunPKCS11-Firma");
		if (previousProvider != null) {
			Security.removeProvider(previousProvider.getName());
		}

		StringBuffer config = new StringBuffer();
		config.append("name = Firma\n");
		config.append("library = " + libraryPath);

		InputStream is = IOUtils.toInputStream(config);

		provider = new SunPKCS11(is);

		Security.addProvider(provider);

		// boolean isLinux = System.getProperty("os.name").equals("Linux");

	}

	@Override
	public void login() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
		IOException {

		KeyStore.Builder builder = KeyStore.Builder.newInstance("PKCS11", provider,
			new KeyStore.CallbackHandlerProtection(handler));

		keystore = builder.getKeyStore();
		keystore.load(null, null);
		alias = keystore.aliases().nextElement();

		Enumeration<String> aliases = keystore.aliases();

		while (aliases.hasMoreElements()) {
			System.out.println("--" + aliases.nextElement());
		}

	}

	public void logout() throws LoginException {
		provider.logout();
		Security.removeProvider(provider.getName());
	}

}
