package tc.fab.firma.app;

import javax.swing.ActionMap;

public interface AppController {

	static final String ACTION_FILES_ADD = "firma.view.files_add";

	ActionMap getActionMap();

	boolean saveBeforeExit();

	void startActionManually(String actionName);

	void selectAlias();

	void showDropArea();

}
