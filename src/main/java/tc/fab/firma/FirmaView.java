/*
 * PDFSignerView.java
 */
package tc.fab.firma;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

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
		JButton showDropArea = new JButton();
		
		showDropArea.setAction(actionMap.get(AppController.ACTION_DROPAREA_SHOW));
		signFiles.setAction(actionMap.get(AppController.ACTION_FILES_SIGN));
		removeFile.setAction(actionMap.get(AppController.ACTION_FILES_REMOVE));

		JPanel messagePanel = new JPanel();
		
		dropPanel = new JPanel();
		dropPanel.setBorder(BorderFactory.createEmptyBorder());
		dropPanel.setMinimumSize(new Dimension(550, 300));

		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(
			gl_mainPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(dropPanel, GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
						.addGroup(gl_mainPanel.createSequentialGroup()
							.addComponent(signFiles, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(showDropArea, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(messagePanel, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(removeFile, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_mainPanel.setVerticalGroup(
			gl_mainPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(dropPanel, GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(messagePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(signFiles, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(removeFile, GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
						.addComponent(showDropArea, GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
					.addContainerGap())
		);
		dropPanel.setLayout(new CardLayout(0, 0));
		scrollPane = new JScrollPane();
		scrollPane.setVisible(false);
		
		Border dropBorder = BorderFactory.createLineBorder(Color.WHITE, 2);
		iconPanel = new JPanel();
		iconPanel.setBorder(dropBorder);
		iconPanel.setBackground(new Color(200, 200, 200));
		
		dropPanel.add(iconPanel, "name_41064197351004");
		
		lblDropHere = new JLabel("Arraste os documentos aqui");
		lblDropHere.setVerticalAlignment(SwingConstants.TOP);
		lblDropHere.setAlignmentY(20.0f);
		lblDropHere.setFont(new Font(Font.SERIF, Font.ITALIC, 22));
		lblDropHere.setForeground(new Color(255, 255, 255));
		lblDropHere.setHorizontalAlignment(SwingConstants.CENTER);
		iconPanel.setLayout(new GridLayout(2, 1, 0, 16));
		
		lblDropIcon = new JLabel("");
		lblDropIcon.setName("firma.view.drop_icon");
		lblDropIcon.setVerticalAlignment(SwingConstants.BOTTOM);
		lblDropIcon.setHorizontalAlignment(SwingConstants.CENTER);
		iconPanel.add(lblDropIcon);
		iconPanel.add(lblDropHere);
		dropPanel.add(scrollPane, "name_41052103490079");
		fileTable = new JFileTable(model);
		
				scrollPane.setViewportView(fileTable);
		GridBagLayout gbl_messagePanel = new GridBagLayout();
		gbl_messagePanel.columnWidths = new int[] {20, 40, 0};
		gbl_messagePanel.rowHeights = new int[] { 14, 0 };
		gbl_messagePanel.columnWeights = new double[] { 0.0, 0.0, 1.0 };
		gbl_messagePanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		messagePanel.setLayout(gbl_messagePanel);
		
		statusLabel = new JLabel("");
		GridBagConstraints gbc_statusLabel = new GridBagConstraints();
		gbc_statusLabel.insets = new Insets(0, 0, 0, 5);
		gbc_statusLabel.gridx = 0;
		gbc_statusLabel.gridy = 0;
		messagePanel.add(statusLabel, gbc_statusLabel);

		statusMessageLabel = new JLabel((String) null);
		statusMessageLabel.setName("firma.dlg.appearances.status_message");
		statusMessageLabel.setForeground(Color.GRAY);
		GridBagConstraints gbc_statusMessageLabel = new GridBagConstraints();
		gbc_statusMessageLabel.insets = new Insets(0, 0, 0, 5);
		gbc_statusMessageLabel.gridx = 1;
		gbc_statusMessageLabel.gridy = 0;
		messagePanel.add(statusMessageLabel, gbc_statusMessageLabel);

		progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridx = 2;
		gbc_progressBar.gridy = 0;
		messagePanel.add(progressBar, gbc_progressBar);
		mainPanel.setLayout(gl_mainPanel);

		setComponent(mainPanel);
	}

	private JScrollPane scrollPane;
	private JFileTable fileTable;
	private JProgressBar progressBar;
	private JLabel statusMessageLabel;
	private JLabel statusLabel;
	private JPanel dropPanel;
	private JPanel iconPanel;
	private JLabel lblDropHere;
	private JLabel lblDropIcon;

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

		new FileDrop(dropPanel, new Listener() {
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
						
						CardLayout layout = (CardLayout) dropPanel.getLayout();
						layout.last(dropPanel);
						
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
