/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package tc.fab.pdf.signer.application;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.RootPaneContainer;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import org.jdesktop.application.ApplicationAction;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;

final class ProgressInputBlocker extends Task.InputBlocker {
	private static final Logger logger = Logger
			.getLogger(ProgressInputBlocker.class.getName());
	private JDialog modalDialog = null;

	@SuppressWarnings("rawtypes")
	ProgressInputBlocker(Task task, Task.BlockingScope scope, Object target,
			ApplicationAction action) {
		super(task, scope, target, action);
	}

	private void setActionTargetBlocked(boolean f) {
		javax.swing.Action action = (javax.swing.Action) getTarget();
		action.setEnabled(!f);
	}

	private void setComponentTargetBlocked(boolean f) {
		Component c = (Component) getTarget();
		c.setEnabled(!f);
		// Note: can't set the cursor on a disabled component
	}

	/*
	 * Accumulates a list of all of the descendants of root whose name begins
	 * with "BlockingDialog"
	 */
	private void blockingDialogComponents(Component root, List<Component> rv) {
		String rootName = root.getName();
		if ((rootName != null) && rootName.startsWith("BlockingDialog")) {
			rv.add(root);
		}
		if (root instanceof Container) {
			for (Component child : ((Container) root).getComponents()) {
				blockingDialogComponents(child, rv);
			}
		}
	}

	private List<Component> blockingDialogComponents(Component root) {
		List<Component> rv = new ArrayList<Component>();
		blockingDialogComponents(root, rv);
		return rv;
	}

	/*
	 * Inject resources from both the Task's ResourceMap and the
	 * ApplicationAction's ResourceMap. We add the action's name prefix to all
	 * of the components before the second step.
	 */
	private void injectBlockingDialogComponents(Component root) {
		ResourceMap taskResourceMap = getTask().getResourceMap();
		if (taskResourceMap != null) {
			taskResourceMap.injectComponents(root);
		}
		ApplicationAction action = getAction();
		if (action != null) {
			ResourceMap actionResourceMap = action.getResourceMap();
			String actionName = action.getName();
			for (Component c : blockingDialogComponents(root)) {
				c.setName(actionName + "." + c.getName());
			}
			actionResourceMap.injectComponents(root);
		}
	}

