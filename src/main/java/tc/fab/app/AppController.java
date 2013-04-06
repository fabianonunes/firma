package tc.fab.app;

import javax.swing.ActionMap;

import tc.fab.pdf.signer.options.AppearanceOptions;

public interface AppController {

	static final String ACTION_FILES_SIGN = "firma.view.files_sign";
	static final String ACTION_FILES_REMOVE = "firma.view.files_remove";
	static final String ACTION_DROPAREA_SHOW = "firma.view.droparea_show";
	static final String ACTION_FILES_FLAT = "firma.view.files_flat";

	ActionMap getActionMap();

	boolean saveBeforeExit();

	void signFiles(String provider, String alias, AppearanceOptions appearanceOptions) throws Exception;

}
