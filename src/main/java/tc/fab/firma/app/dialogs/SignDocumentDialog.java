package tc.fab.firma.app.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.apache.commons.codec.binary.Hex;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import org.fit.cssbox.swingbox.BrowserPane;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;
import org.jdesktop.application.Task.BlockingScope;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.app.AppDocument;
import tc.fab.firma.FirmaOptions;
import tc.fab.mechanisms.Mechanism;
import tc.fab.mechanisms.ProviderManager;

public class SignDocumentDialog extends JDialog {

	private static final long serialVersionUID = 7850839445605448945L;

	private static final String ACTION_FILL_ALIASES = "firma.dlg.sign_document.fill_aliases";
	private static final String ACTION_ADD_PROVIDER = "firma.dlg.sign_document.add_provider";
	private static final String ACTION_SIGN = "firma.dlg.sign_document.sign";

	private AppContext context;
	private AppController controller;
	private FirmaOptions options;

	private final JPanel contentPanel = new JPanel();

	private JComboBox<String> cbAlias;
	private JComboBox<String> cbProvider;
	private JComboBox<String> comboBox;
	private ProviderManager providerManager;

	@Inject
	public SignDocumentDialog(AppContext context, AppController controller, AppDocument document,
		ProviderManager providersManager) {

		super(context.getMainFrame(), true);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.context = context;
		this.controller = controller;
		this.providerManager = providersManager;
		this.options = document.getOptions();

		initComponents();

		fillProviders();
		// the action setup must be after initial fulfillment to avoid double
		// fire
		cbProvider.setAction(context.getAction(this, ACTION_FILL_ALIASES));

	}

	@Action(name = ACTION_SIGN)
	public void sign() throws Exception {
		String alias = (String) cbAlias.getSelectedItem();
		String provider = (String) cbProvider.getSelectedItem();
		options.setAlias(alias);
		options.setProvider(provider);
		context.fireAction(controller, AppController.ACTION_FILE_PREVIEW);

		Mechanism m = providerManager.getMechanism(provider, alias);
		m.login();

		Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initSign(m.getPrivateKey(alias));
		signature.update("fabiano nunes parente".getBytes());

		byte[] data_signed = signature.sign();

		System.out.println(Hex.encodeHex(data_signed));
		
		m.logout();


	}

	@Action(name = ACTION_FILL_ALIASES, block = BlockingScope.ACTION)
	public Task<Void, String> fillAliases() {
		String provider = cbProvider.getItemAt(cbProvider.getSelectedIndex());
		return new FillAliasesTask(provider);
	}

	class FillAliasesTask extends Task<Void, String> {

		private String provider;
		private ArrayList<String> aliases;

		public FillAliasesTask(String provider) {
			super(context.getAppContext().getApplication());
			this.provider = provider;
			cbAlias.setEnabled(false);
			cbAlias.removeAllItems();
			cbAlias.addItem(context.getResReader().getString("firma.dlg.waiting"));
		}

		@Override
		protected Void doInBackground() throws Exception {
			aliases = providerManager.aliases(provider);
			if (aliases.size() > 0) {
				for (String alias : aliases) {
					publish(alias);
				}
			}
			return null;
		}

		@Override
		protected void process(List<String> values) {
			super.process(values);
			for (String value : values) {
				cbAlias.addItem(value);
			}
		}

		@Override
		protected void failed(Throwable cause) {
			super.failed(cause);
			cbAlias.removeAllItems();
		}

		@Override
		protected void succeeded(Void result) {
			cbAlias.setSelectedItem(options.getAlias());
			cbAlias.setEnabled(true);
		}

		@Override
		protected void finished() {
			cbAlias.removeItemAt(0);
		}

	}

	private void fillProviders() {
		List<String> libs = providerManager.getProviders();
		for (String lib : libs) {
			cbProvider.addItem(lib);
		}
		if (libs.size() > 0) {
			context.fireAction(this, ACTION_FILL_ALIASES);
		}
	}

