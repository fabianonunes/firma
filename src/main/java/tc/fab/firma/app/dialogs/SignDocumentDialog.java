package tc.fab.firma.app.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Signature;
import java.security.cert.Certificate;
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
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.apache.commons.codec.binary.Hex;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingx.JXImageView;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.app.AppDocument;
import tc.fab.firma.FirmaOptions;
import tc.fab.mechanisms.Mechanism;
import tc.fab.mechanisms.MechanismManager;
import tc.fab.mechanisms.callback.PINCallback.UserCancelledException;
import tc.fab.pdf.signer.SignaturePreview;
import tc.fab.pdf.signer.application.ComponentsInputBlocker;

public class SignDocumentDialog extends JDialog {

	private static final long serialVersionUID = 7850839445605448945L;

	private static final String ACTION_FILL_ALIASES = "firma.dlg.sign_document.fill_aliases";
	private static final String ACTION_ADD_PROVIDER = "firma.dlg.sign_document.add_provider";
	private static final String ACTION_PREVIEW = "firma.dlg.sign_document.preview";
	private static final String ACTION_SIGN = "firma.dlg.sign_document.sign";

	private AppContext context;
	@SuppressWarnings("unused")
	private AppController controller;
	private FirmaOptions options;

	private final JPanel contentPanel = new JPanel();

	private JComboBox<String> cbAlias;
	private JComboBox<String> cbProvider;
	private JComboBox<String> cbRenderMode;
	private MechanismManager providerManager;

	private JButton btOk;
	private JLabel lblAssinarComo;

	private JXImageView imagePane;
	private JTextField textField;

	@Inject
	public SignDocumentDialog(AppContext context, AppController controller, AppDocument document,
		MechanismManager providersManager) throws IOException {

		super(context.getMainFrame(), true);

		// setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.context = context;
		this.controller = controller;
		this.providerManager = providersManager;
		this.options = document.getOptions();

		initComponents();

		fillProviders();
		// the action setup must be after initial fulfillment to avoid double
		// fire
		cbProvider.setAction(context.getAction(this, ACTION_FILL_ALIASES));
		cbAlias.setAction(context.getAction(this, ACTION_PREVIEW));

	}

	public String getProvider() {
		return cbProvider.getItemAt(cbProvider.getSelectedIndex());
	}

	public String getAlias() {
		return cbAlias.getItemAt(cbAlias.getSelectedIndex());
	}

	@Action(name = ACTION_SIGN)
	public void sign() throws InvalidKeyException, Exception {

		String alias = getAlias();
		String provider = getProvider();

		try (Mechanism m = providerManager.getMechanism(provider, alias)) {
			try {
				m.login();
			} catch (UserCancelledException e) {
				return;
			}

			options.setAlias(alias);
			options.setProvider(provider);

			byte[] dataToSign = "fabiano nunes parente".getBytes();

			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(m.getPrivateKey());
			signature.update(dataToSign);

			byte[] data_signed = signature.sign();
			System.out.println(Hex.encodeHex(data_signed));

			signature = Signature.getInstance("SHA1withRSA");
			signature.initVerify(m.getCertificate());
			signature.update(dataToSign);

			System.out.println(signature.verify(data_signed));

			setVisible(false);

		}

	}

	@Action(name = ACTION_PREVIEW)
	public Task<BufferedImage, Void> preview() {
		String selected = getAlias();
		if (selected != null
			&& !selected.equals(context.getResReader().getString("firma.dlg.waiting"))) {
			PreviewTask task = new PreviewTask(selected);
			task.setInputBlocker(ComponentsInputBlocker.builder(task, btOk, cbProvider, cbAlias));
			return task;
		}
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
			SignaturePreview preview = new SignaturePreview(cert, imagePane.getSize());
			return preview.getImagePreview();
		}

		@Override
		protected void succeeded(BufferedImage result) {
			imagePane.setImage(result);
		}

