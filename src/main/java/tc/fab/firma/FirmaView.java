/*
 * PDFSignerView.java
 */
package tc.fab.firma;

import java.awt.Dimension;
import java.awt.Image;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;

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

	/**
	 * @wbp.parser.entryPoint
	 */
	private JRootPane getMain () {
	        JFrame frame;
	            frame = new JFrame("oi");
	            frame.setName(MAIN_FRAME_NAME);
		initComponents();
		postInitComponents();
		return getFrame().getRootPane();
		
	}
//	
//	public static void main(String[] args) {
//		new FirmaView(null, null);
//	}
//	
	@Override
	public void initView() {
		initComponents();
		postInitComponents();
		
	}


	private void initComponents() {
		
		setStatusBar(new JPanel());


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
		mainPanelLayout.setHorizontalGroup(
			mainPanelLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(mainPanelLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, mainPanelLayout.createSequentialGroup()
							.addComponent(addFolder)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(addFiles)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(settings)
							.addPreferredGap(ComponentPlacement.RELATED, 278, Short.MAX_VALUE)
							.addComponent(showDropArea))
						.addGroup(Alignment.LEADING, mainPanelLayout.createSequentialGroup()
							.addComponent(signFiles)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(previewFile)
							.addPreferredGap(ComponentPlacement.RELATED, 318, Short.MAX_VALUE)
							.addComponent(removeFile)))
					.addContainerGap())
		);
		mainPanelLayout.setVerticalGroup(
			mainPanelLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(mainPanelLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(mainPanelLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(addFolder, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(addFiles, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(settings, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(showDropArea, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(mainPanelLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(previewFile, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(removeFile, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(signFiles, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		mainPanel.setLayout(mainPanelLayout);

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

	private void setupWindowIcons() {
		
        getFrame().setIconImages(Arrays.asList(
            loadImage("/icons/app-96.png"),
            loadImage("/icons/app-64.png"),
            loadImage("/icons/app-48.png"),
            loadImage("/icons/app-32.png"),
            loadImage("/icons/app-24.png"),
            loadImage("/icons/app-22.png"),
            loadImage("/icons/app-16.png")
        ));
        
    }   
	
    private Image loadImage(String filename) {
        return new ImageIcon(FirmaView.class.getResource(filename)).getImage();
    }
    
	public void postInitComponents() {
		
		setupWindowIcons();

		fileTable.getTableHeader().setPreferredSize(
			new Dimension(fileTable.getTableHeader().getWidth(), 21));
		fileTable.setRowHeight(24);
		

	}
	
	@Override
	public JComponent getStatusBar() {
		// TODO Auto-generated method stub
		return super.getStatusBar();
	}
}
