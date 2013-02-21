/*
 * PDFSignerApp.java
 */
package tc.fab.pdf.signer.application;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import net.iharder.dnd.FileDrop;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.error.ErrorLevel;

import tc.fab.file.drop.DialogDrop;
import tc.fab.file.selector.DirectorySelector;
import tc.fab.file.selector.FileSelector;
import tc.fab.file.selector.filters.ExtensionFilter;
import tc.fab.file.selector.filters.ImageFilter;
import tc.fab.file.selector.gui.FileSelectorDialog;
import tc.fab.file.selector.gui.JFileTable;
import tc.fab.pdf.signer.FileDropListener;
import tc.fab.pdf.signer.SignatureAppearance;
import tc.fab.pdf.signer.options.SignerOptionDialog;
import tc.fab.pdf.signer.options.SignerOptions;
import tc.fab.security.callback.PINCallback;

public class PDFSignerApp extends SingleFrameApplication {

	public static FileFilter acceptedFiles = new ExtensionFilter(
			new String[] { "pdf" });

	public static PDFSignerApp getApplication() {
		return Application.getInstance(PDFSignerApp.class);
	}

	public static void main(String[] args) {

		// TODO: bug do java: não reconhece o locale default no windows
		Locale locale = new Locale("pt", "BR");
		Locale.setDefault(locale);

		launch(PDFSignerApp.class, args);

	}

	private PDFSignerView view;

	private SignerOptions options;

	public PDFSignerApp() throws IOException {
		super();
	}

	@Action
	public void addFiles() throws FileNotFoundException, Exception {

		FileSelectorDialog c = new FileSelectorDialog(getApplication()
				.getMainFrame(), true);

		FileFilter extf = new ExtensionFilter(new String[] { "pdf" });

		JFileChooser fileChooser = c.getJFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileFilter(extf);

		int rv = fileChooser.showOpenDialog(getApplication().getMainFrame());

		if (rv == JFileChooser.APPROVE_OPTION) {

			File[] files = fileChooser.getSelectedFiles();

			FileSelector selector = new FileSelector();
			selector.setFilter(new ExtensionFilter(new String[] { "pdf" }));
			selector.addSource(files);

			getOptions().addSelection(selector);

		}

	}

	public void attachFileDrop(Component c) {

		new FileDrop(c, FileDropListener.getInstance());

	}

	@Action
	public void config() throws IOException, URISyntaxException {

		SignerOptionDialog configDialog = view.getOptionsDialog();

		configDialog.setOptions(getOptions());

		getApplication().show(configDialog);

		setOptions(configDialog.saveOptions());

	}

	@Override
	protected void configureWindow(java.awt.Window root) {
	}

	public SignerOptions getOptions() {
		return options;
	}

	@Action
	public void getSelection() throws FileNotFoundException, Exception {

		FileSelectorDialog c = new FileSelectorDialog(getApplication()
				.getMainFrame(), true);

		JFileChooser fileChooser = c.getJFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);

		int rv = fileChooser.showOpenDialog(getApplication().getMainFrame());

