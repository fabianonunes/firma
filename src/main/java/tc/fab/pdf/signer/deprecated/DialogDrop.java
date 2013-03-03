/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DialogDrop.java
 *
 * Created on 07/05/2009, 13:55:05
 */

package tc.fab.pdf.signer.deprecated;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sun.awt.AWTUtilities;

@SuppressWarnings("restriction")
@Deprecated
public class DialogDrop extends javax.swing.JWindow {

	DialogDrop				dialog;

	public static JFrame	mFrame;

	private static class Holder {

		private static final DialogDrop	listener	= new DialogDrop(DialogDrop.mFrame);

	}

	public static DialogDrop getInstance(JFrame frame) {
		DialogDrop.mFrame = frame;
		return Holder.listener;
	}

	private static final long	serialVersionUID	= 1L;
	private int					X					= 0;
	private int					Y					= 0;

	private DialogDrop(final JFrame frame) {

		setAlwaysOnTop(true);

		try {
			AWTUtilities.setWindowOpacity(this, .7F);
		} catch (Exception e1) {
			// e1.printStackTrace();
		}

		initComponents();

		getDroppable().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				if (SwingUtilities.isRightMouseButton(e)) {
					frame.setState(Frame.ICONIFIED);
				} else if (SwingUtilities.isLeftMouseButton(e)) {
					frame.setState(Frame.NORMAL);
					frame.toFront();
				}

				super.mouseClicked(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				X = e.getX();
				Y = e.getY();
			}
		});

		getDroppable().addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {

				if (SwingUtilities.isLeftMouseButton(e)) {

					setLocation(getLocation().x + (e.getX() - X), getLocation().y + (e.getY() - Y));

				}

			}
		});

	}

	private void initComponents() {

		jScrollPane2 = new javax.swing.JScrollPane();

		jLabel2 = new javax.swing.JLabel();

		jScrollPane2
			.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane2
			.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

		jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource(
			"/tc/fab/file/resources/box_download_48.png"))); // NOI18N

		jScrollPane2.setViewportView(jLabel2);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2,
			javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE));
		layout.setVerticalGroup(layout.createParallelGroup(
			javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2,
			javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE));

		pack();
	}

	private javax.swing.JLabel		jLabel2;
	private javax.swing.JScrollPane	jScrollPane2;

	public Component getDroppable() {
		return jScrollPane2;
	}

	public void appears(JFrame frame) {

		if (isVisible()) {

			setVisible(false);

		} else {

			Toolkit tk = Toolkit.getDefaultToolkit();

			Dimension screen = tk.getScreenSize();

			Double pTop = 30D;

			setVisible(true);

			frame.setState(Frame.ICONIFIED);

			Double pLeft = screen.getWidth() - pTop - getWidth();

			setLocation(pLeft.intValue(), pTop.intValue());

		}

	}

}
