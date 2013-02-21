/*
 * PDFSignerView.java
 */
package tc.fab.firma.app;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.jdesktop.application.Action;
import org.jdesktop.application.FrameView;

import tc.fab.file.drop.DialogDrop;
import tc.fab.file.selector.gui.JFileTable;
import tc.fab.pdf.signer.application.PDFSignerAboutBox;
import tc.fab.pdf.signer.application.PDFSignerApp;
import tc.fab.pdf.signer.options.SignerOptionDialog;
import tc.fab.pdf.signer.options.SignerOptions;

@Singleton
public class FirmaAppView extends FrameView implements AppView {

	AppContext context;

	@Inject
	public FirmaAppView(AppContext context) {

		super(context.getAppContext().getApplication());

		this.context = context;

		initComponents();

		postInitComponents();

	}

	@Action
	public void showAboutBox() {
		if (aboutBox == null) {
			JFrame mainFrame = PDFSignerApp.getApplication().getMainFrame();
			aboutBox = new PDFSignerAboutBox(mainFrame);
			aboutBox.setLocationRelativeTo(mainFrame);
		}
		PDFSignerApp.getApplication().show(aboutBox);
	}

	private void initComponents() {

		mainPanel = new javax.swing.JPanel();
		jButton2 = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();
		jButton1 = new javax.swing.JButton();
		jButton4 = new javax.swing.JButton();
		jButton5 = new javax.swing.JButton();
		jScrollPane2 = new javax.swing.JScrollPane();
		jTable1 = new JFileTable();
		jButton6 = new javax.swing.JButton();
		jButton7 = new javax.swing.JButton();
		menuBar = new javax.swing.JMenuBar();
		menuSobre = new javax.swing.JMenu();
		jMenuItem1 = new javax.swing.JMenuItem();

		mainPanel.setName("mainPanel"); // NOI18N

		javax.swing.ActionMap actionMap = org.jdesktop.application.Application
				.getInstance().getContext()
				.getActionMap(FirmaAppView.class, this);
		jButton2.setAction(actionMap.get("signSelection")); // NOI18N
		org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
				.getInstance().getContext().getResourceMap(FirmaAppView.class);
		jButton2.setIcon(resourceMap.getIcon("jButton2.icon")); // NOI18N
		jButton2.setName("jButton2"); // NOI18N

		jButton3.setAction(actionMap.get("removeSelecteds")); // NOI18N
		jButton3.setIcon(resourceMap.getIcon("jButton3.icon")); // NOI18N
		jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
		jButton3.setName("jButton3"); // NOI18N

		jButton1.setAction(actionMap.get("getSelection")); // NOI18N
		jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
		jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
		jButton1.setName("jButton1"); // NOI18N

		jButton4.setAction(actionMap.get("addFiles")); // NOI18N
		jButton4.setIcon(resourceMap.getIcon("jButton4.icon")); // NOI18N
		jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
		jButton4.setName("jButton4"); // NOI18N

		jButton5.setAction(actionMap.get("config")); // NOI18N
		jButton5.setIcon(resourceMap.getIcon("jButton5.icon")); // NOI18N
		jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
		jButton5.setName("jButton5"); // NOI18N

		jScrollPane2.setName("jScrollPane2"); // NOI18N

		jTable1.setName("jTable1"); // NOI18N
		jScrollPane2.setViewportView(jTable1);

		jButton6.setAction(actionMap.get("showDropBox")); // NOI18N
		jButton6.setIcon(resourceMap.getIcon("jButton6.icon")); // NOI18N
		jButton6.setText(resourceMap.getString("jButton6.text")); // NOI18N
		jButton6.setName("jButton6"); // NOI18N

		jButton7.setAction(actionMap.get("previewFile")); // NOI18N
		jButton7.setName("jButton7"); // NOI18N

		javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(
				mainPanel);
		mainPanel.setLayout(mainPanelLayout);
		mainPanelLayout
				.setHorizontalGroup(mainPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								mainPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												mainPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jScrollPane2,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																623,
																Short.MAX_VALUE)
														.addGroup(
																mainPanelLayout
																		.createSequentialGroup()
																		.addComponent(
																				jButton1)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jButton4)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jButton5)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																				203,
																				Short.MAX_VALUE)
																		.addComponent(
																				jButton6))
														.addGroup(
																mainPanelLayout
																		.createSequentialGroup()
																		.addComponent(
																				jButton2)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jButton7)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																				292,
																				Short.MAX_VALUE)
																		.addComponent(
																				jButton3)))
										.addContainerGap()));
		mainPanelLayout
				.setVerticalGroup(mainPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								mainPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												mainPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jButton1,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																28,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																jButton4,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																28,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																jButton5,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																28,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																jButton6,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																28,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jScrollPane2,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												421, Short.MAX_VALUE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												mainPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jButton2,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																28,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																jButton3,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																28,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																jButton7,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																28,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));

		menuBar.setName("menuBar"); // NOI18N

		menuSobre.setText(resourceMap.getString("menuSobre.text")); // NOI18N
		menuSobre.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		menuSobre.setName("menuSobre"); // NOI18N

		jMenuItem1.setAction(actionMap.get("showAboutBox")); // NOI18N
		jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
		jMenuItem1.setName("jMenuItem1"); // NOI18N
		menuSobre.add(jMenuItem1);

		menuBar.add(menuSobre);

		setComponent(mainPanel);
		setMenuBar(menuBar);
	}

	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JButton jButton5;
	private javax.swing.JButton jButton6;
	private javax.swing.JButton jButton7;
	private javax.swing.JMenuItem jMenuItem1;
	private javax.swing.JScrollPane jScrollPane2;
	private JFileTable jTable1;
	private javax.swing.JPanel mainPanel;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenu menuSobre;
	private JDialog aboutBox;
	private SignerOptionDialog optionsDialog;
	private DialogDrop dd;

	/**
	 * @return the optionsDialog
	 */
	public SignerOptionDialog getOptionsDialog() {
		if (optionsDialog == null) {
			JFrame mainFrame = PDFSignerApp.getApplication().getMainFrame();
			optionsDialog = new SignerOptionDialog(mainFrame, true);
			optionsDialog.setLocationRelativeTo(mainFrame);
		}

		return optionsDialog;
	}

	public JFileTable getFileTable() {
		return jTable1;
	}

	public void postInitComponents() {

		final PDFSignerApp app = PDFSignerApp.getApplication();

		jTable1.getTableHeader().setPreferredSize(
				new Dimension(jTable1.getTableHeader().getWidth(), 21));
		jTable1.setRowHeight(24);

		final SignerOptions options = app.getOptions();

		PDFSignerApp.getApplication().attachFileDrop(jScrollPane2);

		options.addPropertyChangeListener(SignerOptions.EVENT_FILES,
				new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						try {

							jTable1.setModel(options.getModelOfFiles());

						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}

					}
				});

	}

	public DialogDrop getDialogDrop() {

		if (dd == null) {

			dd = DialogDrop.getInstance(getFrame());

			PDFSignerApp.getApplication().attachFileDrop(dd.getDroppable());

		}

		return dd;
	}

}