		if (rv == JFileChooser.APPROVE_OPTION) {

			DirectorySelector ds = new DirectorySelector(
					fileChooser.getSelectedFile());
			// TODO: human-check
			ds.setRecursive(true);
			FileFilter extf = new ExtensionFilter(new String[] { "pdf" });
			ds.setFilter(extf);

			getOptions().addSelection(ds);

		}

	}

	public PDFSignerView getView() {
		return view;
	}

	@Override
	protected void initialize(String[] args) {
		super.initialize(args);
		try {
			setOptions(SignerOptions.newInstance());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Action
	public void previewFile() {
	}

	@Action
	public void removeSelecteds() {

		JFileTable tb = getView().getFileTable();

		tb.removeSelecteds();

		getOptions().setFiles(tb.getDataFromColumn(1));

	}

	@Action
	public void selectImageFile() throws FileNotFoundException, Exception {

		FileSelectorDialog c = new FileSelectorDialog(getApplication()
				.getMainFrame(), true);
		JFileChooser fileChooser = c.getJFileChooser();

		String[] acceptable = { "png", "jpg" };
		FileFilter ffilter = new ImageFilter(acceptable);

		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileFilter(ffilter);

		int rv = fileChooser.showOpenDialog(getApplication().getMainFrame());

		if (rv == JFileChooser.APPROVE_OPTION) {

			File imgFile = fileChooser.getSelectedFile();
			getOptions().setImage(imgFile);

		}

	}

	public void setOptions(SignerOptions options) {
		this.options = options;
	}

	public void setView(PDFSignerView view) {
		this.view = view;
	}

	@Action
	public void showDropBox() {

		DialogDrop dd = getView().getDialogDrop();

		if (dd.isVisible()) {

			dd.setVisible(false);

		} else {

			Toolkit tk = Toolkit.getDefaultToolkit();

			Dimension screen = tk.getScreenSize();

			Double pTop = 30D;

			dd.setVisible(true);

			getMainFrame().setState(Frame.ICONIFIED);

			Double pLeft = screen.getWidth() - pTop - dd.getWidth();

			dd.setLocation(pLeft.intValue(), pTop.intValue());

		}

	}

	@Override
	protected void shutdown() {

		super.shutdown();
	}

	@Action(block = Task.BlockingScope.APPLICATION)
	public Task<Object, Integer> signSelection() throws Throwable {

		class SignSelectionTask extends Task<Object, Integer> {

			SignatureAppearance signer;

			Exception ex = null;

			SignSelectionTask(Application app) {

				super(app);

				setUserCanCancel(true);

				addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {

						if (evt.getPropertyName().equals("state")) {

							if (SwingWorker.StateValue.DONE.toString() == evt
									.getNewValue().toString()) {

								if (ex != null) {

									ErrorInfo info = new ErrorInfo(
											"Erro ao assinar documento",
											"Envie o texto abaixo para https://github.com/fabianonunes/firma/issues",
											null, null, ex, ErrorLevel.SEVERE,
											null);

									while (ex.getCause() != null) {
										System.out.println(ex.getCause());
										ex = (Exception) ex.getCause();
									}

									JXErrorPane
											.showDialog(getMainFrame(), info);

								} else {

									JOptionPane
											.showMessageDialog(
													getMainFrame(),
													"Todos os documentos foram assinados.",
													"Documentos assinados",
													JOptionPane.INFORMATION_MESSAGE);

								}

							}
						}

					}
				});

				setInputBlocker(new ProgressInputBlocker(this,
						Task.BlockingScope.APPLICATION, PDFSignerApp
								.getApplication().getMainFrame(), null));

				CallbackHandler handler = new PINCallback(getMainFrame());

				try {
					signer = new SignatureAppearance(handler);
				} catch (GeneralSecurityException e) {
					ex = e;
				} catch (IOException e) {
					ex = e;
				} catch (Exception e) {
					ex = e;
				} finally {
					if (ex != null)
						return;
				}

			}

			@Override
			protected Object doInBackground() {

				if (ex != null) {
					return null;
				}

				Set<File> files;

				files = getOptions().getFiles();

				Integer total = files.size();
				Integer atual = 0, erros = 0;

				// TODO: "sobrescrever o original" não funciona
				signer.setOptions(getOptions());

				Boolean failed = false;

				for (File file : files) {

					++atual;

					// TODO: tratar cada exceção individualmente

					try {

						signer.sign(file);

						message("finishedMessage", atual, total, erros);

						publish(atual);

					} catch (Exception e) {

						ex = e;

						message("finishedMessage", atual, total, ++erros);

						failed = true;

					} finally {

						if (atual < total) {
							setProgress(atual, 1, total);
						}

						// TODO: se todos os arquivos falharem, comportamento
						// inesperado
						if (ex != null) {
							if (failed) {
								ex = new Exception(
										"Não foi possível assinar o documento "
												+ file.getName(), ex);
							}
						}

						// if (signer.getPddoc() != null) {
						//
						// try {
						// signer.getPddoc().close();
						// } catch (IOException e) {
						// e.printStackTrace();
						// }
						//
						// }

						if (signer.getOutputStream() != null) {

							IOUtils.closeQuietly(signer.getOutputStream());

							if (failed) {
								if (getOptions().getOutputOverwriteOriginal()) {
									// TODO: se o processo da assinatura falhar
									// e o usuário tiver solicitado que se
									// sobrescreva o original, o documento será
									// perdido pra sempre.
								} else {
									FileUtils.deleteQuietly(signer
											.getOutputFile());
								}
							}
						}

						failed = false;

					}

				}

				try {
					signer.logout();
				} catch (LoginException e) {
					e.printStackTrace();
				}

				return null;

			}

			@Override
			protected void failed(Throwable cause) {

				cancel(true);

				((ProgressInputBlocker) getInputBlocker()).unblock();

				super.failed(cause);

			}

			@Override
			protected void finished() {

				super.finished();

				try {

					if (signer != null) {

						if (signer.getCard() != null) {
							signer.getCard().logout();
						}

					}

				} catch (LoginException e) {
					// TODO: implementar catch.
				}

			}

			@Override
			protected void process(List<Integer> values) {
				super.process(values);
			}

		}

		SignSelectionTask task = new SignSelectionTask(
				PDFSignerApp.getInstance());

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

				setView(new PDFSignerView(getApplication()));

				show(getView());

			}

		});

	}

}
