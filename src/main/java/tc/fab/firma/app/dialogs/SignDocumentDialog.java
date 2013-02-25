package tc.fab.firma.app.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.eclipse.wb.swing.FocusTraversalOnArray;
import org.fit.cssbox.swingbox.BrowserPane;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;
import org.jdesktop.application.Task.BlockingScope;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.app.AppDocument;
import tc.fab.firma.FirmaOptions;
import tc.fab.mechanisms.ProviderManager;

@Singleton
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
	private JComboBox<String> cbAppearance;

	private ProviderManager providerManager;

	@Inject
	public SignDocumentDialog(AppContext context, AppController controller, AppDocument document,
		ProviderManager providersManager) {

		super(context.getMainFrame(), true);

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
	public void sign() {
		options.setAlias((String) cbAlias.getSelectedItem());
		options.setProvider((String) cbProvider.getSelectedItem());
		context.fireAction(controller, AppController.ACTION_FILE_PREVIEW);
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
			aliases = providerManager.getAliases(provider);
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
		cbAppearance = new JComboBox<String>();
		cbProvider = new JComboBox<String>();

		JButton btOk = new JButton();
		JButton btAddProvider = new JButton();
		JButton btRefresh = new JButton();
		BrowserPane browserPane = new BrowserPane();
		JSeparator separator = new JSeparator();
		JSeparator separator_1 = new JSeparator();

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
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel
			.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(
					gl_contentPanel
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
							gl_contentPanel
								.createParallelGroup(Alignment.TRAILING)
								.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 416,
									Short.MAX_VALUE)
								.addComponent(browserPane, GroupLayout.DEFAULT_SIZE, 416,
									Short.MAX_VALUE)
								.addComponent(separator, GroupLayout.DEFAULT_SIZE, 416,
									Short.MAX_VALUE)
								.addGroup(
									gl_contentPanel
										.createSequentialGroup()
										.addComponent(btCertificateInfo,
											GroupLayout.PREFERRED_SIZE, 101,
											GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, 153,
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
												.addComponent(cbAlias, Alignment.LEADING, 0, 357,
													Short.MAX_VALUE)
												.addComponent(cbProvider, Alignment.LEADING, 0,
													357, Short.MAX_VALUE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(
											gl_contentPanel
												.createParallelGroup(Alignment.LEADING)
												.addComponent(btRefresh,
													GroupLayout.PREFERRED_SIZE, 41,
													GroupLayout.PREFERRED_SIZE)
												.addComponent(btAddProvider,
													GroupLayout.PREFERRED_SIZE, 41,
													GroupLayout.PREFERRED_SIZE))))
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
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(btRefresh, GroupLayout.PREFERRED_SIZE, 25,
								GroupLayout.PREFERRED_SIZE))
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

		setName("firma.dlg.sign_document");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 450, 292);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		cbProvider.setPreferredSize(new Dimension(0, 24));
		cbAlias.setPreferredSize(new Dimension(0, 24));
		browserPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		cbAppearance.setPreferredSize(new Dimension(0, 24));
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
			cbProvider, btAddProvider, cbAlias, btRefresh, btCertificateInfo, cbAppearance }));

		context.getResourceMap().injectComponents(this);

	}

}
