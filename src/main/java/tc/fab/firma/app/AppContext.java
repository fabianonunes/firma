package tc.fab.firma.app;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.jdesktop.application.ApplicationContext;

public interface AppContext {

	ApplicationContext getAppContext();

	JFrame getMainFrame();

	void showDialog(JDialog dlg);

	void showMessageDialog(Component parent, int msgType, String titleKey,
			String messageKey, Object... arguments);

	int showConfirmDialog(Component parent, String titleKey, String messageKey);

	ResourceReader getResReader();

}