		@Override
		protected void failed(Throwable cause) {
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
			// cbAlias.setEnabled(false);
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
			// cbAlias.setEnabled(true);
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
		for (String lib : libs) {
			cbProvider.addItem(lib);
		}
		if (options.getProvider() != null) {
			cbProvider.setSelectedItem(options.getProvider());
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

		cbRenderMode = new JComboBox<>();
		cbRenderMode.setPreferredSize(new Dimension(0, 24));

		imagePane = new JXImageView();
		imagePane.setEditable(false);
		imagePane.setDragEnabled(false);
		imagePane.setBorder(new LineBorder(Color.LIGHT_GRAY));

		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setName("firma.dlg.sign_document.location");

		textField = new JTextField();
		textField.setPreferredSize(new Dimension(0, 24));
		textField.setColumns(10);

		JSeparator separator_1 = new JSeparator();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel
			.setHorizontalGroup(gl_contentPanel
				.createParallelGroup(Alignment.TRAILING)
				.addComponent(separator_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 498,
					Short.MAX_VALUE)
				.addGroup(
					Alignment.LEADING,
					gl_contentPanel
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(btCertificateInfo, GroupLayout.PREFERRED_SIZE, 101,
							GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 235, Short.MAX_VALUE)
						.addComponent(cbRenderMode, GroupLayout.PREFERRED_SIZE, 138,
							GroupLayout.PREFERRED_SIZE).addContainerGap())
				.addGroup(
					gl_contentPanel
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
							gl_contentPanel
								.createParallelGroup(Alignment.TRAILING)
								.addComponent(imagePane, Alignment.LEADING,
									GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
								.addGroup(
									Alignment.LEADING,
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
												.addComponent(cbAlias, Alignment.LEADING, 0, 355,
													Short.MAX_VALUE)
												.addComponent(cbProvider, Alignment.LEADING, 0,
													355, Short.MAX_VALUE))
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
								.addComponent(textField, GroupLayout.DEFAULT_SIZE, 414,
									Short.MAX_VALUE).addComponent(lblNewLabel, Alignment.LEADING))
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
					.addComponent(imagePane, GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(
						gl_contentPanel
							.createParallelGroup(Alignment.TRAILING)
							.addComponent(btCertificateInfo, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(cbRenderMode, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 2,
						GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE)));

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
		ELProperty<JComboBox<String>, Object> jComboBoxEvalutionProperty = ELProperty
			.create("${selectedItem!=null}");
		BeanProperty<JButton, Boolean> jButtonBeanProperty = BeanProperty.create("enabled");
		AutoBinding<JComboBox<String>, Object, JButton, Boolean> autoBinding = Bindings
			.createAutoBinding(UpdateStrategy.READ, cbAlias, jComboBoxEvalutionProperty, btOk,
				jButtonBeanProperty);
		autoBinding.bind();
		//
		// BeanProperty<JXImageView, Image> jXImageViewBeanProperty =
		// BeanProperty.create("image");
		// BeanProperty<JComboBox<String>, String> jComboBoxBeanProperty =
		// BeanProperty
		// .create("selectedItem");
		// AutoBinding<JComboBox<String>, String, JXImageView, Image>
		// autoBinding_1 = Bindings
		// .createAutoBinding(UpdateStrategy.READ, cbAlias,
		// jComboBoxBeanProperty, imagePane,
		// jXImageViewBeanProperty);
		//
		// autoBinding_1.setConverter(new AliasToPreviewConverter());
		// autoBinding_1.bind();
	}

	class AliasToPreviewConverter extends Converter<String, Image> {
		@Override
		public Image convertForward(String alias) {
			try {
				Certificate cert = providerManager.getCertificate(cbProvider.getSelectedItem()
					.toString(), alias);
				SignaturePreview preview = new SignaturePreview(cert, imagePane.getSize());
				return preview.getImagePreview();
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		public String convertReverse(Image value) {
			return null;
		}

	}
}
