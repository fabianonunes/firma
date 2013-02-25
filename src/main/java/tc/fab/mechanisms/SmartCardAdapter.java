package tc.fab.mechanisms;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;

import javax.inject.Provider;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.apache.commons.io.IOUtils;

import sun.security.pkcs11.SunPKCS11;

import com.google.inject.Inject;

@SuppressWarnings("restriction")
public class SmartCardAdapter extends CommonMechanism {

	@Inject
	private Provider<CallbackHandler> handler;
	private String pkcs11Module;

	public SmartCardAdapter(String pkcs11Module) {
		this.pkcs11Module = pkcs11Module;
		registerProvider();
	}

	private void registerProvider() {

		java.security.Provider previousProvider = Security.getProvider("SunPKCS11-Firma");
		if (previousProvider != null) {
			Security.removeProvider(previousProvider.getName());
		}

		StringBuffer config = new StringBuffer();
		config.append("name = Firma\n");
		config.append("library = " + pkcs11Module);

		InputStream is = IOUtils.toInputStream(config);

		provider = new SunPKCS11(is);

		Security.addProvider(provider);

	}

	@Override
	public void login() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
		IOException {

		KeyStore.Builder builder = KeyStore.Builder.newInstance("PKCS11", provider,
			new KeyStore.CallbackHandlerProtection(handler.get()));

		keystore = builder.getKeyStore();
		keystore.load(null, null);

	}

	public void logout() throws LoginException {
		provider.logout();
		Security.removeProvider(provider.getName());
	}

}
