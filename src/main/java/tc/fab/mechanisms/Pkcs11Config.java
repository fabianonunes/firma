package tc.fab.mechanisms;

import iaik.pkcs.pkcs11.Module;
import iaik.pkcs.pkcs11.Session;
import iaik.pkcs.pkcs11.Slot;
import iaik.pkcs.pkcs11.Token;
import iaik.pkcs.pkcs11.TokenException;
import iaik.pkcs.pkcs11.objects.Attribute;
import iaik.pkcs.pkcs11.objects.Certificate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AuthProvider;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Singleton;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.SystemUtils;

import sun.security.pkcs11.SunPKCS11;
import tc.fab.firma.utils.SystemHelper;
import tc.fab.security.callback.PINCallback.UserCancelledException;

@SuppressWarnings("restriction")
@Singleton
public class Pkcs11Config {

	private static final Logger LOGGER = Logger.getLogger(Pkcs11Config.class.getName());

	private Map<String, Module> modules = new HashMap<>();
	private Map<String, Long> slotIDs = new HashMap<>();

	private File wrapperFile;
	private List<String> pkcs11Modules;
	private CallbackHandler handler;

	public Pkcs11Config(List<String> pkcs11Modules, CallbackHandler handler) {
		this.pkcs11Modules = pkcs11Modules;
		this.handler = handler;
	}

	/**
	 * Load the iaik Pkcs11Wrapper library. Useful for token operations without
	 * opening a session
	 * 
	 * @throws Exception
	 */
	public synchronized void loadPkcs11Wrapper() throws Exception {

		String os = SystemUtils.IS_OS_WINDOWS ? "windows" : "unix";
		String arch = SystemUtils.OS_ARCH.contains("64") ? "64" : "32";
		String libName = SystemUtils.IS_OS_WINDOWS ? "PKCS11Wrapper.dll" : "libpkcs11wrapper.so";

		File wrapperDir = new File(SystemUtils.JAVA_IO_TMPDIR,
			RandomStringUtils.randomAlphanumeric(10));
		wrapperDir.mkdir();
		wrapperDir.getParentFile().deleteOnExit();

		wrapperFile = new File(wrapperDir, libName);
		wrapperFile.deleteOnExit();

		try (InputStream wrapperLib = Pkcs11Config.class.getResourceAsStream("lib/" + os + "/"
			+ arch + "/" + libName);
			OutputStream fout = new FileOutputStream(wrapperFile);) {
			IOUtils.copy(wrapperLib, fout);
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
		}

		SystemHelper.addLibraryPath(wrapperFile.getAbsolutePath());

	}

	public List<String> getPkcs11Modules() {
		return pkcs11Modules;
	}

	public synchronized ArrayList<String> aliases(String pkcs11Module) throws IOException,
		TokenException {

		ArrayList<String> aliases = new ArrayList<>();
		Module module = loadModule(pkcs11Module);
		Slot[] slotsWithToken = module.getSlotList(Module.SlotRequirement.TOKEN_PRESENT);

		for (Slot slot : slotsWithToken) {

			try {

				Token token = slot.getToken();

				Session session = token.openSession(Token.SessionType.SERIAL_SESSION,
					Token.SessionReadWriteBehavior.RO_SESSION, null, null);

				Certificate searchTemplate = new Certificate();

				session.findObjectsInit(searchTemplate);

				for (Object object : session.findObjects(10)) {

					Certificate certificate = (Certificate) object;
					String label = certificate.getLabel().toString();

					if (label.equals("<NULL_PTR>")) {
						label = "0x" + certificate.getAttributeTable().get(Attribute.ID);
					}

					aliases.add(label);
					slotIDs.put(pkcs11Module + label, slot.getSlotID());

				}

				session.findObjectsFinal();

			} catch (TokenException e) {
				LOGGER.warning(e.getMessage());
			}

		}

		return aliases;

	}

	public Long getSlotId(String pkcs11Module, String alias) {
		return slotIDs.get(pkcs11Module + alias);

	}

	private Module loadModule(String pkcs11Module) throws IOException, TokenException {
		if (!modules.containsKey(pkcs11Module)) {
			Module module = Module.getInstance(pkcs11Module, wrapperFile.getAbsolutePath());
			modules.put(pkcs11Module, module);
			module.initialize(null);
		}
		return modules.get(pkcs11Module);
	}

	public void finalizeModules() throws TokenException {
		for (Module module : modules.values()) {
			for (Slot slot : module.getSlotList(Module.SlotRequirement.TOKEN_PRESENT)) {
				try {
					slot.getToken().closeAllSessions();
				} catch (Exception e) {
				}
			}
			module.finalize(null);
		}
	}

	/**
	 * Remove all PKCS11 providers and register a new.
	 * 
	 * @param pkcs11Module
	 *            a complete file path to a pkcs11 module (.so or .dll)
	 * @param alias
	 * @param slotId
	 *            some pkcs11 modules, aetpkss1 e.g., try to load token in all
	 *            slots. if two smartcards are connected, it may try load the
	 *            wrong slot and throw CKR_TOKEN_NOT_RECOGNIZED
	 * @return a new Mechanism
	 */
	public Mechanism getMechanism(String pkcs11Module, String alias) {

		java.security.Provider provider = Security.getProvider("SunPKCS11-Firma");
		if (provider != null) {
			Security.removeProvider(provider.getName());
		}

		StringBuffer config = new StringBuffer();
		config.append("name = Firma");
		config.append("\nslot = " + getSlotId(pkcs11Module, alias));
		config.append("\nlibrary = " + pkcs11Module);

		try (InputStream is = IOUtils.toInputStream(config)) {
			provider = new SunPKCS11(is);
		} catch (IOException e) {
			LOGGER.warning(e.getMessage());
		}

		Security.addProvider(provider);

		return new Pkcs11Adapter(handler, alias);

	}

	public static boolean isCause(Class<? extends Throwable> expected, Throwable exc) {
		return expected.isInstance(exc) || (exc != null && isCause(expected, exc.getCause()));
	}

	private class Pkcs11Adapter extends CommonMechanism {

		private CallbackHandler handler;
		private AuthProvider provider;

		public Pkcs11Adapter(CallbackHandler handler, String alias) {
			this.handler = handler;
			this.provider = (AuthProvider) Security.getProvider("SunPKCS11-Firma");
			setAlias(alias);
		}

		@Override
		public void login() throws KeyStoreException, NoSuchAlgorithmException,
			CertificateException, UnsupportedCallbackException, IOException,
			UserCancelledException, FailedLoginException {

			keystore = KeyStore.getInstance("PKCS11", provider);

			int tries = 0;
			while (tries < 3) {

				PasswordCallback callback = new PasswordCallback("Password: ", false);
				handler.handle(new Callback[] { callback });
				char[] password = callback.getPassword();
				callback.clearPassword();

				try {
					keystore.load(null, password);
					break;
				} catch (IOException e) {
					if (isCause(FailedLoginException.class, e)) {
						tries++;
						continue;
					}
				}
			}

		}

		@Override
		public void logout() throws LoginException {
			provider.logout();
			Security.removeProvider(provider.getName());
		}

	}

}
