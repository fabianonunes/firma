/*
 * PDFSignerView.java
 */
package tc.fab.firma;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import org.jdesktop.observablecollections.ObservableCollections;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.app.AppView;
import tc.fab.firma.FirmaController.SignTask;
import tc.fab.firma.app.components.FileModel;
import tc.fab.firma.app.components.JFileTable;
import tc.fab.firma.utils.FileDrop;
import tc.fab.firma.utils.FileDrop.Listener;

@Singleton
public class FirmaView extends FrameView implements AppView {

	private AppContext context;
	private AppController controller;
	private List<FileModel> model;
	
	ImageIcon doneIcon = new ImageIcon(FirmaView.class.getResource("/icons/bullet_green.png"));
	ImageIcon failedIcon = new ImageIcon(FirmaView.class.getResource("/icons/bullet_error.png"));


	@Inject
	public FirmaView(AppContext context, AppController controller) {

		super(context.getAppContext().getApplication());

		this.context = context;
		this.controller = controller;

		model = ObservableCollections.observableList(new Vector<FileModel>());

	}

	@Override
	public void initView() {
		initComponents();
		setupTaskMonitor();
		postInitComponents();
	}

	private void setupTaskMonitor() {

		progressBar.setVisible(false);

		TaskMonitor taskMonitor = context.getAppContext().getTaskMonitor();
		
		taskMonitor.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				
				if (!(evt.getSource() instanceof SignTask)) {
					return;
				}

				String propertyName = evt.getPropertyName();
				if ("started".equals(propertyName)) {
					// TODO: start-icon
					progressBar.setVisible(true);
					progressBar.setIndeterminate(true);
					statusMessageLabel.setText("");
					statusLabel.setIcon(null);
				} else if ("done".equals(propertyName)) {
					// TODO: stop-icon
					progressBar.setVisible(false);
					progressBar.setValue(0);
				} else if ("message".equals(propertyName)) {
					String text = (String) (evt.getNewValue());
					statusMessageLabel.setText((text == null) ? "" : text);
				} else if ("progress".equals(propertyName)) {
					int value = (Integer) (evt.getNewValue());
					progressBar.setVisible(true);
					progressBar.setIndeterminate(false);
					progressBar.setValue(value);
				} else if ("description".equals(propertyName)) {
					String text = (String) (evt.getNewValue());
					statusLabel.setIcon(text.equals("0") ? doneIcon : failedIcon);
				}
			}
		});

	}

	/**
	 * @wbp.parser.entryPoint
	 */
	private void initComponents() {

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

		JPanel panel = new JPanel();

		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(gl_mainPanel.createParallelGroup(Alignment.TRAILING)
			.addGroup(
				gl_mainPanel
					.createSequentialGroup()
					.addContainerGap()
					.addGroup(
						gl_mainPanel
							.createParallelGroup(Alignment.TRAILING)
							.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
								426, Short.MAX_VALUE)
							.addGroup(
								gl_mainPanel
									.createSequentialGroup()
									.addComponent(signFiles)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(showDropArea)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(panel, GroupLayout.DEFAULT_SIZE, 294,
										Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(removeFile))).addContainerGap()));
		gl_mainPanel.setVerticalGroup(gl_mainPanel.createParallelGroup(Alignment.TRAILING)
			.addGroup(
				gl_mainPanel
					.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(
						gl_mainPanel
							.createParallelGroup(Alignment.LEADING)
							.addGroup(
								gl_mainPanel
									.createParallelGroup(Alignment.TRAILING)
									.addComponent(removeFile, GroupLayout.PREFERRED_SIZE, 28,
										GroupLayout.PREFERRED_SIZE)
									.addComponent(signFiles, GroupLayout.PREFERRED_SIZE, 28,
										GroupLayout.PREFERRED_SIZE)
									.addComponent(showDropArea, GroupLayout.PREFERRED_SIZE, 28,
										GroupLayout.PREFERRED_SIZE))
							.addComponent(panel, GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
					.addContainerGap()));
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {20, 40, 0};
		gbl_panel.rowHeights = new int[] { 14, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 1.0 };
		gbl_panel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);
		
		statusLabel = new JLabel("");
		GridBagConstraints gbc_statusLabel = new GridBagConstraints();
		gbc_statusLabel.insets = new Insets(0, 0, 0, 5);
		gbc_statusLabel.gridx = 0;
		gbc_statusLabel.gridy = 0;
		panel.add(statusLabel, gbc_statusLabel);

		statusMessageLabel = new JLabel((String) null);
		statusMessageLabel.setName("firma.dlg.appearances.status_message");
		statusMessageLabel.setForeground(Color.GRAY);
		GridBagConstraints gbc_statusMessageLabel = new GridBagConstraints();
		gbc_statusMessageLabel.insets = new Insets(0, 0, 0, 5);
		gbc_statusMessageLabel.gridx = 1;
		gbc_statusMessageLabel.gridy = 0;
		panel.add(statusMessageLabel, gbc_statusMessageLabel);

		progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridx = 2;
		gbc_progressBar.gridy = 0;
		panel.add(progressBar, gbc_progressBar);
		mainPanel.setLayout(gl_mainPanel);

		setComponent(mainPanel);
	}

	private JScrollPane scrollPane;
	private JFileTable fileTable;
	private JProgressBar progressBar;
	private JLabel statusMessageLabel;
	private JLabel statusLabel;

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
							fileTable.getData().add(row);
						}
					} catch (MagicParseException | MagicMatchNotFoundException | MagicException e) {
					}
				}
			}
		});

	}

	@Override
	public JFileTable getFileTable() {
		return fileTable;
	}
}
