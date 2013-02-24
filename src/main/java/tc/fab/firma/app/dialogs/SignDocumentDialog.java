package tc.fab.firma.app.dialogs;

import iaik.pkcs.pkcs11.TokenException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.fit.cssbox.swingbox.BrowserPane;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;
import org.jdesktop.application.Task.BlockingScope;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.app.AppDocument;
import tc.fab.firma.FirmaOptions;
import tc.fab.mechanisms.TokenInfo;

@Singleton
public class SignDocumentDialog extends JDialog {

	private static final long serialVersionUID = 7850839445605448945L;

	private static final String ACTION_FILL_ALIASES = "firma.dlg.sign.fill_aliases";

	private AppContext context;
	private AppController controller;

	private final JPanel contentPanel = new JPanel();

	private JComboBox<String> cbAlias;
	private JComboBox<String> cbProvider;
	private JComboBox<String> cbAppearance;

	private JButton btAddProvider;
	private JButton btOk;

	@Inject
	public SignDocumentDialog(AppContext context, AppController controller, AppDocument document) {

		super(context.getMainFrame(), true);

		this.context = context;
		this.controller = controller;

		FirmaOptions options = document.getOptions();

		initComponents();
		context.getResourceMap().injectComponents(this);

		List<String> libs = options.getLibs();

		if (libs.size() > 0) {
			fillProviders(libs);
			context.fireAction(this, ACTION_FILL_ALIASES);
		}

		cbProvider.setAction(context.getAction(this, ACTION_FILL_ALIASES));

	}

	@Action(name = ACTION_FILL_ALIASES, block = BlockingScope.ACTION)
	public Task<Void, Void> fillAliases() {
		cbAlias.removeAllItems();
		cbAlias.addItem("aguarde...");
		cbAlias.setEnabled(false);
		return new FillAliasesTask();
	}

	class FillAliasesTask extends Task<Void, Void> {
		public FillAliasesTask() {
			super(context.getAppContext().getApplication());
		}

		@Override
		protected Void doInBackground() throws Exception {
			String lib = cbProvider.getItemAt(cbProvider.getSelectedIndex());
			ArrayList<String> aliases;
			try {
				aliases = TokenInfo.getAliases(lib);
				cbAlias.removeAllItems();
				for (String alias : aliases) {
					cbAlias.addItem(alias);
				}
			} catch (IOException | TokenException e) {
				cbAlias.removeAllItems();
			} finally {
				cbAlias.setEnabled(true);
			}
			return null;
		}
	}

	private void fillProviders(List<String> libs) {
		for (String lib : libs) {
			cbProvider.addItem(lib);
		}
	}

	public void open() {
		setLocationRelativeTo(this.context.getMainFrame());
		setVisible(true);
	}

	public void initComponents() {

		ActionMap actionMap = controller.getActionMap();

		btOk = new JButton();
		cbAlias = new JComboBox<String>();
		cbAppearance = new JComboBox<String>();
		cbProvider = new JComboBox<String>();
		btAddProvider = new JButton("i");
		btOk.setAction(actionMap.get(AppController.ACTION_FILE_PREVIEW));

		JButton btCancel = new JButton();
		btCancel.setName("firma.dlg.sign_document.cancel");

		JLabel lblTipoDoCertificado = new JLabel();
		lblTipoDoCertificado.setName("firma.dlg.sign_document.provider");

		JLabel lblAssinarComo = new JLabel();
		lblAssinarComo.setName("firma.dlg.sign_document.alias");

		JButton btCertificateInfo = new JButton();
		btCertificateInfo.setName("firma.dlg.sign_document.info");

		BrowserPane browserPane = new BrowserPane();
		JSeparator separator = new JSeparator();
		JSeparator separator_1 = new JSeparator();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);

		setName("firma.dlg.sign_document");
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
		btCertificateInfo.setPreferredSize(new Dimension(80, 22));
		btAddProvider.setMinimumSize(new Dimension(108, 22));
		btAddProvider.setPreferredSize(new Dimension(108, 22));
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
				buttonPane.add(btCancel);
			}
		}
	}

}
