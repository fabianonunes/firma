package tc.fab.security;

import java.io.InputStream;
import java.security.AuthProvider;
import java.security.Provider;
import java.security.Security;

import sun.security.pkcs11.SunPKCS11;

@SuppressWarnings("restriction")
public class ProviderManager {

    private AuthProvider provider;

    public ProviderManager() {

	String resourceName = System.getProperty("os.name").equals("Linux") ? "resources/linux-pkcs11.cfg"
		: "resources/pkcs11.cfg";

	InputStream is = getClass().getResourceAsStream(resourceName);

	provider = new SunPKCS11(is);

	Provider previousProvider = Security.getProvider("SunPKCS11-Firma");

	if (previousProvider != null) {
	    Security.removeProvider(provider.getName());
	}

	Security.addProvider(provider);

    }

    public AuthProvider getProvider() {
	return provider;
    }

}
