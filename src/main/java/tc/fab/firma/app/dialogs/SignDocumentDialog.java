package tc.fab.firma.app.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.cert.Certificate;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.eclipse.wb.swing.FocusTraversalOnArray;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingx.JXImageView;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.app.AppDocument;
import tc.fab.firma.FirmaOptions;
import tc.fab.mechanisms.MechanismManager;
import tc.fab.pdf.signer.SignaturePreview;
import tc.fab.pdf.signer.application.ComponentsInputBlocker;
import tc.fab.pdf.signer.options.AppearanceOptions;
import tc.fab.pdf.signer.options.ReferencePosition;

@Singleton
public class SignDocumentDialog extends JDialog {

	private static final long serialVersionUID = 7850839445605448945L;

	private static final String ACTION_FILL_ALIASES = "firma.dlg.sign_document.fill_aliases";
	private static final String ACTION_ADD_PROVIDER = "firma.dlg.sign_document.add_provider";
	private static final String ACTION_PREVIEW_APPEARANCE = "firma.dlg.sign_document.preview";
	private static final String ACTION_SIGN = "firma.dlg.sign_document.sign";

	private AppContext context;
	private AppController controller;
	private FirmaOptions options;

	private final JPanel contentPanel = new JPanel();

	private JComboBox<String> cbAlias;
	private JComboBox<String> cbProvider;
	private JComboBox<AppearanceOptions> cbAppearance;
	private MechanismManager providerManager;

	private JButton btOk;
	private JLabel lblAssinarComo;

	private JXImageView imagePane;
	private JTextField reference;
	private JComboBox<ReferencePosition> referencePosition;

	@Inject
	public SignDocumentDialog(AppContext context, AppController controller, AppDocument document,
		MechanismManager providersManager) throws IOException {

		super(context.getMainFrame(), true);

		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		this.context = context;
		this.controller = controller;
		this.providerManager = providersManager;
		this.options = document.getOptions();

		initComponents();
		fillProviders();
		// the action setup must be after initial fulfillment to avoid double
		// fire
		cbAlias.setAction(context.getAction(this, ACTION_PREVIEW_APPEARANCE));

	}

	public String getProvider() {
		return cbProvider.getItemAt(cbProvider.getSelectedIndex());
	}

	public String getAlias() {
		return cbAlias.getItemAt(cbAlias.getSelectedIndex());
	}

	public AppearanceOptions getAppearanceOptions() {
		return cbAppearance.getItemAt(cbAppearance.getSelectedIndex());
	}

	@Action(name = ACTION_SIGN)
	public void sign() throws InvalidKeyException, Exception {

		String alias = getAlias();
		String provider = getProvider();

		setVisible(false);

		options.setAlias(alias);
		options.setProvider(provider);

		controller.signFiles(provider, alias, options.getAppearance());

		// try (Mechanism m = providerManager.getMechanism(provider, alias)) {
		// try {
		// m.login();
		// } catch (UserCancelledException e) {
		// return;
		// }
		//
		// byte[] dataToSign = "fabiano nunes parente".getBytes();
		//
		// Signature signature = Signature.getInstance("SHA1withRSA");
		// signature.initSign(m.getPrivateKey());
		// signature.update(dataToSign);
		//
		// byte[] data_signed = signature.sign();
		// System.out.println(Hex.encodeHex(data_signed));
		//
		// signature = Signature.getInstance("SHA1withRSA");
		// signature.initVerify(m.getCertificate());
		// signature.update(dataToSign);
		//
		// System.out.println(signature.verify(data_signed));
		//
		// setVisible(false);
		//
		// }

	}

	@Action(name = ACTION_PREVIEW_APPEARANCE)
	public Task<BufferedImage, Void> preview() {

		System.out.println("preview");

		String selected = getAlias();
		String waiting = context.getResReader().getString("firma.dlg.waiting");

		if (selected != null && !selected.equals(waiting)) {
			PreviewTask task = new PreviewTask(selected);
			task.setInputBlocker(ComponentsInputBlocker.builder(task, btOk, cbProvider, cbAlias));
			return task;
		}

		imagePane.setImage((BufferedImage) null);
		return null;

	}

