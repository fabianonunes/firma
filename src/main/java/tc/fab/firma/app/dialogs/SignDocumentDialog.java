package tc.fab.firma.app.dialogs;

import iaik.pkcs.pkcs11.TokenException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.fit.cssbox.swingbox.BrowserPane;

import tc.fab.app.AppContext;
import tc.fab.mechanisms.Mechanism;
import tc.fab.mechanisms.TokenInfo;

@Singleton
public class SignDocumentDialog extends JDialog {

	private static final long serialVersionUID = 7850839445605448945L;

	private AppContext context;

	private JComboBox<String> cbAlias;
	private final JPanel contentPanel = new JPanel();
	private JComboBox<String> cbProvider;
	private JComboBox<String> cbAppearance;
	private JButton btnNewButton;

	@Inject
	public SignDocumentDialog(AppContext context, Mechanism mechanism) {

		super(context.getMainFrame(), true);

		this.context = context;

		initComponents();

		setLocationRelativeTo(this.context.getMainFrame());

		fillProviders();

		cbProvider.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					fillAliases(e.getItem().toString());
				}
			}
		});

	}

	protected void fillAliases(final String string) {
		cbAlias.removeAllItems();
		cbAlias.addItem("aguarde...");
		cbAlias.setEnabled(false);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				ArrayList<String> aliases;
				try {
					cbAlias.removeAllItems();
					cbAlias.setEnabled(true);
					aliases = TokenInfo.getAliases(string);
					for (String alias : aliases) {
						cbAlias.addItem(alias);
					}
				} catch (IOException | TokenException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

	private void fillProviders() {
		cbProvider.addItem("Windows");
		cbProvider.addItem("/usr/lib/libaetpkss.so");
	}

	public void initComponents() {
		setTitle("Assinar documentos");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 370, 292);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		cbProvider = new JComboBox<String>();
		cbProvider.setPreferredSize(new Dimension(32, 22));

		cbAlias = new JComboBox<String>();
		cbAlias.setPreferredSize(new Dimension(32, 22));

		JLabel lblTipoDoCertificado = new JLabel("Local do certificado");

		JLabel lblAssinarComo = new JLabel("Assinar como");

		BrowserPane browserPane = new BrowserPane();
		browserPane.setBorder(new LineBorder(Color.LIGHT_GRAY));

		JSeparator separator = new JSeparator();

		cbAppearance = new JComboBox<String>();
		cbAppearance.setPreferredSize(new Dimension(32, 22));

		JSeparator separator_1 = new JSeparator();

		JButton btnInformaes = new JButton("Informações...");
		btnInformaes.setPreferredSize(new Dimension(80, 22));
		btnInformaes.setActionCommand("OK");
		
		btnNewButton = new JButton("i");
		btnNewButton.setMinimumSize(new Dimension(108, 22));
		btnNewButton.setPreferredSize(new Dimension(108, 22));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 341, Short.MAX_VALUE)
						.addComponent(browserPane, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
						.addComponent(separator, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(btnInformaes, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
							.addComponent(cbAppearance, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblTipoDoCertificado)
								.addComponent(lblAssinarComo))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(cbProvider, 0, 157, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
								.addComponent(cbAlias, 0, 210, Short.MAX_VALUE))))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTipoDoCertificado)
						.addComponent(cbProvider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAssinarComo)
						.addComponent(cbAlias, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(browserPane, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnInformaes, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(cbAppearance, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(null);
			buttonPane.setPreferredSize(new Dimension(10, 50));
			FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.RIGHT);
			fl_buttonPane.setHgap(10);
			fl_buttonPane.setVgap(10);
			buttonPane.setLayout(fl_buttonPane);
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setPreferredSize(new Dimension(80, 28));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setPreferredSize(new Dimension(80, 28));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
