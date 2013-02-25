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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.jdesktop.application.Resource;

import tc.fab.app.AppContext;

import com.google.inject.Inject;

@Singleton
public class Pkcs11Config {

	private File wrapperFile;
	private Map<String, Module> modules = new HashMap<>();

	@Resource(key = "firma.pkcs11.libs.unix")
	private String[] unixLibs = new String[20];

	@Resource(key = "firma.pkcs11.libs.win")
	private String[] winLibs = new String[20];

	@Inject
	public Pkcs11Config(AppContext context) {
		context.getResourceMap().injectFields(this);
	}

	public synchronized void loadPkcs11Wrapper() throws IOException {

		String os = SystemUtils.IS_OS_WINDOWS ? "windows" : "unix";
		String arch = SystemUtils.OS_ARCH.contains("64") ? "64" : "32";
		String libName = SystemUtils.IS_OS_WINDOWS ? "PKCS11Wrapper.dll" : "libpkcs11wrapper.so";

		wrapperFile = new File(SystemUtils.JAVA_IO_TMPDIR, libName);
		wrapperFile.deleteOnExit();

		try (InputStream wrapperLib = Pkcs11Config.class.getResourceAsStream("lib/" + os + "/"
			+ arch + "/" + libName);
			OutputStream fout = new FileOutputStream(wrapperFile);) {
			IOUtils.copy(wrapperLib, fout);
		}

		try {
			addLibraryPath(wrapperFile.getAbsolutePath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<String> getProviders() {
		List<String> existentsLibs = new ArrayList<>();
		for (String libPath : Arrays.asList(SystemUtils.IS_OS_LINUX ? unixLibs : winLibs)) {
			if (libPath != null) {
				if (new File(libPath).exists()) {
					existentsLibs.add(libPath);
				}
			}
		}
		return existentsLibs;
	}

	public synchronized ArrayList<String> getAliases(String pkcs11Module) throws TokenException,
		IOException {

		ArrayList<String> aliases = new ArrayList<>();

		Module module = loadModule(pkcs11Module);

		Slot[] slotsWithToken = module.getSlotList(Module.SlotRequirement.TOKEN_PRESENT);

		for (Slot slot : slotsWithToken) {

			Session session = slot.getToken().openSession(Token.SessionType.SERIAL_SESSION,
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
			}

			session.findObjectsFinal();

		}

		return aliases;

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
			module.finalize(null);
		}
	}

	/**
	 * Adds the specified path to the java library path
	 * 
	 * @param pathToAdd
	 *            the path to add
	 * @throws Exception
	 */
	public static void addLibraryPath(String pathToAdd) throws Exception {
		final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
		usrPathsField.setAccessible(true);

		// get array of paths
		final String[] paths = (String[]) usrPathsField.get(null);

		// check if the path to add is already present
		for (String path : paths) {
			if (path.equals(pathToAdd)) {
				return;
			}
		}

		// add the new path
		final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
		newPaths[newPaths.length - 1] = pathToAdd;
		usrPathsField.set(null, newPaths);
	}

}
