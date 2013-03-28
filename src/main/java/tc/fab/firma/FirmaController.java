package tc.fab.firma;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ActionMap;

import org.jdesktop.application.Action;
import org.jdesktop.application.Task;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.app.AppDocument;
import tc.fab.app.AppView;
import tc.fab.firma.app.components.JFileTable.FileModel;
import tc.fab.firma.app.components.JFileTable.FileModel.Status;
import tc.fab.firma.app.dialogs.FileSelectorDialog;
import tc.fab.firma.app.dialogs.SignDocumentDialog;
import tc.fab.mechanisms.Mechanism;
import tc.fab.mechanisms.MechanismManager;
import tc.fab.pdf.signer.DocumentSigner;
import tc.fab.pdf.signer.options.AppearanceOptions;

import com.google.inject.Provider;

@Singleton
public class FirmaController implements AppController {

	private AppContext context;
	private AppView view;
	// private AppDocument document;

	private ActionMap actionMap;

	// dialogs
	@Inject
	private Provider<FileSelectorDialog> fileDialog;
	@Inject
	private Provider<SignDocumentDialog> optionsDialog;

	private MechanismManager providersManager;

	@Inject
	public FirmaController(AppContext context, AppDocument document, AppView view,
		MechanismManager providersManager) {
		this.context = context;
		this.view = view;
		// this.document = document;
		this.providersManager = providersManager;
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

	@Override
	public void signFiles(String provider, String alias, AppearanceOptions options)
		throws Exception {
		Mechanism m = providersManager.getMechanism(provider, alias);
		PreviewTask p = new PreviewTask(m, options);
		p.execute();
	}

	class PreviewTask extends Task<Void, Pair<Status, Integer>> {

		Map<Integer, FileModel> indexes;
		private Mechanism m;
		private AppearanceOptions options;

		public PreviewTask(Mechanism m, AppearanceOptions options) throws Exception {
			super(context.getAppContext().getApplication());

			this.m = m;
			m.login();

			this.options = options;

			int rowCount = view.getFileTable().getModel().getRowCount();

			indexes = new LinkedHashMap<>();

			for (int viewRow = 0; viewRow < rowCount; viewRow++) {
				view.getFileTable().setStatus(viewRow, Status.IDLE);
				int modelRow = view.getFileTable().convertRowIndexToModel(viewRow);
				indexes.put(modelRow, view.getFileTable().getData().get(modelRow));
			}

		}

		@Override
		protected Void doInBackground() throws Exception {

			for (Integer modelRow : indexes.keySet()) {

				FileModel fileModel = indexes.get(modelRow);
				File file = fileModel.getFile();

				publish(new Pair<Status, Integer>(Status.LOADING, modelRow));

				try (DocumentSigner signer = new DocumentSigner(options, file)) {
					signer.sign(m, " assinado");
					publish(new Pair<Status, Integer>(Status.DONE, modelRow));
				} catch (Exception e) {
					e.printStackTrace();
					publish(new Pair<Status, Integer>(Status.FAILED, modelRow));
				}

			}

			return null;

		}

		@Override
		protected void process(List<Pair<Status, Integer>> values) {
			super.process(values);
			for (Pair<Status, Integer> pair : values) {
				int viewRow = view.getFileTable().convertRowIndexToView(pair.getSecond());
				view.getFileTable().setStatus(viewRow, pair.getFirst());
			}
		}

		@Override
		protected void finished() {
			try {
				m.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
