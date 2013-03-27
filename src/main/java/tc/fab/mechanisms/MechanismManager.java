package tc.fab.mechanisms;

import iaik.pkcs.pkcs11.TokenException;

import java.io.File;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.apache.commons.lang.SystemUtils;
import org.jdesktop.application.Resource;

import tc.fab.app.AppContext;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.inject.Inject;

@Singleton
public class MechanismManager {

	@Resource(key = "firma.pkcs11.libs.unix")
	private String[] unixLibs = new String[20];
	@Resource(key = "firma.pkcs11.libs.win")
	private String[] winLibs = new String[20];

	List<String> entries = new ArrayList<>();
	Map<String, Mechanism> mechanisms = new HashMap<>();
	Pkcs11Config pkcs11config;
	MscapiConfig mscapi;

	@Inject
	public MechanismManager(AppContext context, CallbackHandler handler) throws Exception {

		if (context != null) {
			context.getResourceMap().injectFields(this);
		}

		Collection<String> libraries = new ArrayList<>(
			Arrays.asList(SystemUtils.IS_OS_WINDOWS ? winLibs : unixLibs));

		libraries = Collections2.filter(libraries, new Predicate<String>() {
			@Override
			public boolean apply(@Nullable String object) {
				if (object != null && new File(object).exists()) {
					return true;
				}
				return false;
			}
		});

		pkcs11config = new Pkcs11Config(libraries, handler);

		if (SystemUtils.IS_OS_WINDOWS) {
			entries.add(MscapiConfig.MSCAPI_STORE_NAME);
			mscapi = new MscapiConfig();
		}

		for (String libraryPath : libraries) {
			entries.add(libraryPath);
		}

	}

	public List<String> getAvaliableMechanisms() {
		return entries;
	}

	public List<String> aliases(String entry) throws Exception {
		if (entry.equals(MscapiConfig.MSCAPI_STORE_NAME)) {
			return mscapi.aliases();
		} else {
			return pkcs11config.aliases(entry);
		}
	}

	public Mechanism getMechanism(String entry, String alias) {
		if (entry.equals(MscapiConfig.MSCAPI_STORE_NAME)) {
			return mscapi.getMechanism(alias);
		} else {
			return pkcs11config.getMechanism(entry, alias);
		}
	}

	public Certificate getCertificate(String entry, String alias) throws KeyStoreException {
		if (entry.equals(MscapiConfig.MSCAPI_STORE_NAME)) {
			return mscapi.getMechanism(alias).getCertificate();
		} else {
			return pkcs11config.getMechanism(entry, alias).getCertificate();
		}
	}

	public void loadPkcs11Wrapper() throws Exception {
		pkcs11config.loadPkcs11Wrapper();
	}

	public void finalizeModules() throws TokenException, LoginException {
		pkcs11config.finalizeModules();
	}

}
