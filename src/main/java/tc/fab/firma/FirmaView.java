/*
 * PDFSignerView.java
 */
package tc.fab.firma;

import java.awt.Dimension;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;

import org.jdesktop.application.FrameView;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.app.AppView;
import tc.fab.file.drop.DialogDrop;
import tc.fab.firma.app.components.JFileTable;
import tc.fab.pdf.signer.options.SignerOptionDialog;

@Singleton
public class FirmaView extends FrameView implements AppView {

	private AppContext context;

	private AppController controller;

	@Inject
	public FirmaView(AppContext context, AppController controller) {

		super(context.getAppContext().getApplication());

		this.context = context;
		this.controller = controller;

	}

	@Override
	public void initView() {
		initComponents();
		postInitComponents();
	}

	private void initComponents() {

		ActionMap actionMap = controller.getActionMap();

		mainPanel = new JPanel();
		signFiles = new JButton();
		removeFile = new JButton();
		addFolder = new JButton();
		addFiles = new JButton();
		settings = new JButton();
		scrollPane = new JScrollPane();
		fileTable = new JFileTable();
		showDropArea = new JButton();
		previewFile = new JButton();

		addFiles.setAction(actionMap.get(AppController.ACTION_FILES_ADD));
		addFolder.setAction(actionMap.get(AppController.ACTION_FOLDER_ADD));
		settings.setAction(actionMap.get(AppController.ACTION_SETTINGS));
		showDropArea.setAction(actionMap.get(AppController.ACTION_DROPAREA_SHOW));
		signFiles.setAction(actionMap.get(AppController.ACTION_FILES_SIGN));
		removeFile.setAction(actionMap.get(AppController.ACTION_FILES_REMOVE));
		previewFile.setAction(actionMap.get(AppController.ACTION_FILE_PREVIEW));

		scrollPane.setViewportView(fileTable);

		GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
		mainPanel.setLayout(mainPanelLayout);
		mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(
			GroupLayout.Alignment.LEADING).addGroup(
			mainPanelLayout
				.createSequentialGroup()
				.addContainerGap()
				.addGroup(
					mainPanelLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
						.addGroup(
							mainPanelLayout
								.createSequentialGroup()
								.addComponent(addFolder)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(addFiles)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(settings)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 203,
									Short.MAX_VALUE).addComponent(showDropArea))
						.addGroup(
							mainPanelLayout
								.createSequentialGroup()
								.addComponent(signFiles)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(previewFile)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 292,
									Short.MAX_VALUE).addComponent(removeFile))).addContainerGap()));
		mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(
			GroupLayout.Alignment.LEADING).addGroup(
			mainPanelLayout
				.createSequentialGroup()
				.addContainerGap()
				.addGroup(
					mainPanelLayout
						.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(addFolder, GroupLayout.PREFERRED_SIZE, 28,
							GroupLayout.PREFERRED_SIZE)
						.addComponent(addFiles, GroupLayout.PREFERRED_SIZE, 28,
							GroupLayout.PREFERRED_SIZE)
						.addComponent(settings, GroupLayout.PREFERRED_SIZE, 28,
							GroupLayout.PREFERRED_SIZE)
						.addComponent(showDropArea, GroupLayout.PREFERRED_SIZE, 28,
							GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(
					mainPanelLayout
						.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(signFiles, GroupLayout.PREFERRED_SIZE, 28,
							GroupLayout.PREFERRED_SIZE)
						.addComponent(removeFile, GroupLayout.PREFERRED_SIZE, 28,
							GroupLayout.PREFERRED_SIZE)
						.addComponent(previewFile, GroupLayout.PREFERRED_SIZE, 28,
							GroupLayout.PREFERRED_SIZE)).addContainerGap()));

		setComponent(mainPanel);
	}

	private JButton addFolder;
	private JButton signFiles;
	private JButton removeFile;
	private JButton addFiles;
	private JButton settings;
	private JButton showDropArea;
	private JButton previewFile;
	private JScrollPane scrollPane;
	private JFileTable fileTable;
	private JPanel mainPanel;
	private SignerOptionDialog optionsDialog;
	private DialogDrop dd;

	public void postInitComponents() {

		fileTable.getTableHeader().setPreferredSize(
			new Dimension(fileTable.getTableHeader().getWidth(), 21));
		fileTable.setRowHeight(24);

	}

}
