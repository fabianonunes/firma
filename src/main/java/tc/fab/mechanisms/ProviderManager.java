package tc.fab.mechanisms;

import iaik.pkcs.pkcs11.TokenException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang.SystemUtils;

@Singleton
public class ProviderManager {

	private Pkcs11Config pkcs11Config;
	private String certmgr = "Windows Certificate Manager";

	@Inject
	public ProviderManager(Pkcs11Config pkcs11Config) {
		this.pkcs11Config = pkcs11Config;
	}

	public List<String> getProviders() {
		List<String> providers = new ArrayList<>();
		if (SystemUtils.IS_OS_WINDOWS) {
			providers.add(certmgr);
		}
		providers.addAll(pkcs11Config.getProviders());
		return providers;
	}

	public ArrayList<String> getAliases(String provider) throws TokenException, IOException {
		if (!provider.equals(certmgr)) {
			return pkcs11Config.getAliases(provider);
		}
		// TODO: a Mechanism to windows-cert-mgre
		return null;

	}

	public Mechanism getMechanism(String provider, String alias) {
		if (!provider.equals(certmgr)) {
			return new SmartCardAdapter(provider);
		}
		// TODO: a Mechanism to windows-cert-mgre
		return null;

	}
}
