package tc.fab.firma.app.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;

import javax.inject.Inject;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdesktop.application.Action;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.JXImageView;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.app.AppDocument;
import tc.fab.pdf.signer.SignaturePreview;
import tc.fab.pdf.signer.options.AppearanceOptions;

import com.google.inject.Provider;

public class AppearanceDialog extends JDialog {

	private static final long serialVersionUID = -4115566787900700647L;

	private static final String ACTION_PREVIEW = "firma.dlg.add_appearance.preview";
	private static final String ACTION_SELECT_GRAPHIC = "firma.dlg.add_appearance.select_graphic";
	private static final String ACTION_SAVE = "firma.dlg.add_appearance.save";

	@Inject
	private Provider<FileSelectorDialog> fileDialog;

	private AppContext context;
	private AppearanceOptions options;
	private boolean save = false;

	@Inject
	public AppearanceDialog(AppContext context, AppController controller, AppDocument document) {
		super(context.getMainFrame(), true);
		this.context = context;
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		initComponents();
	}

	public void initComponents() {

		contentPanel = new JPanel();
		buttonGroup = new ButtonGroup();

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setName("firma.dlg.add_appearance.name");

		textField = new JTextField();
		textField.setPreferredSize(new Dimension(0, 24));
		textField.setColumns(10);

		JPanel panel = new JPanel();
		TitledBorder previewBorder = new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null,
			null), null, TitledBorder.LEADING, TitledBorder.TOP, null, null);
		previewBorder
			.setTitle(context.getResReader().getString("firma.msg.add_appearance.preview"));
		panel.setBorder(previewBorder);

		JPanel panel_1 = new JPanel();
		TitledBorder imageBorder = new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null,
			null), null, TitledBorder.LEADING, TitledBorder.TOP, null, null);
		imageBorder.setTitle(context.getResReader().getString("firma.msg.add_appearance.text"));
		panel_1.setBorder(imageBorder);

		JPanel panel_3 = new JPanel();
		TitledBorder textBorder = new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null,
			null), null, TitledBorder.LEADING, TitledBorder.TOP, null, null);
		textBorder.setTitle(context.getResReader().getString("firma.msg.add_appearance.image"));
		panel_3.setBorder(textBorder);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
			.addGroup(
				gl_contentPanel
					.createSequentialGroup()
					.addContainerGap()
					.addGroup(
						gl_contentPanel
							.createParallelGroup(Alignment.LEADING)
							.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 416,
								Short.MAX_VALUE)
							.addGroup(
								Alignment.TRAILING,
								gl_contentPanel
									.createSequentialGroup()
									.addComponent(lblNewLabel)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(textField, GroupLayout.DEFAULT_SIZE, 370,
										Short.MAX_VALUE))
							.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
							.addComponent(panel_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
								416, Short.MAX_VALUE)).addContainerGap()));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
			.addGroup(
				gl_contentPanel
					.createSequentialGroup()
					.addContainerGap()
					.addGroup(
						gl_contentPanel
							.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblNewLabel)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE).addContainerGap()));

		JPanel panel_2 = new JPanel();
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel_2.rowHeights = new int[] { 0 };
		gbl_panel_2.columnWeights = new double[] { 0.0, 1.0, 0.0 };
		gbl_panel_2.rowWeights = new double[] { 0.0 };
		panel_2.setLayout(gbl_panel_2);

		rdioRenderName = new JRadioButton("");
		rdioRenderName.setName("firma.dlg.add_appearance.name");
		buttonGroup.add(rdioRenderName);
		GridBagConstraints gbc_rdioRenderName = new GridBagConstraints();
		gbc_rdioRenderName.anchor = GridBagConstraints.WEST;
		gbc_rdioRenderName.insets = new Insets(0, 0, 0, 5);
		gbc_rdioRenderName.gridx = 0;
		gbc_rdioRenderName.gridy = 0;
		panel_2.add(rdioRenderName, gbc_rdioRenderName);

		rdioRenderGraphic = new JRadioButton("");
		rdioRenderGraphic.setName("firma.dlg.add_appearance.import_image");
		buttonGroup.add(rdioRenderGraphic);
		GridBagConstraints gbc_rdioRenderGraphic = new GridBagConstraints();
		gbc_rdioRenderGraphic.insets = new Insets(0, 0, 0, 5);
		gbc_rdioRenderGraphic.gridx = 1;
		gbc_rdioRenderGraphic.gridy = 0;
		panel_2.add(rdioRenderGraphic, gbc_rdioRenderGraphic);

		rdioRenderNothing = new JRadioButton("");
		rdioRenderNothing.setName("firma.dlg.add_appearance.no_image");
		buttonGroup.add(rdioRenderNothing);
		GridBagConstraints gbc_rdioRenderNothing = new GridBagConstraints();
		gbc_rdioRenderNothing.anchor = GridBagConstraints.EAST;
		gbc_rdioRenderNothing.gridx = 2;
		gbc_rdioRenderNothing.gridy = 0;
		panel_2.add(rdioRenderNothing, gbc_rdioRenderNothing);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(
			gl_panel_1.createSequentialGroup().addContainerGap()
				.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
				.addContainerGap()));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(
			gl_panel_1
				.createSequentialGroup()
				.addGap(5)
				.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
					GroupLayout.PREFERRED_SIZE)
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_1.setLayout(gl_panel_1);

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(null);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[] { 80, 0, 0 };
		gbl_panel_4.rowHeights = new int[] { 0, 0, 24, 24 };
		gbl_panel_4.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_4.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		panel_4.setLayout(gbl_panel_4);

		chkName = new JCheckBox("");
		chkName.setName("firma.dlg.add_appearance.name");
		GridBagConstraints gbc_chkName = new GridBagConstraints();
		gbc_chkName.fill = GridBagConstraints.VERTICAL;
		gbc_chkName.anchor = GridBagConstraints.WEST;
		gbc_chkName.insets = new Insets(0, 0, 5, 5);
		gbc_chkName.gridx = 0;
		gbc_chkName.gridy = 0;
		panel_4.add(chkName, gbc_chkName);

		chkDN = new JCheckBox("");
		chkDN.setName("firma.dlg.add_appearance.dn");
		GridBagConstraints gbc_chkDN = new GridBagConstraints();
		gbc_chkDN.fill = GridBagConstraints.VERTICAL;
		gbc_chkDN.anchor = GridBagConstraints.WEST;
		gbc_chkDN.insets = new Insets(0, 0, 5, 0);
		gbc_chkDN.gridx = 1;
		gbc_chkDN.gridy = 0;
		panel_4.add(chkDN, gbc_chkDN);

		chkDate = new JCheckBox("");
		chkDate.setName("firma.dlg.add_appearance.date");
		GridBagConstraints gbc_chkDate = new GridBagConstraints();
		gbc_chkDate.fill = GridBagConstraints.VERTICAL;
		gbc_chkDate.anchor = GridBagConstraints.WEST;
		gbc_chkDate.insets = new Insets(0, 0, 5, 5);
		gbc_chkDate.gridx = 0;
		gbc_chkDate.gridy = 1;
		panel_4.add(chkDate, gbc_chkDate);

		chkLabels = new JCheckBox("");
		chkLabels.setName("firma.dlg.add_appearance.labels");
		GridBagConstraints gbc_chkLabels = new GridBagConstraints();
		gbc_chkLabels.fill = GridBagConstraints.VERTICAL;
		gbc_chkLabels.anchor = GridBagConstraints.WEST;
		gbc_chkLabels.insets = new Insets(0, 0, 5, 0);
		gbc_chkLabels.gridx = 1;
		gbc_chkLabels.gridy = 1;
		panel_4.add(chkLabels, gbc_chkLabels);

		chkReason = new JCheckBox("");
		chkReason.setName("firma.dlg.add_appearance.reason");
		chkReason.setVerticalAlignment(SwingConstants.BOTTOM);
		GridBagConstraints gbc_chkReason = new GridBagConstraints();
		gbc_chkReason.fill = GridBagConstraints.VERTICAL;
		gbc_chkReason.anchor = GridBagConstraints.WEST;
		gbc_chkReason.insets = new Insets(0, 0, 5, 5);
		gbc_chkReason.gridx = 0;
		gbc_chkReason.gridy = 2;
		panel_4.add(chkReason, gbc_chkReason);

		txtReason = new JTextField();
		GridBagConstraints gbc_txtReason = new GridBagConstraints();
		gbc_txtReason.insets = new Insets(0, 0, 5, 0);
		gbc_txtReason.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtReason.gridx = 1;
		gbc_txtReason.gridy = 2;
		panel_4.add(txtReason, gbc_txtReason);
		txtReason.setColumns(10);

		chkLocation = new JCheckBox("");
		chkLocation.setName("firma.dlg.add_appearance.local");
		GridBagConstraints gbc_chkLocation = new GridBagConstraints();
		gbc_chkLocation.fill = GridBagConstraints.VERTICAL;
		gbc_chkLocation.anchor = GridBagConstraints.WEST;
		gbc_chkLocation.insets = new Insets(0, 0, 0, 5);
		gbc_chkLocation.gridx = 0;
		gbc_chkLocation.gridy = 3;
		panel_4.add(chkLocation, gbc_chkLocation);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(
			gl_panel_3.createSequentialGroup().addContainerGap()
				.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
				.addContainerGap()));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(
			gl_panel_3.createSequentialGroup().addContainerGap()
				.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		txtLocation = new JTextField();
		GridBagConstraints gbc_txtLocation = new GridBagConstraints();
		gbc_txtLocation.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLocation.gridx = 1;
		gbc_txtLocation.gridy = 3;
		panel_4.add(txtLocation, gbc_txtLocation);
		txtLocation.setColumns(10);
		panel_3.setLayout(gl_panel_3);

		imageView = new JXImageView();
		imageView.setEditable(false);
		imageView.setDragEnabled(false);
		imageView.setBorder(new LineBorder(Color.LIGHT_GRAY));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(
			gl_panel.createSequentialGroup().addContainerGap()
				.addComponent(imageView, GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
				.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(
			Alignment.TRAILING,
			gl_panel.createSequentialGroup().addContainerGap()
				.addComponent(imageView, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
				.addContainerGap()));
		panel.setLayout(gl_panel);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setPreferredSize(new Dimension(10, 50));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.RIGHT, 10, 10);
			fl_buttonPane.setAlignOnBaseline(true);
			buttonPane.setLayout(fl_buttonPane);
			{
				btOk = new JButton("OK");
				btOk.setPreferredSize(new Dimension(80, 27));
				btOk.setAction(context.getAction(this, ACTION_SAVE));
				buttonPane.add(btOk);
				getRootPane().setDefaultButton(btOk);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setPreferredSize(new Dimension(80, 27));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		setName("firma.dlg.add_appearance");
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setBounds(100, 100, 450, 471);
		context.getResourceMap().injectComponents(this);

	}

	public AppearanceOptions open(AppearanceOptions options) {

		this.options = options;

		if (options.getName() == null || options.getName().length() < 1) {
			options.setName("Criado em "
				+ DateFormat.getDateInstance(DateFormat.MEDIUM).format(
					Calendar.getInstance().getTime()));
		}

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				super.windowOpened(e);
				context.fireAction(AppearanceDialog.this, ACTION_PREVIEW);
			}
		});

		setLocationRelativeTo(this.context.getMainFrame());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		initDataBindings();
		attachPreviewEvent();
		setVisible(true);
		return save ? this.options : null;
	}

	private void attachPreviewEvent() {

		final javax.swing.Action action = context.getAction(this, ACTION_PREVIEW);
		javax.swing.Action actionSelect = context.getAction(this, ACTION_SELECT_GRAPHIC);

		chkName.addActionListener(action);
		chkDate.addActionListener(action);
		chkLabels.addActionListener(action);
		chkReason.addActionListener(action);
		chkLocation.addActionListener(action);
		chkDN.addActionListener(action);
		rdioRenderName.addActionListener(action);
		rdioRenderGraphic.addActionListener(actionSelect);
		rdioRenderNothing.addActionListener(action);

		FocusListener blurEvent = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				context.fireAction(action);
			}
		};

		txtReason.addFocusListener(blurEvent);
		txtLocation.addFocusListener(blurEvent);

	}

	@Action(name = ACTION_PREVIEW)
	public void preview() {
		imageView.setImage(SignaturePreview.generate(null, imageView.getSize(), options));
	}

	@Action(name = ACTION_SAVE)
	public void save() {
		this.save = true;
		dispose();
	}

	@Action(name = ACTION_SELECT_GRAPHIC)
	public void selectGraphic() {

		FileNameExtensionFilter filter = new FileNameExtensionFilter(context.getResReader()
			.getString("firma.msg.images"), "gif", "png", "jpg", "bmp");

		File image = fileDialog.get().selectFile(filter);

		if (image != null) {
			options.setRenderGraphic(true);
			options.setGraphic(image.getAbsolutePath());
		} else {
			rdioRenderName.setSelected(true);
		}

		preview();

	}

	protected void initDataBindings() {

		BeanProperty<JCheckBox, Boolean> onChkSelecte = BeanProperty.create("selected");
		BeanProperty<JRadioButton, Boolean> onRdoSelect = BeanProperty.create("selected");
		BeanProperty<JTextField, String> onBlur = BeanProperty.create("text_ON_FOCUS_LOST");

		BeanProperty<AppearanceOptions, Boolean> showReasonOption = BeanProperty
			.create("showReason");
		AutoBinding<AppearanceOptions, Boolean, JCheckBox, Boolean> showReasonBinding = Bindings
			.createAutoBinding(UpdateStrategy.READ_WRITE, options, showReasonOption, chkReason,
				onChkSelecte, "showReason");
		showReasonBinding.bind();
		// //
		BeanProperty<AppearanceOptions, Boolean> showNameOption = BeanProperty.create("showName");
		AutoBinding<AppearanceOptions, Boolean, JCheckBox, Boolean> showNameBinding = Bindings
			.createAutoBinding(UpdateStrategy.READ_WRITE, options, showNameOption, chkName,
				onChkSelecte, "showName");
		showNameBinding.bind();
		// //
		BeanProperty<AppearanceOptions, Boolean> showDateOption = BeanProperty.create("showDate");
		AutoBinding<AppearanceOptions, Boolean, JCheckBox, Boolean> showDateBinding = Bindings
			.createAutoBinding(UpdateStrategy.READ_WRITE, options, showDateOption, chkDate,
				onChkSelecte, "showDate");
		showDateBinding.bind();
		// //
		BeanProperty<AppearanceOptions, Boolean> showLabelsOption = BeanProperty
			.create("showLabels");
		AutoBinding<AppearanceOptions, Boolean, JCheckBox, Boolean> showLabelsBinding = Bindings
			.createAutoBinding(UpdateStrategy.READ_WRITE, options, showLabelsOption, chkLabels,
				onChkSelecte, "showLabels");
		showLabelsBinding.bind();
		// //
		BeanProperty<AppearanceOptions, Boolean> showLocationOption = BeanProperty
			.create("showLocation");
		AutoBinding<AppearanceOptions, Boolean, JCheckBox, Boolean> showLocalBinding = Bindings
			.createAutoBinding(UpdateStrategy.READ_WRITE, options, showLocationOption, chkLocation,
				onChkSelecte, "showLocation");
		showLocalBinding.bind();
		// //
		BeanProperty<AppearanceOptions, String> reasonOption = BeanProperty.create("reason");
		AutoBinding<AppearanceOptions, String, JTextField, String> reasonBinding = Bindings
			.createAutoBinding(UpdateStrategy.READ_WRITE, options, reasonOption, txtReason, onBlur,
				"reason");
		reasonBinding.bind();
		// //
		BeanProperty<AppearanceOptions, String> locationOption = BeanProperty.create("location");
		AutoBinding<AppearanceOptions, String, JTextField, String> locationBinding = Bindings
			.createAutoBinding(UpdateStrategy.READ_WRITE, options, locationOption, txtLocation,
				onBlur, "location");
		locationBinding.bind();
		// //
		BeanProperty<AppearanceOptions, String> nameOption = BeanProperty.create("name");
		AutoBinding<AppearanceOptions, String, JTextField, String> nameBinding = Bindings
			.createAutoBinding(UpdateStrategy.READ_WRITE, options, nameOption, textField, onBlur,
				"name");
		nameBinding.bind();
		// //
		BeanProperty<AppearanceOptions, Boolean> renderNameOption = BeanProperty
			.create("renderName");
		AutoBinding<AppearanceOptions, Boolean, JRadioButton, Boolean> renderNameBinding = Bindings
			.createAutoBinding(UpdateStrategy.READ_WRITE, options, renderNameOption,
				rdioRenderName, onRdoSelect, "renderName");
		renderNameBinding.bind();
		// //
		BeanProperty<AppearanceOptions, Boolean> renderGraphicOption = BeanProperty
			.create("renderGraphic");
		AutoBinding<AppearanceOptions, Boolean, JRadioButton, Boolean> renderGraphicBinding = Bindings
			.createAutoBinding(UpdateStrategy.READ_WRITE, options, renderGraphicOption,
				rdioRenderGraphic, onRdoSelect, "renderGraphic");
		renderGraphicBinding.bind();
		//
		BeanProperty<AppearanceOptions, Boolean> showDnOption = BeanProperty.create("showDN");
		AutoBinding<AppearanceOptions, Boolean, JCheckBox, Boolean> showDnBinding = Bindings
			.createAutoBinding(UpdateStrategy.READ_WRITE, options, showDnOption, chkDN,
				onChkSelecte);
		showDnBinding.bind();
	}

	public AppearanceOptions getOptions() {
		return options;
	}

	private JPanel contentPanel;
	private JTextField textField;
	private ButtonGroup buttonGroup;
	private JTextField txtReason;
	private JTextField txtLocation;
	private JCheckBox chkName;
	private JCheckBox chkDate;
	private JCheckBox chkLabels;
	private JCheckBox chkReason;
	private JCheckBox chkLocation;
	private JCheckBox chkDN;
	private JRadioButton rdioRenderName;
	private JRadioButton rdioRenderGraphic;
	private JRadioButton rdioRenderNothing;
	private JXImageView imageView;
	private JButton btOk;
}
