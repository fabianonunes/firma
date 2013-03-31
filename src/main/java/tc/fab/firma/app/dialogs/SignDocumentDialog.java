package tc.fab.firma.app.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.util.ArrayList;
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
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.eclipse.wb.swing.FocusTraversalOnArray;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingbinding.JComboBoxBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXImageView;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.app.AppDocument;
import tc.fab.firma.FirmaOptions;
import tc.fab.firma.app.components.ComponentsInputBlocker;
import tc.fab.firma.app.tasks.PreviewTask;
import tc.fab.mechanisms.MechanismManager;
import tc.fab.pdf.signer.options.AppearanceOptions;
import tc.fab.pdf.signer.options.ReferencePosition;

import com.google.inject.Provider;

@Singleton
public class SignDocumentDialog extends JDialog {

	private static final long serialVersionUID = 7850839445605448945L;

	private static final String ACTION_SIGN = "firma.dlg.sign_document.sign";
	private static final String ACTION_FILL_ALIASES = "firma.dlg.sign_document.fill_aliases";
	private static final String ACTION_ADD_PROVIDER = "firma.dlg.sign_document.add_provider";
	private static final String ACTION_ADD_APPEARANCE = "firma.dlg.sign_document.add_appearance";
	private static final String ACTION_DEL_APPEARANCE = "firma.dlg.sign_document.del_appearance";
	private static final String ACTION_PREVIEW_APPEARANCE = "firma.dlg.sign_document.preview";

	@Inject
	private Provider<AppearanceDialog> appearanceDialog;

	private AppContext context;
	private AppController controller;
	private FirmaOptions options;
	private MechanismManager providerManager;

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
		cbAppearance.setAction(context.getAction(this, ACTION_PREVIEW_APPEARANCE));

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

