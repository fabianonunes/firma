package tc.fab.firma.utils;

import java.lang.reflect.Field;
import java.util.Arrays;

public class SystemHelper {

	/**
	 * Adds the specified path to the java library path
	 * 
	 * @param pathToAdd
	 *            the path to add
	 * @throws Exception
	 */
	public static void addLibraryPath(String pathToAdd) throws Exception {
		final Field usrPaths = ClassLoader.class.getDeclaredField("usr_paths");
		usrPaths.setAccessible(true);

		final String[] paths = (String[]) usrPaths.get(null);

		// check if the path to add is already present
		for (String path : paths) {
			if (path.equals(pathToAdd)) {
				return;
			}
		}

		// add the new path
		final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
		newPaths[newPaths.length - 1] = pathToAdd;
		usrPaths.set(null, newPaths);
	}

}
