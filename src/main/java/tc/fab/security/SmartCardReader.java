package tc.fab.security;

import java.io.InputStream;
import java.security.AuthProvider;
import java.security.Provider;
import java.security.Security;

import sun.security.pkcs11.SunPKCS11;

@SuppressWarnings("restriction")
public class SmartCardReader {

	private AuthProvider provider;

	public SmartCardReader() {

		InputStream is;

		if (System.getProperty("os.name").equals("Linux")) {

			is = SmartCard.class.getResourceAsStream("resources/linux-pkcs11.cfg");

		} else {

			is = SmartCard.class.getResourceAsStream("resources/pkcs11.cfg");
		}

		provider = new SunPKCS11(is);

		Provider existingProvider = Security.getProvider("SunPKCS11-Firma");

		if (existingProvider != null) {
			Security.removeProvider(provider.getName());
		}

		Security.addProvider(provider);

	}

	public AuthProvider getProvider() {
		return provider;
	}

}
