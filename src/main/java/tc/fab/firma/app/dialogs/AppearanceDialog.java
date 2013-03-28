package tc.fab.firma.app.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.JXImageView;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.app.AppDocument;
import tc.fab.pdf.signer.options.AppearanceOptions;

public class AppearanceDialog extends JDialog {

	private static final long serialVersionUID = -4115566787900700647L;

	private AppContext context;

	@Inject
	public AppearanceDialog(AppContext context, AppController controller, AppDocument document) {
		super(context.getMainFrame(), true);
		this.context = context;
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		initComponents();
	}

	private AppearanceOptions options;

	public void initComponents() {

		contentPanel = new JPanel();
		buttonGroup = new ButtonGroup();

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JLabel lblNewLabel = new JLabel("Nome");

		textField = new JTextField();
		textField.setPreferredSize(new Dimension(0, 24));
		textField.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),
			"Visualiza\u00E7\u00E3o", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),
			"Imagem", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),
			"Texto", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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

		rdioRenderName = new JRadioButton("Nome");
		buttonGroup.add(rdioRenderName);
		GridBagConstraints gbc_rdioRenderName = new GridBagConstraints();
		gbc_rdioRenderName.anchor = GridBagConstraints.WEST;
		gbc_rdioRenderName.insets = new Insets(0, 0, 0, 5);
		gbc_rdioRenderName.gridx = 0;
		gbc_rdioRenderName.gridy = 0;
		panel_2.add(rdioRenderName, gbc_rdioRenderName);

		rdioRenderGraphic = new JRadioButton("Importar imagem");
		buttonGroup.add(rdioRenderGraphic);
		GridBagConstraints gbc_rdioRenderGraphic = new GridBagConstraints();
		gbc_rdioRenderGraphic.insets = new Insets(0, 0, 0, 5);
		gbc_rdioRenderGraphic.gridx = 1;
		gbc_rdioRenderGraphic.gridy = 0;
		panel_2.add(rdioRenderGraphic, gbc_rdioRenderGraphic);

		JRadioButton rdioRenderNothing = new JRadioButton("Sem imagem");
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

		chkName = new JCheckBox("Nome");
		GridBagConstraints gbc_chkName = new GridBagConstraints();
		gbc_chkName.fill = GridBagConstraints.VERTICAL;
		gbc_chkName.anchor = GridBagConstraints.WEST;
		gbc_chkName.insets = new Insets(0, 0, 5, 5);
		gbc_chkName.gridx = 0;
		gbc_chkName.gridy = 0;
		panel_4.add(chkName, gbc_chkName);

		JCheckBox chkDN = new JCheckBox("Nome distinto");
		GridBagConstraints gbc_chkDN = new GridBagConstraints();
		gbc_chkDN.fill = GridBagConstraints.VERTICAL;
		gbc_chkDN.anchor = GridBagConstraints.WEST;
		gbc_chkDN.insets = new Insets(0, 0, 5, 0);
		gbc_chkDN.gridx = 1;
		gbc_chkDN.gridy = 0;
		panel_4.add(chkDN, gbc_chkDN);

		chkDate = new JCheckBox("Data");
		GridBagConstraints gbc_chkDate = new GridBagConstraints();
		gbc_chkDate.fill = GridBagConstraints.VERTICAL;
		gbc_chkDate.anchor = GridBagConstraints.WEST;
		gbc_chkDate.insets = new Insets(0, 0, 5, 5);
		gbc_chkDate.gridx = 0;
		gbc_chkDate.gridy = 1;
		panel_4.add(chkDate, gbc_chkDate);

		chkLabels = new JCheckBox("Etiquetas");
		GridBagConstraints gbc_chkLabels = new GridBagConstraints();
		gbc_chkLabels.fill = GridBagConstraints.VERTICAL;
		gbc_chkLabels.anchor = GridBagConstraints.WEST;
		gbc_chkLabels.insets = new Insets(0, 0, 5, 0);
		gbc_chkLabels.gridx = 1;
		gbc_chkLabels.gridy = 1;
		panel_4.add(chkLabels, gbc_chkLabels);

		chkReason = new JCheckBox("Motivo:");
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

		chkLocation = new JCheckBox("Local:");
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

		JXImageView imageView = new JXImageView();
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
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		setName("firma.dlg.add_appearance");
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setBounds(100, 100, 380, 471);

		context.getResourceMap().injectComponents(this);

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
	private JRadioButton rdioRenderName;
	private JRadioButton rdioRenderGraphic;

	public AppearanceOptions open(AppearanceOptions options) {
		this.options = options;
		initDataBindings();
		setLocationRelativeTo(this.context.getMainFrame());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		return this.options;
	}
	protected void initDataBindings() {
		BeanProperty<AppearanceOptions, Boolean> appearanceOptionsBeanProperty = BeanProperty.create("showReason");
		BeanProperty<JCheckBox, Boolean> jCheckBoxBeanProperty = BeanProperty.create("selected");
		AutoBinding<AppearanceOptions, Boolean, JCheckBox, Boolean> autoBinding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, options, appearanceOptionsBeanProperty, chkReason, jCheckBoxBeanProperty, "showReason");
		autoBinding.bind();
		//
		BeanProperty<AppearanceOptions, Boolean> appearanceOptionsBeanProperty_1 = BeanProperty.create("showName");
		AutoBinding<AppearanceOptions, Boolean, JCheckBox, Boolean> autoBinding_1 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, options, appearanceOptionsBeanProperty_1, chkName, jCheckBoxBeanProperty, "showName");
		autoBinding_1.bind();
		//
		BeanProperty<AppearanceOptions, Boolean> appearanceOptionsBeanProperty_2 = BeanProperty.create("showDate");
		AutoBinding<AppearanceOptions, Boolean, JCheckBox, Boolean> autoBinding_2 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, options, appearanceOptionsBeanProperty_2, chkDate, jCheckBoxBeanProperty, "showDate");
		autoBinding_2.bind();
		//
		BeanProperty<AppearanceOptions, Boolean> appearanceOptionsBeanProperty_3 = BeanProperty.create("showLabels");
		AutoBinding<AppearanceOptions, Boolean, JCheckBox, Boolean> autoBinding_3 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, options, appearanceOptionsBeanProperty_3, chkLabels, jCheckBoxBeanProperty, "showLabels");
		autoBinding_3.bind();
		//
		BeanProperty<AppearanceOptions, Boolean> appearanceOptionsBeanProperty_4 = BeanProperty.create("sbowLocal");
		AutoBinding<AppearanceOptions, Boolean, JCheckBox, Boolean> autoBinding_4 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, options, appearanceOptionsBeanProperty_4, chkLocation, jCheckBoxBeanProperty, "showLocal");
		autoBinding_4.bind();
		//
		BeanProperty<AppearanceOptions, String> appearanceOptionsBeanProperty_5 = BeanProperty.create("reason");
		BeanProperty<JTextField, String> jTextFieldBeanProperty = BeanProperty.create("text");
		AutoBinding<AppearanceOptions, String, JTextField, String> autoBinding_5 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, options, appearanceOptionsBeanProperty_5, txtReason, jTextFieldBeanProperty, "reason");
		autoBinding_5.bind();
		//
		BeanProperty<AppearanceOptions, String> appearanceOptionsBeanProperty_6 = BeanProperty.create("local");
		BeanProperty<JTextField, String> jTextFieldBeanProperty_1 = BeanProperty.create("text");
		AutoBinding<AppearanceOptions, String, JTextField, String> autoBinding_6 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, options, appearanceOptionsBeanProperty_6, txtLocation, jTextFieldBeanProperty_1, "local");
		autoBinding_6.bind();
		//
		BeanProperty<AppearanceOptions, String> appearanceOptionsBeanProperty_7 = BeanProperty.create("name");
		BeanProperty<JTextField, String> jTextFieldBeanProperty_2 = BeanProperty.create("text");
		AutoBinding<AppearanceOptions, String, JTextField, String> autoBinding_7 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, options, appearanceOptionsBeanProperty_7, textField, jTextFieldBeanProperty_2, "name");
		autoBinding_7.bind();
		//
		BeanProperty<AppearanceOptions, Boolean> appearanceOptionsBeanProperty_8 = BeanProperty.create("renderName");
		BeanProperty<JRadioButton, Boolean> jRadioButtonBeanProperty = BeanProperty.create("selected");
		AutoBinding<AppearanceOptions, Boolean, JRadioButton, Boolean> autoBinding_8 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, options, appearanceOptionsBeanProperty_8, rdioRenderName, jRadioButtonBeanProperty, "renderName");
		autoBinding_8.bind();
		//
		BeanProperty<AppearanceOptions, Boolean> appearanceOptionsBeanProperty_9 = BeanProperty.create("renderGraphic");
		AutoBinding<AppearanceOptions, Boolean, JRadioButton, Boolean> autoBinding_9 = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, options, appearanceOptionsBeanProperty_9, rdioRenderGraphic, jRadioButtonBeanProperty, "renderGraphic");
		autoBinding_9.bind();
	}
}