	class PreviewTask extends Task<BufferedImage, Void> {

		private String alias;

		public PreviewTask(String alias) {
			super(context.getAppContext().getApplication());
			this.alias = alias;
		}

		@Override
		protected BufferedImage doInBackground() throws Exception {
			Certificate cert = providerManager.getCertificate(getProvider(), alias);
			return SignaturePreview.generate(cert, imagePane.getSize(), getAppearanceOptions());
		}

		@Override
		protected void succeeded(BufferedImage result) {
			imagePane.setImage(result);
		}

		@Override
		protected void failed(Throwable cause) {
			cause.printStackTrace();
			imagePane.setImage((BufferedImage) null);
		}

	}

	@Action(name = ACTION_FILL_ALIASES)
	public Task<Void, String> fillAliases() {

		Task<Void, String> task = new FillAliasesTask(getProvider());
		task.setInputBlocker(ComponentsInputBlocker.builder(task, btOk, cbAlias));

		return task;

	}

	class FillAliasesTask extends Task<Void, String> {

		private String provider;
		private List<String> aliases;

		public FillAliasesTask(String provider) {
			super(context.getAppContext().getApplication());
			this.provider = provider;
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
			if (options.getAlias() != null) {
				cbAlias.setSelectedItem(options.getAlias());
			}
		}

		@Override
		protected void finished() {
			if (cbAlias.getItemCount() > 0) {
				cbAlias.removeItemAt(0);
			}
		}

	}

