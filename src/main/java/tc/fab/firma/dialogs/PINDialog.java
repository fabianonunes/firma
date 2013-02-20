/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewJDialog.java
 *
 * Created on 28/04/2009, 15:11:50
 */

package tc.fab.firma.dialogs;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;

public class PINDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 1818127766295051259L;

    private Boolean status = false;

    public PINDialog(java.awt.Frame parent, boolean modal) {
	super(parent, modal);
	initComponents();
    }

    private void initComponents() {

	jPanel1 = new javax.swing.JPanel();
	jSeparator1 = new javax.swing.JSeparator();
	jButton1 = new javax.swing.JButton(Application.getInstance()
		.getContext().getActionMap(this).get("doClose"));
	jLabel1 = new javax.swing.JLabel();
	jPasswordField1 = new javax.swing.JPasswordField();

	setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	setName("Form"); // NOI18N
	setResizable(false);

	org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
		.getInstance(tc.fab.pdf.signer.application.PDFSignerApp.class)
		.getContext().getResourceMap(PINDialog.class);
	jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
	jPanel1.setName("jPanel1"); // NOI18N

	jSeparator1.setName("jSeparator1"); // NOI18N

	jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
	jButton1.setName("jButton1"); // NOI18N
	// jButton1.addActionListener(new java.awt.event.ActionListener() {
	// public void actionPerformed(java.awt.event.ActionEvent evt) {
	// okButtonActionPerformed(evt);
	// }
	// });

	javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
		jPanel1);
	jPanel1.setLayout(jPanel1Layout);
	jPanel1Layout
		.setHorizontalGroup(jPanel1Layout
			.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(
				jPanel1Layout
					.createSequentialGroup()
					.addContainerGap()
					.addGroup(
						jPanel1Layout
							.createParallelGroup(
								javax.swing.GroupLayout.Alignment.CENTER)
							.addComponent(
								jButton1,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								84,
								javax.swing.GroupLayout.PREFERRED_SIZE)
							.addComponent(
								jSeparator1,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								238,
								Short.MAX_VALUE))
					.addContainerGap()));
	jPanel1Layout
		.setVerticalGroup(jPanel1Layout
			.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(
				jPanel1Layout
					.createSequentialGroup()
					.addComponent(
						jSeparator1,
						javax.swing.GroupLayout.PREFERRED_SIZE,
						10,
						javax.swing.GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(
						javax.swing.LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(
						jButton1,
						javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE,
						Short.MAX_VALUE)
					.addContainerGap()));

	jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
	jLabel1.setName("jLabel1"); // NOI18N

	jPasswordField1.setText(resourceMap.getString("jPasswordField1.text")); // NOI18N
	jPasswordField1.setName("jPasswordField1"); // NOI18N

	javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
		getContentPane());
	getContentPane().setLayout(layout);
	layout.setHorizontalGroup(layout
		.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
			javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		.addGroup(
			layout.createSequentialGroup()
				.addContainerGap()
				.addComponent(jLabel1)
				.addPreferredGap(
					javax.swing.LayoutStyle.ComponentPlacement.RELATED,
					javax.swing.GroupLayout.DEFAULT_SIZE,
					Short.MAX_VALUE)
				.addComponent(jPasswordField1,
					javax.swing.GroupLayout.PREFERRED_SIZE,
					121,
					javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap()));
	layout.setVerticalGroup(layout
		.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		.addGroup(
			javax.swing.GroupLayout.Alignment.TRAILING,
			layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(
					layout.createParallelGroup(
						javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(jLabel1)
						.addComponent(
							jPasswordField1,
							javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(
					javax.swing.LayoutStyle.ComponentPlacement.RELATED,
					javax.swing.GroupLayout.DEFAULT_SIZE,
					Short.MAX_VALUE)
				.addComponent(jPanel1,
					javax.swing.GroupLayout.PREFERRED_SIZE,
					javax.swing.GroupLayout.DEFAULT_SIZE,
					javax.swing.GroupLayout.PREFERRED_SIZE)));

	pack();
    }

    public char[] getPassword() {
	char[] r = jPasswordField1.getPassword();
	jPasswordField1.setText(null);
	return r;
    }

    @Action
    public void doClose() {
	setStatus(true);
	setVisible(false);
	dispose();
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JSeparator jSeparator1;

    /**
     * @return the status
     */
    public Boolean getStatus() {
	return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(Boolean status) {
	this.status = status;
    }

}
