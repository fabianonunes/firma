/*
 * PDFSignerApp.java
 */
package tc.fab.pdf.signer.deprecated;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.error.ErrorLevel;

import tc.fab.firma.FirmaView;
import tc.fab.pdf.signer.options.AppearanceOptions;

@Deprecated
public class PDFSignerApp extends SingleFrameApplication {

	public static PDFSignerApp getApplication() {
		return Application.getInstance(PDFSignerApp.class);
	}

	public static void main(String[] args) {

		// TODO: bug do java: não reconhece o locale default no windows
		Locale locale = new Locale("pt", "BR");
		Locale.setDefault(locale);

		launch(PDFSignerApp.class, args);

	}

	private FirmaView view;

	private AppearanceOptions options;

	public PDFSignerApp() throws IOException {
		super();
	}

	@Action
	public void addFiles() throws FileNotFoundException, Exception {

		// FileSelectorDialog c = new
		// FileSelectorDialog(getApplication().getMainFrame(), true);
		//
		// FileFilter extf = new ExtensionFilter(new String[] { "pdf" });
		//
		// JFileChooser fileChooser = c.getJFileChooser();
		// fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// fileChooser.setMultiSelectionEnabled(true);
		// fileChooser.setFileFilter(extf);
		//
		// int rv = fileChooser.showOpenDialog(getApplication().getMainFrame());
		//
		// if (rv == JFileChooser.APPROVE_OPTION) {
		//
		// File[] files = fileChooser.getSelectedFiles();
		//
		// FileSelector selector = new FileSelector();
		// selector.setFilter(new ExtensionFilter(new String[] { "pdf" }));
		// selector.addSource(files);
		//
		// getOptions().addSelection(selector);
		//
		// }

	}

	public void attachFileDrop(Component c) {

//		new FileDrop(c, FileDropListener.getInstance());

	}

	@Override
	protected void configureWindow(java.awt.Window root) {
	}

	public AppearanceOptions getOptions() {
		return options;
	}

	@Action
	public void getSelection() throws FileNotFoundException, Exception {

		// FileSelectorDialog c = new
		// FileSelectorDialog(getApplication().getMainFrame(), true);
		//
		// JFileChooser fileChooser = c.getJFileChooser();
		// fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// fileChooser.setMultiSelectionEnabled(false);
		//
		// int rv = fileChooser.showOpenDialog(getApplication().getMainFrame());
		//
		// if (rv == JFileChooser.APPROVE_OPTION) {
		//
		// DirectorySelector ds = new
		// DirectorySelector(fileChooser.getSelectedFile());
		// // TODO: human-check
		// ds.setRecursive(true);
		// FileFilter extf = new ExtensionFilter(new String[] { "pdf" });
		// ds.setFilter(extf);
		//
		// getOptions().addSelection(ds);
		//
		// }

	}

	public FirmaView getView() {
		return view;
	}

	@Override
	protected void initialize(String[] args) {
		super.initialize(args);
	}

	@Action
	public void previewFile() {
	}

	@Action
	public void removeSelecteds() {

		// JFileTable tb = getView().getFileTable();
		// tb.removeSelecteds();
		// getOptions().setFiles(tb.getDataFromColumn(1));

	}

	@Action
	public void selectImageFile() throws FileNotFoundException, Exception {

		// FileSelectorDialog c = new
		// FileSelectorDialog(getApplication().getMainFrame(), true);
		// JFileChooser fileChooser = c.getJFileChooser();
		//
		// String[] acceptable = { "png", "jpg" };
		// FileFilter ffilter = new ImageFilter(acceptable);
		//
		// fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// fileChooser.setMultiSelectionEnabled(false);
		// fileChooser.setFileFilter(ffilter);
		//
		// int rv = fileChooser.showOpenDialog(getApplication().getMainFrame());
		//
		// if (rv == JFileChooser.APPROVE_OPTION) {
		//
		// File imgFile = fileChooser.getSelectedFile();
		// getOptions().setImage(imgFile);
		//
		// }

	}

	public void setOptions(AppearanceOptions options) {
		this.options = options;
	}

	public void setView(FirmaView view) {
		this.view = view;
	}

	@Action
	public void showDropBox() {

		// DialogDrop dd = getView().getDialogDrop();
		//
		// if (dd.isVisible()) {
		//
		// dd.setVisible(false);
		//
		// } else {
		//
		// Toolkit tk = Toolkit.getDefaultToolkit();
		//
		// Dimension screen = tk.getScreenSize();
		//
		// Double pTop = 30D;
		//
		// dd.setVisible(true);
		//
		// getMainFrame().setState(Frame.ICONIFIED);
		//
		// Double pLeft = screen.getWidth() - pTop - dd.getWidth();
		//
		// dd.setLocation(pLeft.intValue(), pTop.intValue());
		//
		// }

	}

	@Override
	protected void shutdown() {

		super.shutdown();
	}

	@Action(block = Task.BlockingScope.APPLICATION)
	public Task<Object, Integer> signSelection() throws Throwable {

		class SignSelectionTask extends Task<Object, Integer> {

			Exception ex = null;

			SignSelectionTask(Application app) {

				super(app);

				setUserCanCancel(true);

				addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {

						if (evt.getPropertyName().equals("state")) {

							if (SwingWorker.StateValue.DONE.toString() == evt.getNewValue()
								.toString()) {

								if (ex != null) {

									ErrorInfo info = new ErrorInfo(
										"Erro ao assinar documento",
										"Envie o texto abaixo para https://github.com/fabianonunes/firma/issues",
										null, null, ex, ErrorLevel.SEVERE, null);

									while (ex.getCause() != null) {
										System.out.println(ex.getCause());
										ex = (Exception) ex.getCause();
									}

									JXErrorPane.showDialog(getMainFrame(), info);

								} else {

									JOptionPane.showMessageDialog(getMainFrame(),
										"Todos os documentos foram assinados.",
										"Documentos assinados", JOptionPane.INFORMATION_MESSAGE);

								}

							}
						}

					}
				});

				// setInputBlocker(new ProgressInputBlocker(this,
				// Task.BlockingScope.APPLICATION,
				// PDFSignerApp.getApplication().getMainFrame(), null));

			}

			@Override
			protected Object doInBackground() {

				return null;

			}

			@Override
			protected void failed(Throwable cause) {

				cancel(true);

				// ((ProgressInputBlocker) getInputBlocker()).unblock();

				super.failed(cause);

			}

			@Override
			protected void finished() {

				super.finished();

			}

			@Override
			protected void process(List<Integer> values) {
				super.process(values);
			}

		}

		SignSelectionTask task = new SignSelectionTask(PDFSignerApp.getInstance());

		return task;

	}

	@Override
	protected void startup() {

		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				try {

					String lf = "org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel";

					UIManager.setLookAndFeel(lf);

				} catch (Exception e) {
				}

				show(getView());

			}

		});

	}

}
