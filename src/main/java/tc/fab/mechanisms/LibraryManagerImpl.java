package tc.fab.mechanisms;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.jdesktop.application.Resource;

import tc.fab.app.AppContext;

public class LibraryManagerImpl implements LibraryManager {

	@Resource(key = "firma.pkcs11.libs.unix")
	private String[] unixLibs = new String[20];

	@Resource(key = "firma.pkcs11.libs.win")
	private String[] winLibs = new String[20];
	
	@Inject
	public LibraryManagerImpl(AppContext context) {
		context.getAppContext().getResourceMap().injectFields(this);
	}
	
	@Override
	public List<String> getLibs() {

		List<String> existentsLibs = new ArrayList<>();

		boolean isLinux = System.getProperty("os.name").equals("Linux");

		for (String libPath : Arrays.asList(isLinux ? unixLibs : winLibs)) {
			if (libPath != null) {
				if (new File(libPath).exists()) {
					existentsLibs.add(libPath);
				}
			}
		}

		return existentsLibs;
	}

}
