package tc.fab.firma;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ActionMap;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.math.RandomUtils;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;

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
	@SuppressWarnings("unused")
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
		// File[] selectedFile = fileDialog.get().selectFile();
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
	public PreviewTask previewFile() {

		return new PreviewTask(view.getFileModel());

	}

	class PreviewTask extends Task<Void, Pair<String, Integer>> {

		DefaultTableModel model;
		List<Integer> indexes;

		public PreviewTask(DefaultTableModel defaultTableModel) {
			super(context.getAppContext().getApplication());
			model = defaultTableModel;
			indexes = new ArrayList<>();
			for (int row = 0; row < model.getRowCount(); row++) {
				indexes.add(view.getFileTable().convertRowIndexToModel(row));
			}

		}

		@Override
		protected Void doInBackground() throws Exception {

			for (Integer row : indexes) {
				publish(new Pair<String, Integer>("starting", row));
				long time = (long) (RandomUtils.nextFloat() * 1000l);
				System.out.println(time);
				Thread.sleep(time);
				publish(new Pair<String, Integer>("ending", row));

			}

			return null;

		}

		@Override
		protected void process(List<Pair<String, Integer>> values) {
			super.process(values);
			long time = System.currentTimeMillis();
			for (Pair<String, Integer> pair : values) {
				if (pair.getFirst().equals("starting")) {
					view.getFileTable().setWaiting(pair.getSecond());
				} else {
					if (time % 2 == 0 || time % 3 == 0) {
						view.getFileTable().setDone(pair.getSecond());
					} else {
						view.getFileTable().setError(pair.getSecond());
					}
				}
			}
		}

	}

	public class Pair<U, V> {
		private U first;
		private V second;

		public Pair(U first, V second) {
			this.first = first;
			this.second = second;
		}

		public U getFirst() {
			return first;
		}

		public V getSecond() {
			return second;
		}

	}

	@Action(name = AppController.ACTION_DROPAREA_SHOW)
	public void showDropArea() {
	}

	@Action(name = AppController.ACTION_SETTINGS)
	public void settings() {
	}

}
