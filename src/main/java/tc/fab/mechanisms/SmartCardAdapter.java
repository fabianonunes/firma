package tc.fab.mechanisms;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.apache.commons.io.IOUtils;

import sun.security.pkcs11.SunPKCS11;

@SuppressWarnings("restriction")
public class SmartCardAdapter extends CommonMechanism {

	private CallbackHandler handler;

	public SmartCardAdapter(CallbackHandler handler) {
		this.handler = handler;
	}

	public Mechanism registerProvider(String pkcs11Module) {

		java.security.Provider previousProvider = Security.getProvider("SunPKCS11-Firma");
		if (previousProvider != null) {
			Security.removeProvider(previousProvider.getName());
		}

		StringBuffer config = new StringBuffer();
		config.append("name = Firma\n");
		config.append("library = " + pkcs11Module);
		
		System.out.println(config);

		InputStream is = IOUtils.toInputStream(config);

		provider = new SunPKCS11(is);
		
		System.out.println(provider.getName());

		Security.addProvider(provider);

		return this;

	}

	@Override
	public void login() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
		IOException {

		KeyStore.Builder builder = KeyStore.Builder.newInstance("PKCS11", provider,
			new KeyStore.CallbackHandlerProtection(handler));

		keystore = builder.getKeyStore();
		keystore.load(null, null);

	}

	public void logout() throws LoginException {
		provider.logout();
		Security.removeProvider(provider.getName());
	}

}
