package tc.fab.firma.app.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.inject.Inject;
import javax.inject.Singleton;
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

import org.jdesktop.swingx.JXImageView;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.app.AppDocument;

@Singleton
public class AppearanceDialog extends JDialog {

	private static final long serialVersionUID = -4115566787900700647L;
	
	private AppContext context;

	@Inject
	public AppearanceDialog(AppContext context, AppController controller, AppDocument document) {
		super(context.getMainFrame(), true);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.context = context;
		initComponents();
	}

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField textField_3;
	private JTextField textField_4;

	public void initComponents() {

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JLabel lblNewLabel = new JLabel("Nome");

		textField = new JTextField();
		textField.setPreferredSize(new Dimension(0, 24));
		textField.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Visualiza\u00E7\u00E3o", TitledBorder.LEADING, TitledBorder.TOP, null, null));

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
							.addComponent(panel_3, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
								416, Short.MAX_VALUE)
							.addComponent(panel_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
								416, Short.MAX_VALUE)
							.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 416,
								Short.MAX_VALUE)
							.addGroup(
								Alignment.TRAILING,
								gl_contentPanel
									.createSequentialGroup()
									.addComponent(lblNewLabel)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(textField, GroupLayout.DEFAULT_SIZE, 346,
										Short.MAX_VALUE))).addContainerGap()));
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
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 149,
						GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE).addContainerGap(204, Short.MAX_VALUE)));

		JPanel panel_2 = new JPanel();
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel_2.rowHeights = new int[] { 0 };
		gbl_panel_2.columnWeights = new double[] { 0.0, 1.0, 0.0 };
		gbl_panel_2.rowWeights = new double[] { 0.0 };
		panel_2.setLayout(gbl_panel_2);

		JRadioButton radioButton = new JRadioButton("Nome");
		buttonGroup.add(radioButton);
		GridBagConstraints gbc_radioButton = new GridBagConstraints();
		gbc_radioButton.anchor = GridBagConstraints.WEST;
		gbc_radioButton.insets = new Insets(0, 0, 0, 5);
		gbc_radioButton.gridx = 0;
		gbc_radioButton.gridy = 0;
		panel_2.add(radioButton, gbc_radioButton);

		JRadioButton radioButton_1 = new JRadioButton("Importar imagem");
		buttonGroup.add(radioButton_1);
		GridBagConstraints gbc_radioButton_1 = new GridBagConstraints();
		gbc_radioButton_1.insets = new Insets(0, 0, 0, 5);
		gbc_radioButton_1.gridx = 1;
		gbc_radioButton_1.gridy = 0;
		panel_2.add(radioButton_1, gbc_radioButton_1);

		JRadioButton radioButton_2 = new JRadioButton("Sem imagem");
		buttonGroup.add(radioButton_2);
		GridBagConstraints gbc_radioButton_2 = new GridBagConstraints();
		gbc_radioButton_2.anchor = GridBagConstraints.EAST;
		gbc_radioButton_2.gridx = 2;
		gbc_radioButton_2.gridy = 0;
		panel_2.add(radioButton_2, gbc_radioButton_2);
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

		JCheckBox checkBox_6 = new JCheckBox("Nome");
		GridBagConstraints gbc_checkBox_6 = new GridBagConstraints();
		gbc_checkBox_6.anchor = GridBagConstraints.WEST;
		gbc_checkBox_6.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_6.gridx = 0;
		gbc_checkBox_6.gridy = 0;
		panel_4.add(checkBox_6, gbc_checkBox_6);

		JCheckBox checkBox_7 = new JCheckBox("Nome distinto");
		GridBagConstraints gbc_checkBox_7 = new GridBagConstraints();
		gbc_checkBox_7.anchor = GridBagConstraints.WEST;
		gbc_checkBox_7.insets = new Insets(0, 0, 5, 0);
		gbc_checkBox_7.gridx = 1;
		gbc_checkBox_7.gridy = 0;
		panel_4.add(checkBox_7, gbc_checkBox_7);

		JCheckBox checkBox_8 = new JCheckBox("Data");
		GridBagConstraints gbc_checkBox_8 = new GridBagConstraints();
		gbc_checkBox_8.anchor = GridBagConstraints.WEST;
		gbc_checkBox_8.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_8.gridx = 0;
		gbc_checkBox_8.gridy = 1;
		panel_4.add(checkBox_8, gbc_checkBox_8);

		JCheckBox checkBox_9 = new JCheckBox("Etiquetas");
		GridBagConstraints gbc_checkBox_9 = new GridBagConstraints();
		gbc_checkBox_9.anchor = GridBagConstraints.WEST;
		gbc_checkBox_9.insets = new Insets(0, 0, 5, 0);
		gbc_checkBox_9.gridx = 1;
		gbc_checkBox_9.gridy = 1;
		panel_4.add(checkBox_9, gbc_checkBox_9);

		JCheckBox checkBox_10 = new JCheckBox("Motivo");
		GridBagConstraints gbc_checkBox_10 = new GridBagConstraints();
		gbc_checkBox_10.anchor = GridBagConstraints.WEST;
		gbc_checkBox_10.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_10.gridx = 0;
		gbc_checkBox_10.gridy = 2;
		panel_4.add(checkBox_10, gbc_checkBox_10);

		textField_3 = new JTextField();
		textField_3.setMinimumSize(new Dimension(0, 22));
		textField_3.setPreferredSize(new Dimension(0, 24));
		textField_3.setColumns(10);
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.insets = new Insets(0, 0, 5, 0);
		gbc_textField_3.gridx = 1;
		gbc_textField_3.gridy = 2;
		panel_4.add(textField_3, gbc_textField_3);

		JCheckBox checkBox_11 = new JCheckBox("Local");
		GridBagConstraints gbc_checkBox_11 = new GridBagConstraints();
		gbc_checkBox_11.anchor = GridBagConstraints.WEST;
		gbc_checkBox_11.insets = new Insets(0, 0, 0, 5);
		gbc_checkBox_11.gridx = 0;
		gbc_checkBox_11.gridy = 3;
		panel_4.add(checkBox_11, gbc_checkBox_11);

		textField_4 = new JTextField();
		textField_4.setMinimumSize(new Dimension(0, 22));
		textField_4.setPreferredSize(new Dimension(0, 24));
		textField_4.setColumns(10);
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 1;
		gbc_textField_4.gridy = 3;
		panel_4.add(textField_4, gbc_textField_4);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(
			gl_panel_3.createSequentialGroup().addContainerGap()
				.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 380, GroupLayout.PREFERRED_SIZE)
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(
			gl_panel_3.createSequentialGroup().addContainerGap()
				.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
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
		setBounds(100, 100, 450, 479);
		
		context.getResourceMap().injectComponents(this);


	}

	public void open() {
		setLocationRelativeTo(this.context.getMainFrame());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

}
