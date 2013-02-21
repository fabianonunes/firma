package tc.fab.firma.app.dialogs;

public class FileSelectorDialog extends javax.swing.JDialog {

	private static final long	serialVersionUID	= 1L;

	public FileSelectorDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		setLocationRelativeTo(parent);
	}

	private void initComponents() {

		jFileChooser = new javax.swing.JFileChooser();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addComponent(jFileChooser,
			javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			layout
				.createSequentialGroup()
				.addComponent(jFileChooser, javax.swing.GroupLayout.PREFERRED_SIZE,
					javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pack();
	}

	private javax.swing.JFileChooser	jFileChooser;

	public javax.swing.JFileChooser getJFileChooser() {
		return jFileChooser;
	}

}