	private JDialog createBlockingDialog() {
		JOptionPane optionPane = new JOptionPane();
		if (getTask().getUserCanCancel()) {
			JButton cancelButton = new JButton();
			cancelButton.setName("BlockingDialog.cancelButton");
			ActionListener doCancelTask = new ActionListener() {
				public void actionPerformed(ActionEvent ignore) {
					getTask().cancel(true);
				}
			};
			cancelButton.addActionListener(doCancelTask);
			optionPane.setOptions(new Object[] { cancelButton });
		} else {
			optionPane.setOptions(new Object[] {}); // no OK button
		}
		Component dialogOwner = (Component) getTarget();
		String taskTitle = getTask().getTitle();
		String dialogTitle = (taskTitle == null) ? "BlockingDialog" : taskTitle;
		final JDialog dialog = optionPane
				.createDialog(dialogOwner, dialogTitle);
		dialog.setModal(true);
		dialog.setName("BlockingDialog");
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		WindowListener dialogCloseListener = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (getTask().getUserCanCancel()) {
					getTask().cancel(true);
					dialog.setVisible(false);
				}
			}
		};
		dialog.addWindowListener(dialogCloseListener);
		optionPane.setName("BlockingDialog.optionPane");
		injectBlockingDialogComponents(dialog);

		recreateOptionPaneMessage(optionPane);
		dialog.pack();
		dialog.setLocationRelativeTo((Component) getTarget());

		return dialog;
	}

	private void recreateOptionPaneMessage(JOptionPane optionPane) {

		Object message = optionPane.getMessage();

		if (message instanceof String) {

			Font font = optionPane.getFont();

			final JTextArea textArea = new JTextArea((String) message);
			textArea.setFont(font);
			int lh = textArea.getFontMetrics(font).getHeight();
			Insets margin = new Insets(15, 0, lh, 24); // top left bottom right
			textArea.setMargin(margin);
			textArea.setEditable(false);
			textArea.setWrapStyleWord(true);
			textArea.setBackground(optionPane.getBackground());

			JPanel panel = new JPanel();
			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(panel);

			panel.setLayout(layout);

			final JProgressBar progressBar = new JProgressBar();
			progressBar.setName("BlockingDialog.progressBar");
			progressBar.setIndeterminate(true);

			PropertyChangeListener taskPCL = new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent e) {
					if ("progress".equals(e.getPropertyName())) {
						progressBar.setIndeterminate(false);
						progressBar.setValue((Integer) e.getNewValue());
						updateStatusBarString(progressBar);
					} else if ("message".equals(e.getPropertyName())) {
						textArea.setText((String) e.getNewValue());
					}
				}
			};

			getTask().addPropertyChangeListener(taskPCL);

			layout.setHorizontalGroup(layout
					.createParallelGroup(
							javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(
							layout.createSequentialGroup()
									.addContainerGap()
									.addGroup(
											layout.createParallelGroup(
													javax.swing.GroupLayout.Alignment.LEADING)
													.addComponent(
															textArea,
															javax.swing.GroupLayout.Alignment.TRAILING,
															javax.swing.GroupLayout.DEFAULT_SIZE,
															249,
															Short.MAX_VALUE)
													.addComponent(
															progressBar,
															javax.swing.GroupLayout.Alignment.TRAILING,
															javax.swing.GroupLayout.DEFAULT_SIZE,
															249,
															Short.MAX_VALUE))
									.addContainerGap()));
			layout.setVerticalGroup(layout
					.createParallelGroup(
							javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(
							javax.swing.GroupLayout.Alignment.TRAILING,
							layout.createSequentialGroup()
									.addContainerGap()
									.addComponent(
											textArea,
											javax.swing.GroupLayout.DEFAULT_SIZE,
											39, Short.MAX_VALUE)
									.addPreferredGap(
											javax.swing.LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(
											progressBar,
											javax.swing.GroupLayout.PREFERRED_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.PREFERRED_SIZE)
									.addContainerGap()));

			injectBlockingDialogComponents(panel);
			optionPane.setMessage(panel);
		}
	}

	private void updateStatusBarString(JProgressBar progressBar) {
		if (!progressBar.isStringPainted()) {
			return;
		}

		ResourceMap map = getTask().getResourceMap();

		// TODO: passagem dinamica de type!!!
		String text = (String) map.getObject(
				"BlockingDialog.progressBarStringFormat", String.class);

		progressBar.putClientProperty("progressBarStringFormat", text);

		String key = "progressBarStringFormat";
		if (progressBar.getClientProperty(key) == null) {
			progressBar.putClientProperty(key, progressBar.getString());
		}
		String fmt = (String) progressBar.getClientProperty(key);
		if (progressBar.getValue() <= 0) {
			progressBar.setString("");
		} else if (fmt == null) {
			progressBar.setString(null);
		} else {
			double pctComplete = progressBar.getValue() / 100.0;
			long durSeconds = getTask().getExecutionDuration(TimeUnit.SECONDS);
			long durMinutes = durSeconds / 60;
			long remSeconds = (long) (0.5 + ((double) durSeconds / pctComplete))
					- durSeconds;
			long remMinutes = remSeconds / 60;
			String s = String.format(fmt, durMinutes, durSeconds
					- (durMinutes * 60), remMinutes, remSeconds
					- (remMinutes * 60));
			progressBar.setString(s);
		}

	}

	private void showBusyGlassPane(boolean f) {
		RootPaneContainer rpc = null;
		Component root = (Component) getTarget();
		while (root != null) {
			if (root instanceof RootPaneContainer) {
				rpc = (RootPaneContainer) root;
				break;
			}
			root = root.getParent();
		}
		if (rpc != null) {
			if (f) {
				JMenuBar menuBar = rpc.getRootPane().getJMenuBar();
				if (menuBar != null) {
					menuBar.putClientProperty(this, menuBar.isEnabled());
					menuBar.setEnabled(false);
				}
				JComponent glassPane = new BusyGlassPane();
				InputVerifier retainFocusWhileVisible = new InputVerifier() {
					public boolean verify(JComponent c) {
						return !c.isVisible();
					}
				};
				glassPane.setInputVerifier(retainFocusWhileVisible);
				Component oldGlassPane = rpc.getGlassPane();
				rpc.getRootPane().putClientProperty(this, oldGlassPane);
				rpc.setGlassPane(glassPane);
				glassPane.setVisible(true);
				glassPane.revalidate();
			} else {
				JMenuBar menuBar = rpc.getRootPane().getJMenuBar();
				if (menuBar != null) {
					boolean enabled = (Boolean) menuBar.getClientProperty(this);
					menuBar.putClientProperty(this, null);
					menuBar.setEnabled(enabled);
				}
				Component oldGlassPane = (Component) rpc.getRootPane()
						.getClientProperty(this);
				rpc.getRootPane().putClientProperty(this, null);
				if (!oldGlassPane.isVisible()) {
					rpc.getGlassPane().setVisible(false);
				}
				rpc.setGlassPane(oldGlassPane); // sets oldGlassPane.visible
			}
		}
	}

	/*
	 * Note: unfortunately, the busy cursor is reset when the modal dialog is
	 * shown.
	 */
	private static class BusyGlassPane extends JPanel {

		private static final long serialVersionUID = 1L;

		BusyGlassPane() {
			super(null, false);
			setVisible(false);
			setOpaque(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			MouseInputListener blockMouseEvents = new MouseInputAdapter() {
			};
			addMouseMotionListener(blockMouseEvents);
			addMouseListener(blockMouseEvents);
		}
	}

	/*
	 * If an action was specified then return the value of the
	 * actionName.BlockingDialogTimer.delay resource from the action's
	 * resourceMap. Otherwise return the value of the BlockingDialogTimer.delay
	 * resource from the Task's ResourceMap. The latter's default in defined in
	 * resources/Application.properties.
	 */
	private int blockingDialogDelay() {
		Integer delay = null;
		String key = "BlockingDialogTimer.delay";
		ApplicationAction action = getAction();
		if (action != null) {
			ResourceMap actionResourceMap = action.getResourceMap();
			String actionName = action.getName();
			delay = actionResourceMap.getInteger(actionName + "." + key);
		}
		ResourceMap taskResourceMap = getTask().getResourceMap();
		if ((delay == null) && (taskResourceMap != null)) {
			delay = taskResourceMap.getInteger(key);
		}
		return (delay == null) ? 0 : delay.intValue();
	}

	private void showBlockingDialog(boolean f) {
		if (f) {
			if (modalDialog != null) {
				String msg = String.format(
						"unexpected InputBlocker state [%s] %s", f, this);
				logger.warning(msg);
				modalDialog.dispose();
			}
			modalDialog = createBlockingDialog();
			ActionListener showModalDialog = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (modalDialog != null) { // already dismissed
						modalDialog.setVisible(true);
					}
				}
			};
			Timer showModalDialogTimer = new Timer(blockingDialogDelay(),
					showModalDialog);
			showModalDialogTimer.setRepeats(false);
			showModalDialogTimer.start();
		} else {
			if (modalDialog != null) {
				modalDialog.dispose();
				modalDialog = null;
			} else {
				String msg = String.format(
						"unexpected InputBlocker state [%s] %s", f, this);
				logger.warning(msg);
			}
		}
	}

	@Override
	protected void block() {
		switch (getScope()) {
		case ACTION:
			setActionTargetBlocked(true);
			break;
		case COMPONENT:
			setComponentTargetBlocked(true);
			break;
		case WINDOW:
		case APPLICATION:
			showBusyGlassPane(true);
			showBlockingDialog(true);
			break;
		default:
			break;
		}
	}

	@Override
	protected void unblock() {
		switch (getScope()) {
		case ACTION:
			setActionTargetBlocked(false);
			break;
		case COMPONENT:
			setComponentTargetBlocked(false);
			break;
		case WINDOW:
		case APPLICATION:
			showBusyGlassPane(false);
			showBlockingDialog(false);
			break;
		default:
			break;
		}
	}
}
