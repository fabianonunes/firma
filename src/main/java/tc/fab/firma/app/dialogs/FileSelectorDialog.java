package tc.fab.firma.app.dialogs;

import java.io.File;

import javax.inject.Inject;
import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import tc.fab.app.AppContext;

public class FileSelectorDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private AppContext context;

	@Inject
	public FileSelectorDialog(AppContext context) {

		super(context.getMainFrame(), true);

		this.context = context;

		initComponents();
		setLocationRelativeTo(context.getMainFrame());
	}

	private void initComponents() {

		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		javax.swing.GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addComponent(fileChooser,
			javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			layout
				.createSequentialGroup()
				.addComponent(fileChooser, javax.swing.GroupLayout.PREFERRED_SIZE,
					javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pack();
	}

	private JFileChooser fileChooser;

	public File selectFile(FileFilter filter) {

		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileFilter(filter);

		int rv = fileChooser.showOpenDialog(context.getMainFrame());

		if (rv == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}

		return null;

	}
	public File[] selectFiles(FileFilter filter) {

		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileFilter(filter);

		int rv = fileChooser.showOpenDialog(context.getMainFrame());

		if (rv == JFileChooser.APPROVE_OPTION) {
			System.out.println("asfd");
			return fileChooser.getSelectedFiles();
		}

		return null;

	}

}
