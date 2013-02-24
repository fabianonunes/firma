package tc.fab.firma;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ActionMap;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.app.AppDocument;
import tc.fab.app.AppView;
import tc.fab.firma.app.dialogs.FileSelectorDialog;
import tc.fab.firma.app.dialogs.SignDocumentDialog;

import com.google.inject.Provider;

@Singleton
public class FirmaController implements AppController {

	private AppContext context;
	private AppView view;
	private AppDocument document;

	private ActionMap actionMap;

	// dialogs
	@Inject
	private Provider<FileSelectorDialog> fileDialog;
	@Inject
	private Provider<SignDocumentDialog> optionsDialog;

	@Inject
	public FirmaController(AppContext context, AppDocument document, AppView view) {
		this.context = context;
		this.view = view;
		this.document = document;
	}

	@Override
	public ActionMap getActionMap() {
		if (actionMap == null) {
			actionMap = context.getAppContext().getActionMap(FirmaController.class, this);
		}
		return actionMap;
	}

	@Override
	public boolean saveBeforeExit() {
		return true;
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
		optionsDialog.get().open();
	}

	@Action(name = AppController.ACTION_FILES_REMOVE)
	public void removeFile() {
	}

	@Action(name = AppController.ACTION_FILE_PREVIEW)
	public void previewFile() {
		System.out.println("preview");
	}

	@Action(name = AppController.ACTION_DROPAREA_SHOW)
	public void showDropArea() {
	}

	@Action(name = AppController.ACTION_SETTINGS)
	public void settings() {
	}

	@Override
	public void detectModules() {
		ResourceMap resourceMap = context.getAppContext().getResourceMap();
	}

}
