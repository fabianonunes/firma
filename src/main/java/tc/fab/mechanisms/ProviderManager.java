package tc.fab.mechanisms;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.security.auth.callback.CallbackHandler;

import org.apache.commons.lang.SystemUtils;

@Singleton
public class ProviderManager {

	// PKCS11
	private Pkcs11Config pkcs11Config;
	private SmartCardAdapter pkcs11;

	// MSCAPI
	private String certmgr = "Windows Certificate Manager";
	private Mechanism mscapi;

	@Inject
	public ProviderManager(Pkcs11Config pkcs11Config, CallbackHandler handler)
			throws Exception {

		this.pkcs11Config = pkcs11Config;
		pkcs11 = new SmartCardAdapter(handler);

		if (SystemUtils.IS_OS_WINDOWS) {
			mscapi = new WindowsMyAdapter();
			mscapi.login();
		}

	}

	public List<String> getProviders() {
		List<String> providers = new ArrayList<>();
		if (SystemUtils.IS_OS_WINDOWS) {
			providers.add(certmgr);
		}
		providers.addAll(pkcs11Config.getProviders());
		return providers;
	}

	public ArrayList<String> aliases(String provider) throws Exception {
		if (!provider.equals(certmgr)) {
			return pkcs11Config.aliases(provider);
		} else {
			return mscapi.aliases();
		}

	}

	public Mechanism getMechanism(String provider, String alias) {
		if (provider.equals(certmgr)) {
			return mscapi;
		} else {
			return pkcs11.registerProvider(provider,
					pkcs11Config.getSlotId(provider, alias));
		}
	}

}