	private void fillProviders() {

		List<String> libs = providerManager.getAvaliableMechanisms();

		cbProvider.setModel(new DefaultComboBoxModel<>(libs.toArray(new String[] {})));
		cbProvider.setAction(context.getAction(this, ACTION_FILL_ALIASES));

		if (options.getProvider() != null) {
			cbProvider.setSelectedItem(options.getProvider());
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

		cbAlias = new JComboBox<>();
		cbProvider = new JComboBox<>();

		btOk = new JButton();
		JButton btAddProvider = new JButton();
		JButton btRefresh = new JButton();

		btOk.setAction(context.getAction(this, ACTION_SIGN));
		btRefresh.setAction(context.getAction(this, ACTION_FILL_ALIASES));
		btAddProvider.setAction(context.getAction(this, ACTION_ADD_PROVIDER));

		JButton btCancel = new JButton();
		btCancel.setName("firma.dlg.sign_document.cancel");

		JLabel lblTipoDoCertificado = new JLabel();
		lblTipoDoCertificado.setName("firma.dlg.sign_document.provider");

		lblAssinarComo = new JLabel();
		lblAssinarComo.setName("firma.dlg.sign_document.alias");

		JButton btCertificateInfo = new JButton();
		btCertificateInfo.setName("firma.dlg.sign_document.info");

		getRootPane().setDefaultButton(btOk);

		btRefresh.setPreferredSize(new Dimension(0, 24));
		btRefresh.setMinimumSize(new Dimension(108, 22));

		cbAppearance = new JComboBox<>();

		cbAppearance.setModel(new DefaultComboBoxModel<AppearanceOptions>(options.getAppearances()
			.toArray(new AppearanceOptions[] {})));

		cbAppearance.setPreferredSize(new Dimension(0, 24));

		imagePane = new JXImageView();
		imagePane.setEditable(false);
		imagePane.setDragEnabled(false);
		imagePane.setBorder(new LineBorder(Color.LIGHT_GRAY));

		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setText("tt");
		lblNewLabel.setName("firma.dlg.sign_document.location");

		reference = new JTextField();
		reference.setPreferredSize(new Dimension(0, 24));
		reference.setColumns(10);

		JSeparator separator_1 = new JSeparator();

		referencePosition = new JComboBox<>();
		referencePosition.setModel(new DefaultComboBoxModel<ReferencePosition>(ReferencePosition
			.values()));
		referencePosition.setPreferredSize(new Dimension(0, 24));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel
			.setHorizontalGroup(gl_contentPanel
				.createParallelGroup(Alignment.LEADING)
				.addComponent(separator_1, GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
				.addGroup(
					Alignment.TRAILING,
					gl_contentPanel
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(btCertificateInfo, GroupLayout.PREFERRED_SIZE, 94,
							GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
						.addComponent(cbAppearance, GroupLayout.PREFERRED_SIZE, 138,
							GroupLayout.PREFERRED_SIZE).addContainerGap())
				.addGroup(
					gl_contentPanel
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
							gl_contentPanel
								.createParallelGroup(Alignment.LEADING)
								.addComponent(imagePane, GroupLayout.DEFAULT_SIZE, 474,
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
												.addComponent(cbAlias, Alignment.LEADING, 0, 415,
													Short.MAX_VALUE)
												.addComponent(cbProvider, Alignment.LEADING, 0,
													415, Short.MAX_VALUE))
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
									gl_contentPanel
										.createSequentialGroup()
										.addComponent(referencePosition,
											GroupLayout.PREFERRED_SIZE, 138,
											GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(reference, GroupLayout.DEFAULT_SIZE, 330,
											Short.MAX_VALUE))).addContainerGap())
				.addGroup(
					gl_contentPanel.createSequentialGroup().addContainerGap()
						.addComponent(lblNewLabel).addContainerGap(474, Short.MAX_VALUE)));
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
					.addComponent(imagePane, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(
						gl_contentPanel
							.createParallelGroup(Alignment.TRAILING)
							.addComponent(cbAppearance, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(btCertificateInfo, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 2,
						GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(
						gl_contentPanel
							.createParallelGroup(Alignment.BASELINE)
							.addComponent(reference, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(referencePosition, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))));

		setName("firma.dlg.sign_document");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 510, 355);
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
		initDataBindings();

	}

	protected void initDataBindings() {
		//
		ELProperty<JComboBox<String>, Object> jComboBoxEvalutionProperty = ELProperty
			.create("${selectedItem!=null}");
		BeanProperty<JButton, Boolean> jButtonBeanProperty = BeanProperty.create("enabled");
		AutoBinding<JComboBox<String>, Object, JButton, Boolean> autoBinding = Bindings
			.createAutoBinding(UpdateStrategy.READ, cbAlias, jComboBoxEvalutionProperty, btOk,
				jButtonBeanProperty);
		autoBinding.bind();

		//
		BeanProperty<FirmaOptions, String> firmaOptionsBeanProperty = BeanProperty
			.create("appearance.referenceText");
		BeanProperty<JTextField, String> jTextFieldBeanProperty = BeanProperty
			.create("text_ON_FOCUS_LOST");
		AutoBinding<FirmaOptions, String, JTextField, String> autoBinding_1 = Bindings
			.createAutoBinding(UpdateStrategy.READ_WRITE, options, firmaOptionsBeanProperty,
				reference, jTextFieldBeanProperty);
		autoBinding_1.bind();

		//
		BeanProperty<FirmaOptions, ReferencePosition> firmaOptionsBeanProperty_1 = BeanProperty
			.create("appearance.referencePosition");
		BeanProperty<JComboBox<ReferencePosition>, Object> jComboBoxBeanProperty = BeanProperty
			.create("selectedItem");
		AutoBinding<FirmaOptions, ReferencePosition, JComboBox<ReferencePosition>, Object> autoBinding_2 = Bindings
			.createAutoBinding(UpdateStrategy.READ_WRITE, options, firmaOptionsBeanProperty_1,
				referencePosition, jComboBoxBeanProperty);
		autoBinding_2.bind();

	}
}
