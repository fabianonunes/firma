package tc.fab.firma.app;

import java.io.File;

import javax.swing.ActionMap;

public interface AppController {

	static final String ACTION_FILES_ADD = "firma.view.files_add";

	ActionMap getActionMap();

	boolean saveBeforeExit();

	void startActionManually(String actionName);

	void addFile(File file);

	void addFolder(File folder);

	void sign();

	void removeSelected();

	void selectAlias();

	void showDropArea();

	void settings();

}
