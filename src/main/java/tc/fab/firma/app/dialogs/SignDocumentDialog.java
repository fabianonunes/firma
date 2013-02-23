package tc.fab.firma.app.dialogs;

import iaik.pkcs.pkcs11.TokenException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.fit.cssbox.swingbox.BrowserPane;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.mechanisms.TokenInfo;

@Singleton
public class SignDocumentDialog extends JDialog {

	private static final long serialVersionUID = 7850839445605448945L;

	private AppContext context;
	private AppController controller;

	private final JPanel contentPanel = new JPanel();

	private JComboBox<String> cbAlias;
	private JComboBox<String> cbProvider;
	private JComboBox<String> cbAppearance;

	private JButton btAddProvider;
	private JButton btOk;
	private JButton btCancel;
	private JButton btCertificateInfo;

	@Inject
	public SignDocumentDialog(AppContext context, AppController controller) {

		super(context.getMainFrame(), true);

		this.context = context;
		this.controller = controller;

		initComponents();

		fillProviders();

		cbProvider.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				fillAliases((String) cbProvider.getSelectedItem());
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
		// cbProvider.removeAllItems();
		// cbProvider.addItem("aguarde...");
		// cbProvider.setEnabled(false);
		cbProvider.addItem("Windows");
		cbProvider.addItem("/usr/lib/libaetpkss.so");
	}

	public void initComponents() {

		ActionMap actionMap = controller.getActionMap();

		btOk = new JButton();
		btCancel = new JButton("Cancel");
		cbAlias = new JComboBox<String>();
		cbAppearance = new JComboBox<String>();
		cbProvider = new JComboBox<String>();
		btAddProvider = new JButton("i");

		btOk.setAction(actionMap.get(AppController.ACTION_FILE_PREVIEW));

		JLabel lblTipoDoCertificado = new JLabel("Local do certificado");
		JLabel lblAssinarComo = new JLabel("Assinar como");
		BrowserPane browserPane = new BrowserPane();
		JSeparator separator = new JSeparator();

		setTitle("Assinar documentos");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 450, 292);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		cbProvider.setPreferredSize(new Dimension(32, 22));
		cbAlias.setPreferredSize(new Dimension(32, 22));
		browserPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		cbAppearance.setPreferredSize(new Dimension(32, 22));
		JSeparator separator_1 = new JSeparator();
		btCertificateInfo = new JButton("Informações...");
		btCertificateInfo.setPreferredSize(new Dimension(80, 22));
		btCertificateInfo.setActionCommand("OK");
		btAddProvider.setMinimumSize(new Dimension(108, 22));
		btAddProvider.setPreferredSize(new Dimension(108, 22));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel
			.setHorizontalGroup(gl_contentPanel
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
					gl_contentPanel
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
							gl_contentPanel
								.createParallelGroup(Alignment.TRAILING)
								.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 341,
									Short.MAX_VALUE)
								.addComponent(browserPane, GroupLayout.DEFAULT_SIZE, 341,
									Short.MAX_VALUE)
								.addComponent(separator, GroupLayout.DEFAULT_SIZE, 341,
									Short.MAX_VALUE)
								.addGroup(
									gl_contentPanel
										.createSequentialGroup()
										.addComponent(btCertificateInfo,
											GroupLayout.PREFERRED_SIZE, 101,
											GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, 78,
											Short.MAX_VALUE)
										.addComponent(cbAppearance, GroupLayout.PREFERRED_SIZE,
											162, GroupLayout.PREFERRED_SIZE))
								.addGroup(
									gl_contentPanel
										.createSequentialGroup()
										.addGroup(
											gl_contentPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(lblTipoDoCertificado)
												.addComponent(lblAssinarComo))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(
											gl_contentPanel
												.createParallelGroup(Alignment.TRAILING)
												.addGroup(
													gl_contentPanel
														.createSequentialGroup()
														.addComponent(cbProvider, 0, 157,
															Short.MAX_VALUE)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(btAddProvider,
															GroupLayout.PREFERRED_SIZE, 41,
															GroupLayout.PREFERRED_SIZE))
												.addComponent(cbAlias, 0, 210, Short.MAX_VALUE))))
						.addContainerGap()));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
			.addGroup(
				gl_contentPanel
					.createSequentialGroup()
					.addContainerGap()
					.addGroup(
						gl_contentPanel
							.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblTipoDoCertificado)
							.addComponent(cbProvider, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(btAddProvider, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(
						gl_contentPanel
							.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblAssinarComo)
							.addComponent(cbAlias, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 2,
						GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(browserPane, GroupLayout.PREFERRED_SIZE, 112,
						GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(
						gl_contentPanel
							.createParallelGroup(Alignment.LEADING, false)
							.addComponent(btCertificateInfo, GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(cbAppearance, GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 2,
						GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
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
				btOk.setPreferredSize(new Dimension(80, 27));
				buttonPane.add(btOk);
				getRootPane().setDefaultButton(btOk);
			}
			{
				btCancel.setPreferredSize(new Dimension(80, 27));
				btCancel.setActionCommand("Cancel");
				buttonPane.add(btCancel);
			}
		}
	}

	public void open() {
		setLocationRelativeTo(this.context.getMainFrame());
		setVisible(true);
	}
}
