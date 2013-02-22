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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void startActionManually(String actionName) {
		// TODO Auto-generated method stub

	}

	@Action
	public void quitApplication() {
		context.getAppContext().getApplication().exit();
	}

	@Action(name = AppController.ACTION_FILES_ADD)
	public void addFile() {
		File[] selectedFile = fileDialog.get().selectFile();
	}

	public void addFolder(File folder) {
	}

	public void sign() {
	}

	public void removeSelected() {
	}

	@Override
	public void selectAlias() {
	}

	@Override
	public void showDropArea() {
	}

	@Action(name = AppController.ACTION_SETTINGS)
	public void settings() {
	}

}
