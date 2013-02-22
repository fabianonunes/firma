package tc.fab.pdf.signer.options;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;

public class SignerOptionDialog extends javax.swing.JDialog {

	private static final long serialVersionUID = 1L;
	private String groupAlignValue = "center";
	private SignerOptions options;
	private BindingGroup bindingGroup = new BindingGroup();
	private ResourceMap resourceMap;
	private ApplicationActionMap actionMap;

	public SignerOptionDialog(java.awt.Frame parent, boolean modal) {

		super(parent, modal);

		resourceMap = org.jdesktop.application.Application.getInstance().getContext()
			.getResourceMap(SignerOptionDialog.class);

		actionMap = org.jdesktop.application.Application.getInstance().getContext()
			.getActionMap(SignerOptionDialog.class, this);

		initComponents();
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(toolbar, BorderLayout.NORTH);

		cards = new JPanel();
		getContentPane().add(cards);
		cards.setLayout(new CardLayout(0, 0));

		panelAppearance = new JPanel();
		cards.add(panelAppearance, "name_88672580169960");
		panelPosition = new javax.swing.JPanel();
		spinnerPage = new javax.swing.JSpinner();
		labelHelp = new javax.swing.JLabel();
		spinnerMarginBottom = new javax.swing.JSpinner();
		labelPage = new javax.swing.JLabel();
		labelFooter = new javax.swing.JLabel();
		labelCm = new javax.swing.JLabel();
		panelAlign = new javax.swing.JPanel();
		toggleLeft = new javax.swing.JToggleButton();
		toggleCenter = new javax.swing.JToggleButton();
		toggleRight = new javax.swing.JToggleButton();
		panelTipo = new javax.swing.JPanel();
		labelPosition = new javax.swing.JLabel();
		radioAbsolute = new javax.swing.JRadioButton();
		labelFooterDistance = new javax.swing.JLabel();
		labelLastLineDistance = new javax.swing.JLabel();
		spinnerFooterDistance = new javax.swing.JSpinner();
		spinnerLastLineDistance = new javax.swing.JSpinner();
		textReference = new javax.swing.JTextField();
		labelReference = new javax.swing.JLabel();
		radioRelative = new javax.swing.JRadioButton();
		checkLastLine = new javax.swing.JCheckBox();

		panelPosition.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
			resourceMap.getString("panelPosition.border.title"),
			javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
			javax.swing.border.TitledBorder.DEFAULT_POSITION,
			resourceMap.getFont("panelPosition.border.titleFont"))); // NOI18N
		panelPosition.setName("panelPosition"); // NOI18N

		spinnerPage.setModel(new javax.swing.SpinnerNumberModel());
		spinnerPage.setName("spinnerPage"); // NOI18N

		labelHelp.setText(resourceMap.getString("labelHelp.text")); // NOI18N
		labelHelp.setName("labelHelp"); // NOI18N

		spinnerMarginBottom.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), null,
			null, Float.valueOf(0.1f)));
		spinnerMarginBottom.setName("spinnerMarginBottom"); // NOI18N

		labelPage.setText(resourceMap.getString("labelPage.text")); // NOI18N
		labelPage.setName("labelPage"); // NOI18N
		labelPage.setPreferredSize(new java.awt.Dimension(32, 24));

		labelFooter.setText(resourceMap.getString("labelFooter.text")); // NOI18N
		labelFooter.setName("labelFooter"); // NOI18N
		labelFooter.setPreferredSize(new java.awt.Dimension(32, 24));

		labelCm.setText(resourceMap.getString("labelCm.text")); // NOI18N
		labelCm.setName("labelCm"); // NOI18N

		panelAlign.setName("panelAlign"); // NOI18N

		groupAlign.add(toggleLeft);
		toggleLeft.setIcon(resourceMap.getIcon("toggleLeft.icon")); // NOI18N
		toggleLeft.setText(resourceMap.getString("toggleLeft.text")); // NOI18N
		toggleLeft.setActionCommand(resourceMap.getString("toggleLeft.actionCommand")); // NOI18N
		toggleLeft.setName("toggleLeft"); // NOI18N
		toggleLeft.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				alignActionPerformed(evt);
			}
		});

		groupAlign.add(toggleCenter);
		toggleCenter.setIcon(resourceMap.getIcon("toggleCenter.icon")); // NOI18N
		toggleCenter.setText(resourceMap.getString("toggleCenter.text")); // NOI18N
		toggleCenter.setActionCommand(resourceMap.getString("toggleCenter.actionCommand")); // NOI18N
		toggleCenter.setName("toggleCenter"); // NOI18N
		toggleCenter.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				alignActionPerformed(evt);
			}
		});

		groupAlign.add(toggleRight);
		toggleRight.setIcon(resourceMap.getIcon("toggleRight.icon")); // NOI18N
		toggleRight.setText(resourceMap.getString("toggleRight.text")); // NOI18N
		toggleRight.setActionCommand(resourceMap.getString("toggleRight.actionCommand")); // NOI18N
		toggleRight.setName("toggleRight"); // NOI18N
		toggleRight.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				alignActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout gl_panelAlign = new javax.swing.GroupLayout(panelAlign);
		panelAlign.setLayout(gl_panelAlign);
		gl_panelAlign.setHorizontalGroup(gl_panelAlign.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			gl_panelAlign
				.createSequentialGroup()
				.addComponent(toggleLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 24,
					javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(toggleCenter, javax.swing.GroupLayout.PREFERRED_SIZE, 24,
					javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(toggleRight, javax.swing.GroupLayout.PREFERRED_SIZE, 24,
					javax.swing.GroupLayout.PREFERRED_SIZE)));
		gl_panelAlign
			.setVerticalGroup(gl_panelAlign
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(toggleCenter, javax.swing.GroupLayout.PREFERRED_SIZE, 24,
					Short.MAX_VALUE)
				.addComponent(toggleRight, javax.swing.GroupLayout.PREFERRED_SIZE, 24,
					Short.MAX_VALUE).addComponent(toggleLeft, 0, 0, Short.MAX_VALUE));

		panelTipo.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
			resourceMap.getString("panelTipo.border.title"),
			javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
			javax.swing.border.TitledBorder.DEFAULT_POSITION,
			resourceMap.getFont("panelTipo.border.titleFont"))); // NOI18N
		panelTipo.setName("panelTipo"); // NOI18N

		labelPosition.setText(resourceMap.getString("labelPosition.text")); // NOI18N
		labelPosition.setName("labelPosition"); // NOI18N
		labelPosition.setPreferredSize(null);

		groupPosition.add(radioAbsolute);
		radioAbsolute.setText(resourceMap.getString("radioAbsolute.text")); // NOI18N
		radioAbsolute.setName("radioAbsolute"); // NOI18N
		radioAbsolute.setPreferredSize(new java.awt.Dimension(63, 24));

		labelFooterDistance.setText(resourceMap.getString("labelFooterDistance.text")); // NOI18N
		labelFooterDistance.setName("labelFooterDistance"); // NOI18N
		labelFooterDistance.setPreferredSize(null);

		labelLastLineDistance.setText(resourceMap.getString("labelLastLineDistance.text")); // NOI18N
		labelLastLineDistance.setName("labelLastLineDistance"); // NOI18N
		labelLastLineDistance.setPreferredSize(null);

		spinnerFooterDistance.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f),
			null, null, Float.valueOf(0.2f)));
		spinnerFooterDistance.setName("spinnerFooterDistance"); // NOI18N
		spinnerFooterDistance.setPreferredSize(null);

		spinnerLastLineDistance.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f),
			null, null, Float.valueOf(0.2f)));
		spinnerLastLineDistance.setName("spinnerLastLineDistance"); // NOI18N
		spinnerLastLineDistance.setPreferredSize(null);

		textReference.setText(resourceMap.getString("textReference.text")); // NOI18N
		textReference.setName("textReference"); // NOI18N

		labelReference.setText(resourceMap.getString("labelReference.text")); // NOI18N
		labelReference.setName("labelReference"); // NOI18N
		labelReference.setPreferredSize(null);

		groupPosition.add(radioRelative);
		radioRelative.setText(resourceMap.getString("radioRelative.text")); // NOI18N
		radioRelative.setName("radioRelative"); // NOI18N
		radioRelative.setPreferredSize(new java.awt.Dimension(32, 24));

		checkLastLine.setText(resourceMap.getString("checkLastLine.text")); // NOI18N
		checkLastLine.setName("checkLastLine"); // NOI18N

		javax.swing.GroupLayout gl_panelTipo = new javax.swing.GroupLayout(panelTipo);
		panelTipo.setLayout(gl_panelTipo);
		gl_panelTipo
			.setHorizontalGroup(gl_panelTipo
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
					gl_panelTipo
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
							gl_panelTipo
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
									false)
								.addComponent(labelPosition, javax.swing.GroupLayout.DEFAULT_SIZE,
									javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(labelFooterDistance,
									javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
								.addComponent(labelLastLineDistance,
									javax.swing.GroupLayout.DEFAULT_SIZE,
									javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(labelReference,
									javax.swing.GroupLayout.PREFERRED_SIZE,
									javax.swing.GroupLayout.DEFAULT_SIZE,
									javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
							gl_panelTipo
								.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addGroup(
									gl_panelTipo
										.createSequentialGroup()
										.addGroup(
											gl_panelTipo
												.createParallelGroup(
													javax.swing.GroupLayout.Alignment.LEADING,
													false)
												.addComponent(radioAbsolute,
													javax.swing.GroupLayout.DEFAULT_SIZE, 80,
													Short.MAX_VALUE)
												.addGroup(
													gl_panelTipo
														.createSequentialGroup()
														.addGroup(
															gl_panelTipo
																.createParallelGroup(
																	javax.swing.GroupLayout.Alignment.TRAILING,
																	false)
																.addComponent(
																	spinnerFooterDistance,
																	javax.swing.GroupLayout.Alignment.LEADING,
																	javax.swing.GroupLayout.DEFAULT_SIZE,
																	javax.swing.GroupLayout.DEFAULT_SIZE,
																	Short.MAX_VALUE)
																.addComponent(
																	spinnerLastLineDistance,
																	javax.swing.GroupLayout.Alignment.LEADING,
																	javax.swing.GroupLayout.DEFAULT_SIZE,
																	54, Short.MAX_VALUE))
														.addPreferredGap(
															javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
										.addGroup(
											gl_panelTipo
												.createParallelGroup(
													javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
													gl_panelTipo
														.createSequentialGroup()
														.addPreferredGap(
															javax.swing.LayoutStyle.ComponentPlacement.RELATED,
															36, Short.MAX_VALUE)
														.addComponent(checkLastLine))
												.addGroup(
													gl_panelTipo
														.createSequentialGroup()
														.addPreferredGap(
															javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(radioRelative,
															javax.swing.GroupLayout.PREFERRED_SIZE,
															104,
															javax.swing.GroupLayout.PREFERRED_SIZE))))
								.addComponent(textReference, javax.swing.GroupLayout.DEFAULT_SIZE,
									192, Short.MAX_VALUE)).addContainerGap()));
		gl_panelTipo.setVerticalGroup(gl_panelTipo.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			gl_panelTipo
				.createSequentialGroup()
				.addGroup(
					gl_panelTipo
						.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(labelPosition, javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(radioAbsolute, javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(radioRelative, javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(
					gl_panelTipo
						.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(labelFooterDistance, javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(spinnerFooterDistance,
							javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(
					gl_panelTipo
						.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(labelLastLineDistance,
							javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(spinnerLastLineDistance,
							javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(checkLastLine))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(
					gl_panelTipo
						.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(labelReference, javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(textReference, javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE))));

		javax.swing.GroupLayout gl_panelPosition = new javax.swing.GroupLayout(panelPosition);
		panelPosition.setLayout(gl_panelPosition);
		gl_panelPosition.setHorizontalGroup(gl_panelPosition.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(
				gl_panelPosition
					.createSequentialGroup()
					.addContainerGap()
					.addGroup(
						gl_panelPosition
							.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addComponent(panelTipo, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(
								gl_panelPosition
									.createSequentialGroup()
									.addGroup(
										gl_panelPosition
											.createParallelGroup(
												javax.swing.GroupLayout.Alignment.TRAILING, false)
											.addComponent(labelPage,
												javax.swing.GroupLayout.Alignment.LEADING,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
											.addComponent(labelFooter,
												javax.swing.GroupLayout.Alignment.LEADING,
												javax.swing.GroupLayout.PREFERRED_SIZE, 97,
												javax.swing.GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(
										gl_panelPosition
											.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING, false)
											.addComponent(spinnerPage,
												javax.swing.GroupLayout.DEFAULT_SIZE, 45,
												Short.MAX_VALUE)
											.addComponent(spinnerMarginBottom,
												javax.swing.GroupLayout.PREFERRED_SIZE, 45,
												javax.swing.GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(
										gl_panelPosition
											.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
											.addComponent(labelCm).addComponent(labelHelp)))
							.addComponent(panelAlign, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
		gl_panelPosition.setVerticalGroup(gl_panelPosition.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			gl_panelPosition
				.createSequentialGroup()
				.addComponent(panelAlign, javax.swing.GroupLayout.PREFERRED_SIZE,
					javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(
					gl_panelPosition
						.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(labelPage, javax.swing.GroupLayout.PREFERRED_SIZE, 13,
							javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(spinnerPage, javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(labelHelp))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(
					gl_panelPosition
						.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(labelFooter, javax.swing.GroupLayout.PREFERRED_SIZE, 17,
							javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(spinnerMarginBottom, javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(labelCm))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(panelTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 152,
					javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		panelSize = new javax.swing.JPanel();
		textWidth = new javax.swing.JTextField();
		textHeight = new javax.swing.JTextField();
		labelWidth = new javax.swing.JLabel();
		labelHeight = new javax.swing.JLabel();
		checkAutomaticSize = new javax.swing.JCheckBox();

		panelSize.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
			resourceMap.getString("panelSize.border.title"),
			javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
			javax.swing.border.TitledBorder.DEFAULT_POSITION,
			resourceMap.getFont("panelSize.border.titleFont"))); // NOI18N
		panelSize.setName("panelSize"); // NOI18N

		textWidth.setText(resourceMap.getString("textWidth.text")); // NOI18N
		textWidth.setName("textWidth"); // NOI18N

		textHeight.setText(resourceMap.getString("textHeight.text")); // NOI18N
		textHeight.setName("textHeight"); // NOI18N

		labelWidth.setText(resourceMap.getString("labelWidth.text")); // NOI18N
		labelWidth.setName("labelWidth"); // NOI18N

		labelHeight.setText(resourceMap.getString("labelHeight.text")); // NOI18N
		labelHeight.setName("labelHeight"); // NOI18N

		checkAutomaticSize.setText(resourceMap.getString("checkAutomaticSize.text")); // NOI18N
		checkAutomaticSize.setName("checkAutomaticSize"); // NOI18N

		javax.swing.GroupLayout gl_panelSize = new javax.swing.GroupLayout(panelSize);
		gl_panelSize.setHorizontalGroup(gl_panelSize.createParallelGroup(Alignment.LEADING)
			.addGroup(
				Alignment.TRAILING,
				gl_panelSize
					.createSequentialGroup()
					.addContainerGap()
					.addComponent(checkAutomaticSize)
					.addPreferredGap(ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
					.addComponent(labelWidth)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textWidth, GroupLayout.PREFERRED_SIZE, 45,
						GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(labelHeight)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textHeight, GroupLayout.PREFERRED_SIZE, 45,
						GroupLayout.PREFERRED_SIZE).addContainerGap()));
		gl_panelSize.setVerticalGroup(gl_panelSize.createParallelGroup(Alignment.LEADING).addGroup(
			gl_panelSize
				.createSequentialGroup()
				.addGroup(
					gl_panelSize
						.createParallelGroup(Alignment.BASELINE)
						.addComponent(textHeight, GroupLayout.PREFERRED_SIZE,
							GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelHeight)
						.addComponent(textWidth, GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
						.addComponent(labelWidth).addComponent(checkAutomaticSize))
				.addContainerGap()));
		panelSize.setLayout(gl_panelSize);
		panelImage = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		radioNoImage = new javax.swing.JRadioButton();
		radioImport = new javax.swing.JRadioButton();
		radioNameImage = new javax.swing.JRadioButton();
		radioCustom = new javax.swing.JRadioButton();
		jPanel4 = new javax.swing.JPanel();
		jPanel5 = new javax.swing.JPanel();
		buttonSelectImageFile = new javax.swing.JButton();
		textFilenameImage = new javax.swing.JTextField();
		labelScale = new javax.swing.JLabel();
		spinnerScale = new javax.swing.JSpinner();
		jPanel6 = new javax.swing.JPanel();
		textCustom = new javax.swing.JTextField();
		spinnerFont = new javax.swing.JSpinner();
		labelFont = new javax.swing.JLabel();

		panelImage.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
			resourceMap.getString("panelImage.border.title"),
			javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
			javax.swing.border.TitledBorder.DEFAULT_POSITION,
			resourceMap.getFont("panelImage.border.titleFont"))); // NOI18N
		panelImage.setToolTipText(resourceMap.getString("panelImage.toolTipText")); // NOI18N
		panelImage.setName("panelImage"); // NOI18N

		jPanel3.setBorder(null);
		jPanel3.setName("jPanel3"); // NOI18N

		groupImage.add(radioNoImage);
		radioNoImage.setText(resourceMap.getString("radioNoImage.text")); // NOI18N
		radioNoImage.setName("radioNoImage"); // NOI18N

		groupImage.add(radioImport);
		radioImport.setText(resourceMap.getString("radioImport.text")); // NOI18N
		radioImport.setName("radioImport"); // NOI18N

		groupImage.add(radioNameImage);
		radioNameImage.setText(resourceMap.getString("radioNameImage.text")); // NOI18N
		radioNameImage.setName("radioNameImage"); // NOI18N

		groupImage.add(radioCustom);
		radioCustom.setText(resourceMap.getString("radioCustom.text")); // NOI18N
		radioCustom.setName("radioCustom"); // NOI18N

		javax.swing.GroupLayout gl_jPanel3 = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(gl_jPanel3);
		gl_jPanel3.setHorizontalGroup(gl_jPanel3.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			gl_jPanel3
				.createSequentialGroup()
				.addGroup(
					gl_jPanel3.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(radioNoImage).addComponent(radioImport))
				.addGap(47, 47, 47)
				.addGroup(
					gl_jPanel3.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(radioCustom).addComponent(radioNameImage))
				.addContainerGap(167, Short.MAX_VALUE)));
		gl_jPanel3.setVerticalGroup(gl_jPanel3.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			gl_jPanel3
				.createSequentialGroup()
				.addGroup(
					gl_jPanel3.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(radioNoImage).addComponent(radioCustom))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(
					gl_jPanel3.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(radioImport).addComponent(radioNameImage))));

		jPanel4.setBorder(null);
		jPanel4.setName("jPanel4"); // NOI18N

		jPanel5.setBorder(null);
		jPanel5.setName("jPanel5"); // NOI18N
		buttonSelectImageFile.setAction(actionMap.get("selectImageFile")); // NOI18N
		buttonSelectImageFile.setIcon(resourceMap.getIcon("buttonSelectImageFile.icon")); // NOI18N
		buttonSelectImageFile.setText(resourceMap.getString("buttonSelectImageFile.text")); // NOI18N
		buttonSelectImageFile.setName("buttonSelectImageFile"); // NOI18N

		textFilenameImage.setEditable(false);
		textFilenameImage.setText(resourceMap.getString("textFilenameImage.text")); // NOI18N
		textFilenameImage.setName("textFilenameImage"); // NOI18N

		labelScale.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		labelScale.setText(resourceMap.getString("labelScale.text")); // NOI18N
		labelScale.setName("labelScale"); // NOI18N

		spinnerScale.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float
			.valueOf(0.0f), null, Float.valueOf(0.1f)));
		spinnerScale.setName("spinnerScale"); // NOI18N

		javax.swing.GroupLayout gl_jPanel5 = new javax.swing.GroupLayout(jPanel5);
		jPanel5.setLayout(gl_jPanel5);
		gl_jPanel5.setHorizontalGroup(gl_jPanel5.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			gl_jPanel5
				.createSequentialGroup()
				.addComponent(buttonSelectImageFile)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(textFilenameImage, javax.swing.GroupLayout.DEFAULT_SIZE, 199,
					Short.MAX_VALUE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(labelScale, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
					javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(spinnerScale, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
					javax.swing.GroupLayout.PREFERRED_SIZE)));
		gl_jPanel5.setVerticalGroup(gl_jPanel5
			.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(
				gl_jPanel5
					.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(textFilenameImage, javax.swing.GroupLayout.DEFAULT_SIZE, 24,
						Short.MAX_VALUE)
					.addComponent(spinnerScale, javax.swing.GroupLayout.PREFERRED_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(labelScale))
			.addComponent(buttonSelectImageFile, javax.swing.GroupLayout.PREFERRED_SIZE, 24,
				Short.MAX_VALUE));

		jPanel6.setBorder(null);
		jPanel6.setName("jPanel6"); // NOI18N

		textCustom.setText(resourceMap.getString("textCustom.text")); // NOI18N
		textCustom.setMaximumSize(null);
		textCustom.setName("textCustom"); // NOI18N

		spinnerFont.setModel(new javax.swing.SpinnerNumberModel(10, 8, 16, 1));
		spinnerFont.setName("spinnerFont"); // NOI18N

		labelFont.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		labelFont.setText(resourceMap.getString("labelFont.text")); // NOI18N
		labelFont.setName("labelFont"); // NOI18N

		javax.swing.GroupLayout gl_jPanel6 = new javax.swing.GroupLayout(jPanel6);
		jPanel6.setLayout(gl_jPanel6);
		gl_jPanel6.setHorizontalGroup(gl_jPanel6.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			javax.swing.GroupLayout.Alignment.TRAILING,
			gl_jPanel6
				.createSequentialGroup()
				.addComponent(textCustom, javax.swing.GroupLayout.DEFAULT_SIZE, 231,
					Short.MAX_VALUE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(labelFont, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
					javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(spinnerFont, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
					javax.swing.GroupLayout.PREFERRED_SIZE)));
		gl_jPanel6.setVerticalGroup(gl_jPanel6.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			gl_jPanel6
				.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
				.addComponent(textCustom, javax.swing.GroupLayout.PREFERRED_SIZE, 24,
					javax.swing.GroupLayout.PREFERRED_SIZE)
				.addComponent(spinnerFont, javax.swing.GroupLayout.PREFERRED_SIZE,
					javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addComponent(labelFont)));

		javax.swing.GroupLayout gl_jPanel4 = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(gl_jPanel4);
		gl_jPanel4.setHorizontalGroup(gl_jPanel4
			.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			.addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		gl_jPanel4.setVerticalGroup(gl_jPanel4.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			gl_jPanel4
				.createSequentialGroup()
				.addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE,
					javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE,
					javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));

		javax.swing.GroupLayout gl_panelImage = new javax.swing.GroupLayout(panelImage);
		panelImage.setLayout(gl_panelImage);
		gl_panelImage.setHorizontalGroup(gl_panelImage.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			gl_panelImage
				.createSequentialGroup()
				.addContainerGap()
				.addGroup(
					gl_panelImage
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addContainerGap()));
		gl_panelImage.setVerticalGroup(gl_panelImage.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			gl_panelImage
				.createSequentialGroup()
				.addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
					javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE,
					javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panelText = new javax.swing.JPanel();
		checkName = new javax.swing.JCheckBox();
		checkDate = new javax.swing.JCheckBox();
		checkLocalization = new javax.swing.JCheckBox();
		checkReason = new javax.swing.JCheckBox();
		checkLabels = new javax.swing.JCheckBox();
		textLocalization = new javax.swing.JTextField();
		textReason = new javax.swing.JTextField();

		panelText.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
			resourceMap.getString("panelText.border.title"),
			javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
			javax.swing.border.TitledBorder.DEFAULT_POSITION,
			resourceMap.getFont("panelText.border.titleFont"))); // NOI18N
		panelText.setName("panelText"); // NOI18N

		checkName.setText(resourceMap.getString("checkName.text")); // NOI18N
		checkName.setName("checkName"); // NOI18N

		checkDate.setText(resourceMap.getString("checkDate.text")); // NOI18N
		checkDate.setName("checkDate"); // NOI18N

		checkLocalization.setText(resourceMap.getString("checkLocalization.text")); // NOI18N
		checkLocalization.setName("checkLocalization"); // NOI18N

		checkReason.setText(resourceMap.getString("checkReason.text")); // NOI18N
		checkReason.setName("checkReason"); // NOI18N

		checkLabels.setText(resourceMap.getString("checkLabels.text")); // NOI18N
		checkLabels.setName("checkLabels"); // NOI18N

		textLocalization.setText(resourceMap.getString("textLocalization.text")); // NOI18N
		textLocalization.setName("textLocalization"); // NOI18N

		textReason.setText(resourceMap.getString("textReason.text")); // NOI18N
		textReason.setName("textReason"); // NOI18N

		javax.swing.GroupLayout gl_panelText = new javax.swing.GroupLayout(panelText);
		panelText.setLayout(gl_panelText);
		gl_panelText.setHorizontalGroup(gl_panelText.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(
				gl_panelText
					.createSequentialGroup()
					.addGroup(
						gl_panelText.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addComponent(checkName).addComponent(checkDate))
					.addGap(36, 36, 36)
					.addGroup(
						gl_panelText
							.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addComponent(checkReason)
							.addComponent(checkLocalization,
								javax.swing.GroupLayout.PREFERRED_SIZE, 68,
								javax.swing.GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(
						gl_panelText
							.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
							.addComponent(textReason, javax.swing.GroupLayout.Alignment.TRAILING,
								javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
							.addComponent(textLocalization,
								javax.swing.GroupLayout.Alignment.TRAILING))
					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(checkLabels).addContainerGap(60, Short.MAX_VALUE)));
		gl_panelText.setVerticalGroup(gl_panelText.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			gl_panelText
				.createSequentialGroup()
				.addGroup(
					gl_panelText
						.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(checkName)
						.addComponent(textLocalization, javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(checkLocalization)
						.addComponent(checkLabels))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(
					gl_panelText
						.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(checkDate)
						.addComponent(checkReason)
						.addComponent(textReason, javax.swing.GroupLayout.PREFERRED_SIZE,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE))
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panelOutput = new javax.swing.JPanel();
		checkOverwrite = new javax.swing.JCheckBox();
		textPreffix = new javax.swing.JTextField();
		labelPreffix = new javax.swing.JLabel();
		labelSuffix = new javax.swing.JLabel();
		textSuffix = new javax.swing.JTextField();
		textRegex = new javax.swing.JTextField();
		checkRegex = new javax.swing.JCheckBox();

		panelOutput.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
			resourceMap.getString("panelOutput.border.title"),
			javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
			javax.swing.border.TitledBorder.DEFAULT_POSITION,
			resourceMap.getFont("panelOutput.border.titleFont"))); // NOI18N
		panelOutput.setName("panelOutput"); // NOI18N

		checkOverwrite.setText(resourceMap.getString("checkOverwrite.text")); // NOI18N
		checkOverwrite.setName("checkOverwrite"); // NOI18N

		textPreffix.setText(resourceMap.getString("textPreffix.text")); // NOI18N
		textPreffix.setName("textPreffix"); // NOI18N

		labelPreffix.setText(resourceMap.getString("labelPreffix.text")); // NOI18N
		labelPreffix.setName("labelPreffix"); // NOI18N

		labelSuffix.setText(resourceMap.getString("labelSuffix.text")); // NOI18N
		labelSuffix.setName("labelSuffix"); // NOI18N

		textSuffix.setName("textSuffix"); // NOI18N

		textRegex.setText(resourceMap.getString("textRegex.text")); // NOI18N
		textRegex.setName("textRegex"); // NOI18N

		checkRegex.setText(resourceMap.getString("checkRegex.text")); // NOI18N
		checkRegex.setName("checkRegex"); // NOI18N

		javax.swing.GroupLayout gl_panelOutput = new javax.swing.GroupLayout(panelOutput);
		panelOutput.setLayout(gl_panelOutput);
		gl_panelOutput.setHorizontalGroup(gl_panelOutput.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(
				gl_panelOutput
					.createSequentialGroup()
					.addContainerGap()
					.addGroup(
						gl_panelOutput
							.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(
								gl_panelOutput
									.createSequentialGroup()
									.addComponent(checkOverwrite)
									.addGap(42, 42, 42)
									.addComponent(labelPreffix)
									.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(textPreffix,
										javax.swing.GroupLayout.PREFERRED_SIZE, 39,
										javax.swing.GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(labelSuffix)
									.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(textSuffix, javax.swing.GroupLayout.DEFAULT_SIZE,
										41, Short.MAX_VALUE))
							.addGroup(
								gl_panelOutput
									.createSequentialGroup()
									.addComponent(checkRegex)
									.addGap(18, 18, 18)
									.addComponent(textRegex, javax.swing.GroupLayout.DEFAULT_SIZE,
										182, Short.MAX_VALUE))).addContainerGap()));
		gl_panelOutput.setVerticalGroup(gl_panelOutput.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addGroup(
			gl_panelOutput
				.createSequentialGroup()
				.addGroup(
					gl_panelOutput
						.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(checkOverwrite)
						.addComponent(labelPreffix)
						.addComponent(textPreffix, javax.swing.GroupLayout.PREFERRED_SIZE, 24,
							javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(labelSuffix)
						.addComponent(textSuffix, javax.swing.GroupLayout.PREFERRED_SIZE, 24,
							javax.swing.GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(
					gl_panelOutput
						.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(textRegex, javax.swing.GroupLayout.PREFERRED_SIZE, 24,
							javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(checkRegex))
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		GroupLayout gl_panelAppearance = new GroupLayout(panelAppearance);
		gl_panelAppearance
			.setHorizontalGroup(gl_panelAppearance.createParallelGroup(Alignment.LEADING)
				.addGroup(
					gl_panelAppearance
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
							gl_panelAppearance
								.createParallelGroup(Alignment.LEADING)
								.addComponent(panelImage, GroupLayout.DEFAULT_SIZE, 431,
									Short.MAX_VALUE)
								.addComponent(panelSize, GroupLayout.DEFAULT_SIZE, 431,
									Short.MAX_VALUE)
								.addComponent(panelPosition, GroupLayout.DEFAULT_SIZE, 431,
									Short.MAX_VALUE)
								.addComponent(panelText, GroupLayout.DEFAULT_SIZE, 431,
									Short.MAX_VALUE)
								.addComponent(panelOutput, GroupLayout.PREFERRED_SIZE, 431,
									GroupLayout.PREFERRED_SIZE)).addContainerGap()));
		gl_panelAppearance.setVerticalGroup(gl_panelAppearance.createParallelGroup(
			Alignment.LEADING).addGroup(
			gl_panelAppearance
				.createSequentialGroup()
				.addContainerGap()
				.addComponent(panelPosition, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
					GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panelSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
					GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panelImage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
					GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panelText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
					GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panelOutput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
					GroupLayout.PREFERRED_SIZE)
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panelAppearance.setLayout(gl_panelAppearance);
		initBindings();

	}

	private void initBindings() {

		AutoBinding<Object, Object, Object, Object> binding;

		binding = Bindings
			.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, radioImport,
				ELProperty.create("${selected}"), buttonSelectImageFile,
				BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);

		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, radioImport,
			ELProperty.create("${selected}"), textFilenameImage, BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);

		//

		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, radioImport,
			ELProperty.create("${selected}"), labelScale, BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);

		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, radioImport,
			ELProperty.create("${selected}"), spinnerScale, BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);

		//

		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, radioCustom,
			ELProperty.create("${selected}"), textCustom, BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);
		//
		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
			checkLocalization, ELProperty.create("${selected}"), textLocalization,
			BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);
		//
		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, checkReason,
			ELProperty.create("${selected}"), textReason, BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);
		//
		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, checkOverwrite,
			ELProperty.create("${!selected}"), textPreffix, BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);
		//
		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, checkOverwrite,
			ELProperty.create("${!selected}"), textSuffix, BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);
		//

		binding = Bindings
			.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, radioAbsolute,
				ELProperty.create("${selected}"), spinnerFooterDistance,
				BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);
		//
		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, radioRelative,
			ELProperty.create("${selected}"), spinnerLastLineDistance,
			BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);
		//

		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ, radioNameImage,
			ELProperty.create("${selected}"), spinnerFont, BeanProperty.create("enabled"));

		bindingGroup.addBinding(binding);

		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ, radioCustom,
			ELProperty.create("${selected}"), spinnerFont, BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);

		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ, radioNameImage,
			ELProperty.create("${selected}"), labelFont, BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);

		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ, radioCustom,
			ELProperty.create("${selected}"), labelFont, BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);

		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ, radioRelative,
			ELProperty.create("${selected}"), checkLastLine, BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);

		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ, checkLastLine,
			ELProperty.create("${!selected}"), textReference, BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);

		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ, radioRelative,
			ELProperty.create("${selected}"), textReference, BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);
		//

		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
			checkAutomaticSize, ELProperty.create("${!selected}"), textWidth,
			BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);
		//
		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
			checkAutomaticSize, ELProperty.create("${!selected}"), textHeight,
			BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);

		//
		binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ, checkRegex,
			ELProperty.create("${selected}"), textRegex, BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);

		bindingGroup.bind();

	}

	private void initComponents() {
		// bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

		groupAlign = new javax.swing.ButtonGroup();
		groupImage = new javax.swing.ButtonGroup();
		groupPosition = new javax.swing.ButtonGroup();
		toolbar = new javax.swing.JToolBar();
		buttonSave = new javax.swing.JButton();
		buttonCancel = new javax.swing.JButton();
		separatorTool = new javax.swing.JToolBar.Separator();
		buttonStandard = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setModal(true);
		setName("Form"); // NOI18N
		setResizable(false);

		toolbar.setRollover(true);
		toolbar.setName("toolbar"); // NOI18N

		buttonSave.setIcon(resourceMap.getIcon("buttonSave.icon")); // NOI18N
		buttonSave.setText(resourceMap.getString("buttonSave.text")); // NOI18N
		buttonSave.setFocusable(false);
		buttonSave.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
		buttonSave.setName("buttonSave"); // NOI18N
		buttonSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		toolbar.add(buttonSave);

		buttonCancel.setIcon(resourceMap.getIcon("buttonCancel.icon")); // NOI18N
		buttonCancel.setText(resourceMap.getString("buttonCancel.text")); // NOI18N
		buttonCancel.setFocusable(false);
		buttonCancel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
		buttonCancel.setName("buttonCancel"); // NOI18N
		buttonCancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		toolbar.add(buttonCancel);

		separatorTool.setName("separatorTool"); // NOI18N
		toolbar.add(separatorTool);

		buttonStandard.setIcon(resourceMap.getIcon("buttonStandard.icon")); // NOI18N
		buttonStandard.setText(resourceMap.getString("buttonStandard.text")); // NOI18N
		buttonStandard.setFocusable(false);
		buttonStandard.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
		buttonStandard.setName("buttonStandard"); // NOI18N
		buttonStandard.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		toolbar.add(buttonStandard);

		pack();
	}

	private void alignActionPerformed(java.awt.event.ActionEvent evt) {
		setGroupAlignValue(evt.getActionCommand());
	}

	private javax.swing.JButton buttonCancel;
	private javax.swing.JButton buttonSave;
	private javax.swing.JButton buttonSelectImageFile;
	private javax.swing.JButton buttonStandard;
	private javax.swing.JCheckBox checkAutomaticSize;
	private javax.swing.JCheckBox checkDate;
	private javax.swing.JCheckBox checkLabels;
	private javax.swing.JCheckBox checkLastLine;
	private javax.swing.JCheckBox checkLocalization;
	private javax.swing.JCheckBox checkName;
	private javax.swing.JCheckBox checkOverwrite;
	private javax.swing.JCheckBox checkReason;
	private javax.swing.JCheckBox checkRegex;
	private javax.swing.ButtonGroup groupAlign;
	private javax.swing.ButtonGroup groupImage;
	private javax.swing.ButtonGroup groupPosition;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JLabel labelCm;
	private javax.swing.JLabel labelFont;
	private javax.swing.JLabel labelFooter;
	private javax.swing.JLabel labelFooterDistance;
	private javax.swing.JLabel labelHeight;
	private javax.swing.JLabel labelHelp;
	private javax.swing.JLabel labelLastLineDistance;
	private javax.swing.JLabel labelPage;
	private javax.swing.JLabel labelPosition;
	private javax.swing.JLabel labelPreffix;
	private javax.swing.JLabel labelReference;
	private javax.swing.JLabel labelScale;
	private javax.swing.JLabel labelSuffix;
	private javax.swing.JLabel labelWidth;
	private javax.swing.JPanel panelAlign;
	private javax.swing.JPanel panelImage;
	private javax.swing.JPanel panelOutput;
	private javax.swing.JPanel panelPosition;
	private javax.swing.JPanel panelSize;
	private javax.swing.JPanel panelText;
	private javax.swing.JPanel panelTipo;
	private javax.swing.JRadioButton radioAbsolute;
	private javax.swing.JRadioButton radioCustom;
	private javax.swing.JRadioButton radioImport;
	private javax.swing.JRadioButton radioNameImage;
	private javax.swing.JRadioButton radioNoImage;
	private javax.swing.JRadioButton radioRelative;
	private javax.swing.JToolBar.Separator separatorTool;
	private javax.swing.JSpinner spinnerFont;
	private javax.swing.JSpinner spinnerFooterDistance;
	private javax.swing.JSpinner spinnerLastLineDistance;
	private javax.swing.JSpinner spinnerMarginBottom;
	private javax.swing.JSpinner spinnerPage;
	private javax.swing.JSpinner spinnerScale;
	private javax.swing.JTextField textCustom;
	private javax.swing.JTextField textFilenameImage;
	private javax.swing.JTextField textHeight;
	private javax.swing.JTextField textLocalization;
	private javax.swing.JTextField textPreffix;
	private javax.swing.JTextField textReason;
	private javax.swing.JTextField textReference;
	private javax.swing.JTextField textRegex;
	private javax.swing.JTextField textSuffix;
	private javax.swing.JTextField textWidth;
	private javax.swing.JToggleButton toggleCenter;
	private javax.swing.JToggleButton toggleLeft;
	private javax.swing.JToggleButton toggleRight;
	private javax.swing.JToolBar toolbar;
	private JPanel cards;
	private JPanel panelAppearance;

	// TODO: isso tem que sair daqui
	public void loadOptions(SignerOptions options) {

		// Position
		getSpinnerPage().setValue(options.getPageToSign());
		getSpinnerMarginBottom().setValue(options.getMarginBottom());
		if (getOptions().getSignaturePosition().equals(SignerOptions.POSITION_ABSOLUTE)) {
			getRadioAbsolute().setSelected(true);
		} else {
			getRadioRelative().setSelected(true);
		}
		getSpinnerLastLineDistance().setValue(options.getLastLineDistance());
		getSpinnerFooterDistance().setValue(getOptions().getFooterDistance());
		getTextReference().setText(options.getReferenceText());
		getCheckLastLine().setSelected(getOptions().getReferenceLastLine());

		// Size
		if (options.isSignatureAutomaticSize()) {
			getCheckAutomaticSize().setSelected(options.isSignatureAutomaticSize());
		} else {
			// TODO: Throw exception if dimensions aren't setted
			// TODO: Make mandatory fields
			getTextHeight().setText(options.getSignatureHeight().toString());
			getTextWidth().setText(options.getSignatureWidth().toString());
		}

		// Imagem
		if (getOptions().getImageImport() && getOptions().getImage() != null) {
			if (getOptions().getImage().exists()) {
				getRadioImport().setSelected(true);
			} else {
				getRadioNoImage().setSelected(true);
			}
		} else if (getOptions().getImageName()) {
			getRadioNameImage().setSelected(true);
		} else if (getOptions().getImageCustom()) {
			getRadioCustom().setSelected(true);
			getTextCustom().setText(getOptions().getImageCustomText());
		} else {
			getRadioNoImage().setSelected(true);
		}

		getSpinnerScale().setValue(options.getImageScale());

		if (options.getImage() != null) {
			getTextFilenameImage().setText(options.getImage().getAbsolutePath());
		}

		// Text
		getCheckName().setSelected(options.getTextName());
		getCheckDate().setSelected(options.getTextDate());
		getCheckLocalization().setSelected(options.getTextLocal());
		getCheckReason().setSelected(options.getTextReason());
		getCheckLabels().setSelected(options.getTextLabels());
		getTextReason().setText(options.getReason());
		getTextLocalization().setText(options.getLocal());

		// Output
		getTextPreffix().setText(options.getOutputPreffix());
		getTextSuffix().setText(options.getOutputSuffix());
		getCheckOverwrite().setSelected(options.getOutputOverwriteOriginal());
		getCheckMaskRegex().setSelected(options.getOutputMaskRegex());
		getTextRegex().setText(options.getMaskRegex());

		// Align
		String align = options.getSignatureAlign();
		if (align.equals("right")) {
			getToggleRight().setSelected(true);
		} else if (align.equals("left")) {
			getToggleLeft().setSelected(true);
		} else {
			getToggleCenter().setSelected(true);
		}

	}

	public SignerOptions saveOptions() throws IOException, URISyntaxException {

		// Position
		options.setPageToSign((Integer) getSpinnerPage().getValue());
		options.setMarginBottom((Float) getSpinnerMarginBottom().getValue());
		options.setLastLineDistance((Float) getSpinnerLastLineDistance().getValue());
		options.setFooterDistance((Float) getSpinnerFooterDistance().getValue());
		if (getRadioAbsolute().isSelected()) {
			options.setSignaturePosition(SignerOptions.POSITION_ABSOLUTE);
		} else {
			options.setSignaturePosition(SignerOptions.POSITION_RELATIVE);
		}
		options.setReferenceLastLine(getCheckLastLine().isSelected());
		options.setReferenceText(getTextReference().getText());

		// Size
		options.setSignatureAutomaticSize(getCheckAutomaticSize().isSelected());
		if (!getTextHeight().getText().isEmpty()) {
			options.setSignatureHeight(Float.parseFloat(getTextHeight().getText()));
		}
		if (!getTextWidth().getText().isEmpty()) {
			options.setSignatureWidth(Float.parseFloat(getTextWidth().getText()));
		}

		// Image
		options.setImageImport(getRadioImport().isSelected());
		options.setImageName(getRadioNameImage().isSelected());
		options.setImageCustom(getRadioCustom().isSelected());
		options.setImageCustomText(getTextCustom().getText());
		options.setImageScale((Float) getSpinnerScale().getValue());

		// Text
		options.setTextName(getCheckName().isSelected());
		options.setTextDate(getCheckDate().isSelected());
		options.setTextLocal(getCheckLocalization().isSelected());
		options.setTextReason(getCheckReason().isSelected());
		options.setTextLabels(getCheckLabels().isSelected());
		options.setReason(getTextReason().getText());
		options.setLocal(getTextLocalization().getText());

		// Output
		options.setOutputPreffix(getTextPreffix().getText());
		options.setOutputSuffix(getTextSuffix().getText());
		options.setOutputOverwriteOriginal(getCheckOverwrite().isSelected());
		options.setOutputMaskRegex(getCheckMaskRegex().isSelected());
		options.setMaskRegex(getTextRegex().getText());

		// Align
		options.setSignatureAlign(getAlign());

		options.store();

		return options;

	}

	public JCheckBox getCheckAutomaticSize() {
		return checkAutomaticSize;
	}

	public JCheckBox getCheckDate() {
		return checkDate;
	}

	public JCheckBox getCheckLabels() {
		return checkLabels;
	}

	public JCheckBox getCheckLocalization() {
		return checkLocalization;
	}

	public JCheckBox getCheckName() {
		return checkName;
	}

	public JCheckBox getCheckOverwrite() {
		return checkOverwrite;
	}

	public JCheckBox getCheckReason() {
		return checkReason;
	}

	public JSpinner getSpinnerLastLineDistance() {
		return spinnerLastLineDistance;
	}

	public JSpinner getSpinnerPage() {
		return spinnerPage;
	}

	public JSpinner getSpinnerScale() {
		return spinnerScale;
	}

	public JTextField getTextHeight() {
		return textHeight;
	}

	public JTextField getTextLocalization() {
		return textLocalization;
	}

	public JTextField getTextPreffix() {
		return textPreffix;
	}

	public JTextField getTextReason() {
		return textReason;
	}

	public JTextField getTextSuffix() {
		return textSuffix;
	}

	public JTextField getTextWidth() {
		return textWidth;
	}

	public JRadioButton getRadioImport() {
		return radioImport;
	}

	public JRadioButton getRadioNameImage() {
		return radioNameImage;
	}

	public JRadioButton getRadioNoImage() {
		return radioNoImage;
	}

	public JToggleButton getToggleCenter() {
		return toggleCenter;
	}

	public JToggleButton getToggleLeft() {
		return toggleLeft;
	}

	public JToggleButton getToggleRight() {
		return toggleRight;
	}

	public JSpinner getSpinnerMarginBottom() {
		return spinnerMarginBottom;
	}

	public String getAlign() {
		return groupAlignValue;
	}

	public void setGroupAlignValue(String align) {
		this.groupAlignValue = align;
	}

	public void setOptions(SignerOptions options) {
		this.options = options;
		loadOptions(options);
	}

	public SignerOptions getOptions() {
		return this.options;
	}

	public JTextField getTextFilenameImage() {
		return textFilenameImage;
	}

	public javax.swing.JRadioButton getRadioAbsolute() {
		return radioAbsolute;
	}

	public javax.swing.JRadioButton getRadioRelative() {
		return radioRelative;
	}

	public javax.swing.JSpinner getSpinnerFooterDistance() {
		return spinnerFooterDistance;
	}

	public javax.swing.JRadioButton getRadioCustom() {
		return radioCustom;
	}

	public javax.swing.JTextField getTextCustom() {
		return textCustom;
	}

	public javax.swing.JSpinner getSpinnerFont() {
		return spinnerFont;
	}

	public javax.swing.JCheckBox getCheckLastLine() {
		return checkLastLine;
	}

	public javax.swing.JTextField getTextReference() {
		return textReference;
	}

	public JTextField getTextRegex() {
		return textRegex;
	}

	public JCheckBox getCheckMaskRegex() {
		return checkRegex;
	}

}