	@Action(name = ACTION_ADD_PROVIDER)
	public void addProvider() {

	}

	public void open() {
		setLocationRelativeTo(this.context.getMainFrame());
		setVisible(true);
	}

	public void initComponents() {

		cbAlias = new JComboBox<String>();
		cbProvider = new JComboBox<String>();

		JButton btOk = new JButton();
		JButton btAddProvider = new JButton();
		JButton btRefresh = new JButton();
		JSeparator separator = new JSeparator();

		btOk.setAction(context.getAction(this, ACTION_SIGN));
		btRefresh.setAction(context.getAction(this, ACTION_FILL_ALIASES));
		btAddProvider.setAction(context.getAction(this, ACTION_ADD_PROVIDER));

		JButton btCancel = new JButton();
		btCancel.setName("firma.dlg.sign_document.cancel");

		JLabel lblTipoDoCertificado = new JLabel();
		lblTipoDoCertificado.setName("firma.dlg.sign_document.provider");

		JLabel lblAssinarComo = new JLabel();
		lblAssinarComo.setName("firma.dlg.sign_document.alias");

		JButton btCertificateInfo = new JButton();
		btCertificateInfo.setName("firma.dlg.sign_document.info");

		getRootPane().setDefaultButton(btOk);

		btRefresh.setPreferredSize(new Dimension(0, 24));
		btRefresh.setMinimumSize(new Dimension(108, 22));

		comboBox = new JComboBox<>();
		comboBox.setPreferredSize(new Dimension(0, 24));

		BrowserPane browserPane = new BrowserPane();
		browserPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel
			.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(
					Alignment.TRAILING,
					gl_contentPanel
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
							gl_contentPanel
								.createParallelGroup(Alignment.TRAILING)
								.addComponent(browserPane, Alignment.LEADING,
									GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
								.addComponent(separator, GroupLayout.DEFAULT_SIZE, 428,
									Short.MAX_VALUE)
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
												.addComponent(cbAlias, Alignment.LEADING, 0, 369,
													Short.MAX_VALUE)
												.addComponent(cbProvider, Alignment.LEADING, 0,
													369, Short.MAX_VALUE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(
											gl_contentPanel
												.createParallelGroup(Alignment.LEADING)
												.addComponent(btRefresh,
													GroupLayout.PREFERRED_SIZE, 41,
													GroupLayout.PREFERRED_SIZE)
												.addComponent(btAddProvider,
													GroupLayout.PREFERRED_SIZE, 41,
													GroupLayout.PREFERRED_SIZE)))
								.addGroup(
									Alignment.LEADING,
									gl_contentPanel
										.createSequentialGroup()
										.addComponent(btCertificateInfo,
											GroupLayout.PREFERRED_SIZE, 101,
											GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, 177,
											Short.MAX_VALUE)
										.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 138,
											GroupLayout.PREFERRED_SIZE))).addContainerGap()));
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
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(btRefresh, GroupLayout.PREFERRED_SIZE, 25,
								GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 2,
						GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(browserPane, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(
						gl_contentPanel
							.createParallelGroup(Alignment.LEADING)
							.addComponent(btCertificateInfo, Alignment.TRAILING,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
							.addComponent(comboBox, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))));

		setName("firma.dlg.sign_document");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 450, 292);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		cbProvider.setPreferredSize(new Dimension(0, 24));
		cbAlias.setPreferredSize(new Dimension(0, 24));
		btCertificateInfo.setPreferredSize(new Dimension(0, 24));
		btAddProvider.setMinimumSize(new Dimension(108, 22));
		btAddProvider.setPreferredSize(new Dimension(0, 24));
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
				buttonPane.add(btCancel);
			}
		}

		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { btOk, btCancel,
			cbProvider, btAddProvider, cbAlias, btRefresh, btCertificateInfo }));

		context.getResourceMap().injectComponents(this);

	}
}
