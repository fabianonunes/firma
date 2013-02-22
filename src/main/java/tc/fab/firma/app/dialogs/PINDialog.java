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

import tc.fab.firma.app.AppContext;

public class PINDialog extends JDialog {

	private static final long	serialVersionUID	= 1818127766295051259L;
	private Boolean				status				= false;
	private AppContext			context;

	public PINDialog(AppContext context) {

		super(context.getMainFrame(), true);

		this.context = context;

		initComponents();
		getRootPane().setDefaultButton(btOk);

	}

	private void initComponents() {

		ResourceMap resourceMap = context.getAppContext().getResourceMap(PINDialog.class);

		jPanel1 = new JPanel();
		jSeparator1 = new JSeparator();
		btOk = new JButton(context.getAppContext().getActionMap(this).get("doClose"));
		jLabel1 = new JLabel();
		jPasswordField1 = new JPasswordField();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setName("Form"); // NOI18N
		setResizable(false);

		jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
		jPanel1.setName("jPanel1"); // NOI18N

		jSeparator1.setName("jSeparator1"); // NOI18N

		btOk.setText(resourceMap.getString("jButton1.text")); // NOI18N
		btOk.setName("jButton1"); // NOI18N

		GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
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
						.addComponent(jSeparator1, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
				.addContainerGap()));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(
			GroupLayout.Alignment.LEADING).addGroup(
			jPanel1Layout
				.createSequentialGroup()
				.addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 10,
					GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(btOk, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
					Short.MAX_VALUE).addContainerGap()));

		jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
		jLabel1.setName("jLabel1"); // NOI18N

		jPasswordField1.setText(resourceMap.getString("jPasswordField1.text")); // NOI18N
		jPasswordField1.setName("jPasswordField1"); // NOI18N

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
			.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE)
			.addGroup(
				layout
					.createSequentialGroup()
					.addContainerGap()
					.addComponent(jLabel1)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
						GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(jPasswordField1, GroupLayout.PREFERRED_SIZE, 121,
						GroupLayout.PREFERRED_SIZE).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
			GroupLayout.Alignment.TRAILING,
			layout
				.createSequentialGroup()
				.addContainerGap()
				.addGroup(
					layout
						.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(jLabel1)
						.addComponent(jPasswordField1, GroupLayout.PREFERRED_SIZE,
							GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
					Short.MAX_VALUE)
				.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
					GroupLayout.PREFERRED_SIZE)));

		pack();

	}

	public char[] getPassword() {
		char[] r = jPasswordField1.getPassword();
		jPasswordField1.setText(null);
		return r;
	}

	@Action
	public void doClose() {
		status = true;
		setVisible(false);
		dispose();
	}

	private JButton			btOk;
	private JLabel			jLabel1;
	private JPanel			jPanel1;
	private JPasswordField	jPasswordField1;
	private JSeparator		jSeparator1;

	public Boolean getStatus() {
		return status;
	}

}
