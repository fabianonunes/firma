package tc.fab.firma.app.dialogs;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;

import tc.fab.app.firma.AppContext;

public class PINDialog extends JDialog {

	private static final long serialVersionUID = 1818127766295051259L;
	private Boolean status = false;
	private AppContext context;

	public PINDialog(AppContext context) {

		super(context.getMainFrame(), true);

		this.context = context;

		initComponents();

		setLocationRelativeTo(context.getMainFrame());
		setVisible(true);

		getRootPane().setDefaultButton(btOk);

	}

	private void initComponents() {

		ResourceMap resourceMap = context.getAppContext().getResourceMap(PINDialog.class);

		panel = new JPanel();
		separator = new JSeparator();
		btOk = new JButton(context.getAppContext().getActionMap(this).get("doClose"));
		label = new JLabel();
		password = new JPasswordField();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setName("Form"); // NOI18N
		setResizable(false);

		panel.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
		panel.setName("jPanel1"); // NOI18N

		separator.setName("jSeparator1"); // NOI18N

		btOk.setText(resourceMap.getString("jButton1.text")); // NOI18N
		btOk.setName("jButton1"); // NOI18N

		GroupLayout jPanel1Layout = new GroupLayout(panel);
		panel.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(
			GroupLayout.Alignment.LEADING).addGroup(
			jPanel1Layout
				.createSequentialGroup()
				.addContainerGap()
				.addGroup(
					jPanel1Layout
						.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(btOk, GroupLayout.PREFERRED_SIZE, 84,
							GroupLayout.PREFERRED_SIZE)
						.addComponent(separator, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
				.addContainerGap()));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(
			GroupLayout.Alignment.LEADING)
			.addGroup(
				jPanel1Layout
					.createSequentialGroup()
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 10,
						GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(btOk, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
						Short.MAX_VALUE).addContainerGap()));

		label.setText(resourceMap.getString("jLabel1.text")); // NOI18N
		label.setName("jLabel1"); // NOI18N

		password.setText(resourceMap.getString("jPasswordField1.text")); // NOI18N
		password.setName("jPasswordField1"); // NOI18N

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
			.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE)
			.addGroup(
				layout
					.createSequentialGroup()
					.addContainerGap()
					.addComponent(label)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
						GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(password, GroupLayout.PREFERRED_SIZE, 121,
						GroupLayout.PREFERRED_SIZE).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
			GroupLayout.Alignment.TRAILING,
			layout
				.createSequentialGroup()
				.addContainerGap()
				.addGroup(
					layout
						.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(label)
						.addComponent(password, GroupLayout.PREFERRED_SIZE,
							GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
					Short.MAX_VALUE)
				.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
					GroupLayout.PREFERRED_SIZE)));

		pack();

	}

	public char[] getPassword() {
		char[] r = password.getPassword();
		password.setText(null);
		return r;
	}

	@Action
	public void doClose() {
		status = true;
		setVisible(false);
		dispose();
	}

	private JButton btOk;
	private JLabel label;
	private JPanel panel;
	private JPasswordField password;
	private JSeparator separator;

	public Boolean getStatus() {
		return status;
	}

}
