package tc.fab.app;

import javax.swing.ActionMap;

import tc.fab.pdf.signer.options.AppearanceOptions;

public interface AppController {

	static final String ACTION_FILES_ADD = "firma.view.files_add";
	static final String ACTION_SETTINGS = "firma.view.settings";
	static final String ACTION_FILES_SIGN = "firma.view.files_sign";
	static final String ACTION_FILES_REMOVE = "firma.view.files_remove";
	static final String ACTION_FILE_PREVIEW = "firma.view.file_preview";
	static final String ACTION_DROPAREA_SHOW = "firma.view.droparea_show";
	static final String ACTION_FOLDER_ADD = "firma.view.folder_add";

	ActionMap getActionMap();

	boolean saveBeforeExit();

	void startActionManually(String actionName);

	void signFiles(String provider, String alias, AppearanceOptions options) throws Exception;
	
}
