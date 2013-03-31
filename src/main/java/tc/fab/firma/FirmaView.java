/*
 * PDFSignerView.java
 */
package tc.fab.firma;

import java.awt.Image;
import java.io.File;
import java.util.Arrays;
import java.util.Vector;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.jdesktop.application.FrameView;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.app.AppView;
import tc.fab.firma.app.components.JFileTable;
import tc.fab.firma.app.components.JFileTable.FileModel;
import tc.fab.firma.utils.FileDrop;
import tc.fab.firma.utils.FileDrop.Listener;

@Singleton
public class FirmaView extends FrameView implements AppView {

	@SuppressWarnings("unused")
	private AppContext context;
	private AppController controller;
	private Vector<FileModel> model;

	@Inject
	public FirmaView(AppContext context, AppController controller) {

		super(context.getAppContext().getApplication());

		this.context = context;
		this.controller = controller;

		model = new Vector<>();

	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void initView() {
		initComponents();
		postInitComponents();
	}

	private void initComponents() {

		setStatusBar(new JPanel());

		ActionMap actionMap = controller.getActionMap();

		JPanel mainPanel = new JPanel();
		JButton signFiles = new JButton();
		JButton removeFile = new JButton();
		scrollPane = new JScrollPane();
		fileTable = new JFileTable(model);
		JButton showDropArea = new JButton();
		showDropArea.setAction(actionMap.get(AppController.ACTION_DROPAREA_SHOW));
		signFiles.setAction(actionMap.get(AppController.ACTION_FILES_SIGN));
		removeFile.setAction(actionMap.get(AppController.ACTION_FILES_REMOVE));

		scrollPane.setViewportView(fileTable);

		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(
			gl_mainPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
						.addGroup(gl_mainPanel.createSequentialGroup()
							.addComponent(signFiles)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(showDropArea)
							.addPreferredGap(ComponentPlacement.RELATED, 318, Short.MAX_VALUE)
							.addComponent(removeFile)))
					.addContainerGap())
		);
		gl_mainPanel.setVerticalGroup(
			gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(removeFile, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(signFiles, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(showDropArea, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		mainPanel.setLayout(gl_mainPanel);

		setComponent(mainPanel);
	}

	private JScrollPane scrollPane;
	private JFileTable fileTable;

	private void setupWindowIcons() {

		getFrame().setIconImages(
			Arrays.asList(loadImage("/icons/app-96.png"), loadImage("/icons/app-64.png"),
				loadImage("/icons/app-48.png"), loadImage("/icons/app-32.png"),
				loadImage("/icons/app-24.png"), loadImage("/icons/app-22.png"),
				loadImage("/icons/app-16.png")));

	}

	private Image loadImage(String filename) {
		return new ImageIcon(FirmaView.class.getResource(filename)).getImage();
	}

	public void postInitComponents() {

		setupWindowIcons();

		new FileDrop(scrollPane, new Listener() {
			@Override
			public void filesDropped(File[] files) {
				for (File file : files) {
					String mimeType;
					try {
						mimeType = Magic.getMagicMatch(file, true).getMimeType();
						if (mimeType.equals("application/pdf")) {
							FileModel row = new FileModel(FileModel.Status.IDLE, file,
								file.length());
							model.add(row);
						}
					} catch (MagicParseException | MagicMatchNotFoundException | MagicException e) {
					}
				}
				fileTable.updateView();
			}
		});

	}

	@Override
	public JComponent getStatusBar() {
		return super.getStatusBar();
	}

	@Override
	public JFileTable getFileTable() {
		return fileTable;
	}
}
