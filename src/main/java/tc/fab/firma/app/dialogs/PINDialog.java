package tc.fab.firma.app.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.inject.Inject;
import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.jdesktop.application.Action;

import tc.fab.app.AppContext;

public class PINDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final String ACTION_PIN_OK = "firma.dlg.pin.ok";
	private AppContext context;

	private boolean status;

	private JPasswordField passwordField;
	private JButton btOk;

	@Inject
	public PINDialog(AppContext context) {
		super(context.getMainFrame(), true);
		this.context = context;
		initComponents();
		open();
	}

	public void initComponents() {

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		ActionMap actionMap = this.context.getAppContext().getActionMap(this);

		btOk = new JButton();
		btOk.setPreferredSize(new Dimension(60, 22));
		btOk.setAction(actionMap.get(ACTION_PIN_OK));
		getRootPane().setDefaultButton(btOk);

		JPanel contentPanel = new JPanel();

		setBounds(100, 100, 246, 124);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JLabel label = new JLabel();
		label.setText(context.getResReader().getString("firma.dlg.pin.enter_pin.text"));

		passwordField = new JPasswordField();
		passwordField.setPreferredSize(new Dimension(32, 24));

		JSeparator separator = new JSeparator();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel
			.createParallelGroup(Alignment.LEADING)
			.addGroup(
				gl_contentPanel.createSequentialGroup().addContainerGap().addComponent(label)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
					.addContainerGap())
			.addComponent(separator, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 236,
				Short.MAX_VALUE));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
			.addGroup(
				gl_contentPanel
					.createSequentialGroup()
					.addContainerGap()
					.addGroup(
						gl_contentPanel
							.createParallelGroup(Alignment.BASELINE)
							.addComponent(label)
							.addComponent(passwordField, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE)));
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setPreferredSize(new Dimension(10, 36));
			FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.CENTER);
			buttonPane.setLayout(fl_buttonPane);
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.add(btOk);
		}
	}

	@Action(name = ACTION_PIN_OK)
	public void doOk() {
		status = true;
		setVisible(false);
		dispose();
	}

	public char[] getPassword() {
		char[] r = passwordField.getPassword();
		passwordField.setText(null);
		return r;
	}

	public Boolean getStatus() {
		return status;
	}

	public void open() {
		setLocationRelativeTo(context.getMainFrame());
		setVisible(true);
	}
}