		controller.signFiles(provider, alias, getAppearanceOptions());

	}

	@Action(name = ACTION_PREVIEW_APPEARANCE)
	public Task<BufferedImage, Void> preview() throws KeyStoreException {

		String selected = getAlias();
		AppearanceOptions options = getAppearanceOptions();
		String waiting = context.getResReader().getString("firma.dlg.waiting");

		if (selected != null && !selected.equals(waiting)) {

			Certificate cert = providerManager.getCertificate(getProvider(), selected);

			PreviewTask task = new PreviewTask(context.getAppContext().getApplication(), cert,
				imagePane, options);

			task.setInputBlocker(ComponentsInputBlocker.builder(task, btOk, cbProvider, cbAlias,
				cbAppearance));

			return task;

		}

		imagePane.setImage((BufferedImage) null);
		return null;
		// context.getAppContext().getApplication()

	}

	@Action(name = ACTION_FILL_ALIASES)
	public Task<Void, String> fillAliases() {

		Task<Void, String> task = new FillAliasesTask(getProvider());
		task.setInputBlocker(ComponentsInputBlocker.builder(task, btOk, cbAlias));

		return task;

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

	@Action(name = ACTION_DEL_APPEARANCE)
	public void delAppearance() {
		AppearanceOptions selected = cbAppearance.getItemAt(cbAppearance.getSelectedIndex());
		int index = options.getAppearances().indexOf(selected);
		options.getAppearances().remove(index);
		cbAppearance.setSelectedIndex(0);
	}

	@Action(name = ACTION_ADD_APPEARANCE)
	public void addAppearance() {

		List<String> names = new ArrayList<>();

		for (AppearanceOptions opts : options.getAppearances()) {
			names.add(opts.getName());
		}

		String baseName = context.getResReader().getString("firma.msg.new_appearance");

		AppearanceOptions iOptions = new AppearanceOptions();
		iOptions.setName(pickName(names, baseName));
		iOptions = appearanceDialog.get().open(iOptions);

		if (iOptions != null) {
			iOptions.setName(pickName(names, iOptions.getName()));
			options.getAppearances().add(iOptions);
			cbAppearance.setSelectedItem(iOptions);
		}

	}

	private String pickName(List<String> names, String baseName) {

		String name = baseName;
		int i = 1;
		while (names.contains(name)) {
			name = baseName + " (" + i + ")";
			i = i + 1;
		}

		return name;

	}

	public void open() {
		setLocationRelativeTo(this.context.getMainFrame());
		setVisible(true);
	}

	public void initComponents() {

		contentPanel = new JPanel();
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

		JButton btAddApearance = new JButton();
		btAddApearance.setMinimumSize(new Dimension(108, 22));
		btAddApearance.setPreferredSize(new Dimension(0, 24));
		btAddApearance.setAction(context.getAction(this, ACTION_ADD_APPEARANCE));

		btDelAppearance = new JButton();
		btDelAppearance.setAction(context.getAction(this, ACTION_DEL_APPEARANCE));
		btDelAppearance.setPreferredSize(new Dimension(0, 24));
		btDelAppearance.setMinimumSize(new Dimension(108, 22));

		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel
			.setHorizontalGroup(gl_contentPanel
				.createParallelGroup(Alignment.TRAILING)
				.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 513, Short.MAX_VALUE)
				.addGroup(
					Alignment.LEADING,
					gl_contentPanel
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(btCertificateInfo, GroupLayout.PREFERRED_SIZE, 94,
							GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
						.addComponent(cbAppearance, GroupLayout.PREFERRED_SIZE, 219,
							GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btAddApearance, GroupLayout.PREFERRED_SIZE, 42,
							GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btDelAppearance, GroupLayout.PREFERRED_SIZE, 42,
							GroupLayout.PREFERRED_SIZE).addContainerGap())
				.addGroup(
					gl_contentPanel
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
							gl_contentPanel
								.createParallelGroup(Alignment.LEADING)
								.addComponent(imagePane, GroupLayout.DEFAULT_SIZE, 489,
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
												.addComponent(cbAlias, Alignment.LEADING, 0, 430,
													Short.MAX_VALUE)
												.addComponent(cbProvider, Alignment.LEADING, 0,
													430, Short.MAX_VALUE))
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
										.addComponent(reference, GroupLayout.DEFAULT_SIZE, 345,
											Short.MAX_VALUE))).addContainerGap())
				.addGroup(
					gl_contentPanel.createSequentialGroup().addContainerGap()
						.addComponent(lblNewLabel).addContainerGap(489, Short.MAX_VALUE)));
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
							.createParallelGroup(Alignment.LEADING)
							.addComponent(btCertificateInfo, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(btDelAppearance, GroupLayout.PREFERRED_SIZE, 24,
								GroupLayout.PREFERRED_SIZE)
							.addComponent(btAddApearance, GroupLayout.PREFERRED_SIZE, 24,
								GroupLayout.PREFERRED_SIZE)
							.addComponent(cbAppearance, GroupLayout.PREFERRED_SIZE,
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
		setBounds(100, 100, 523, 355);
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
		initSecondaryDataBindings();

	}

	/**
	 * Se essas bindings forem realizadas no método initDataBindings, o editor
	 * de bindings as apaga
	 */
	private void initSecondaryDataBindings() {
		@SuppressWarnings("rawtypes")
		JComboBoxBinding<AppearanceOptions, List<AppearanceOptions>, JComboBox> cb = SwingBindings
			.createJComboBoxBinding(UpdateStrategy.READ, options.getAppearances(), cbAppearance);
		cb.bind();

		BeanProperty<JComboBox<AppearanceOptions>, AppearanceOptions> selectedItem = BeanProperty
			.create("selectedItem");

		BeanProperty<JButton, Boolean> enabledProperty = BeanProperty.create("enabled");

		AutoBinding<JComboBox<AppearanceOptions>, AppearanceOptions, JButton, Boolean> autoBinding_3 = Bindings
			.createAutoBinding(UpdateStrategy.READ, cbAppearance, selectedItem, btDelAppearance,
				enabledProperty);
		autoBinding_3.setConverter(new Converter<AppearanceOptions, Boolean>() {

			@Override
			public AppearanceOptions convertReverse(Boolean value) {
				return null;
			}

			@Override
			public Boolean convertForward(AppearanceOptions value) {
				boolean ret = cbAppearance.getItemAt(0) != value;
				return ret;
			}
		});
		autoBinding_3.bind();
	}

	private JPanel contentPanel;
	private JComboBox<String> cbAlias;
	private JComboBox<String> cbProvider;
	private JComboBox<AppearanceOptions> cbAppearance;
	private JButton btOk;
	private JLabel lblAssinarComo;
	private JXImageView imagePane;
	private JTextField reference;
	private JComboBox<ReferencePosition> referencePosition;
	private JButton btDelAppearance;

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

	protected void initDataBindings() {
		ELProperty<JComboBox<String>, Object> jComboBoxEvalutionProperty = ELProperty
			.create("${selectedItem!=null}");
		BeanProperty<JButton, Boolean> jButtonBeanProperty = BeanProperty.create("enabled");
		AutoBinding<JComboBox<String>, Object, JButton, Boolean> autoBinding = Bindings
			.createAutoBinding(UpdateStrategy.READ, cbAlias, jComboBoxEvalutionProperty, btOk,
				jButtonBeanProperty);
		autoBinding.bind();
		//
		BeanProperty<FirmaOptions, String> firmaOptionsBeanProperty = BeanProperty
			.create("referenceText");
		BeanProperty<JTextField, String> jTextFieldBeanProperty = BeanProperty
			.create("text_ON_FOCUS_LOST");
		AutoBinding<FirmaOptions, String, JTextField, String> autoBinding_1 = Bindings
			.createAutoBinding(UpdateStrategy.READ_WRITE, options, firmaOptionsBeanProperty,
				reference, jTextFieldBeanProperty);
		autoBinding_1.bind();
		//
		BeanProperty<FirmaOptions, ReferencePosition> firmaOptionsBeanProperty_1 = BeanProperty
			.create("referencePosition");
		BeanProperty<JComboBox<ReferencePosition>, Object> jComboBoxBeanProperty = BeanProperty
			.create("selectedItem");
		AutoBinding<FirmaOptions, ReferencePosition, JComboBox<ReferencePosition>, Object> autoBinding_2 = Bindings
			.createAutoBinding(UpdateStrategy.READ_WRITE, options, firmaOptionsBeanProperty_1,
				referencePosition, jComboBoxBeanProperty);
		autoBinding_2.bind();
		//

	}

}
