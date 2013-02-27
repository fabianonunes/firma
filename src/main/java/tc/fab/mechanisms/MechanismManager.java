package tc.fab.mechanisms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;
import javax.security.auth.callback.CallbackHandler;

import org.apache.commons.lang.SystemUtils;

@Singleton
public class MechanismManager {

	List<Mechanism.Entry> entries = new ArrayList<>();
	Map<Mechanism.Type, Mechanism> mechanisms = new HashMap<>();
	Pkcs11Config pkcs11config;
	MscapiConfig mscapi;

	public MechanismManager(List<String> libraries, CallbackHandler handler) throws Exception {

		pkcs11config = new Pkcs11Config(libraries, handler);

		if (SystemUtils.IS_OS_WINDOWS) {
			entries.add(new Mechanism.Entry(Mechanism.Type.MSCAPI, "Windows Certificate Manager"));
			mscapi = new MscapiConfig();
		}

		for (String libraryPath : libraries) {
			entries.add(new Mechanism.Entry(Mechanism.Type.PKCS11, libraryPath));
		}

	}

	public List<Mechanism.Entry> getAvaliableMechanisms() {
		return entries;
	}

	public List<String> aliases(Mechanism.Entry entry) throws Exception {
		switch (entry.type) {
			case MSCAPI:
				return mscapi.aliases();
			case PKCS11:
				return pkcs11config.aliases(entry.name);
			default:
				return null;
		}
	}

	public Mechanism getMechanism(Mechanism.Entry entry, String alias) {
		switch (entry.type) {
			case PKCS11:
				return pkcs11config.getMechanism(entry.name, alias);
			case MSCAPI:
				return mscapi.getMechanism(alias);
			default:
				return null;
		}
	}

}
