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

	/**
	 * Remove all PKCS11 provider and register a new.
	 * 
	 * @param pkcs11Module
	 *            a complete file path to a pkcs11 module (.so or .dll)
	 * @param slotId
	 *            some pkcs11 modules, aetpkss1 e.g., try to load token in all
	 *            slots. if two smartcards are connected, it may try load the
	 *            wrong slot and throw CKR_TOKEN_NOT_RECOGNIZED
	 * @return a new Mechanism
	 */
	public Mechanism registerProvider(String pkcs11Module, Long slotId) {

		//

		java.security.Provider previousProvider = Security.getProvider("SunPKCS11-Firma");
		if (previousProvider != null) {
			Security.removeProvider(previousProvider.getName());
		}

		StringBuffer config = new StringBuffer();
		config.append("name = Firma");
		if (slotId != null) {
			config.append("\nslot = " + slotId);
		}
		config.append("\nlibrary = " + pkcs11Module);

		InputStream is = IOUtils.toInputStream(config);

		provider = new SunPKCS11(is);

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
