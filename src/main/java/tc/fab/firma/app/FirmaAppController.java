package tc.fab.firma.app;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ActionMap;

import org.jdesktop.application.Action;

import tc.fab.firma.app.dialogs.FileSelectorDialog;

import com.google.inject.Provider;

@Singleton
public class FirmaAppController implements AppController {

	private ActionMap actionMap;
	private AppContext context;
	private AppView view;

	// dialogs
	@Inject
	private Provider<FileSelectorDialog> fileDialog;

	@Inject
	public FirmaAppController(AppContext context, AppView view) {
		this.context = context;
		this.view = view;
	}

	@Override
	public ActionMap getActionMap() {
		if (actionMap == null) {
			actionMap = context.getAppContext().getActionMap(FirmaAppController.class, this);
		}
		return actionMap;
	}

	@Override
	public boolean saveBeforeExit() {
		return false;
	}

	@Override
	public void startActionManually(String actionName) {

	}

	@Action(name = AppController.ACTION_FILES_ADD)
	public void addFile() {
		File[] selectedFile = fileDialog.get().selectFile();
	}

	@Action(name = AppController.ACTION_FOLDER_ADD)
	public void addFolder(File folder) {
	}

	@Action(name = AppController.ACTION_FILES_SIGN)
	public void sign() {
	}

	@Action(name = AppController.ACTION_FILES_REMOVE)
	public void removeFile() {
	}

	@Action(name = AppController.ACTION_FILE_PREVIEW)
	public void previewFile() {
	}

	@Override
	public void selectAlias() {
	}

	@Action(name = AppController.ACTION_DROPAREA_SHOW)
	public void showDropArea() {
	}

	@Action(name = AppController.ACTION_SETTINGS)
	public void settings() {
	}

}
