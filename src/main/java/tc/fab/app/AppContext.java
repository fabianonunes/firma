package tc.fab.app;

import java.awt.Component;

import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;

public interface AppContext {

	ApplicationContext getAppContext();

	JFrame getMainFrame();

	void showDialog(JDialog dlg);

	void showMessageDialog(Component parent, int msgType, String titleKey, String messageKey,
		Object... arguments);

	int showConfirmDialog(Component parent, String titleKey, String messageKey);

	ResourceReader getResReader();

	ResourceMap getResourceMap();

	Action getAction(Object actionsObject, String actionName);

	void fireAction(Object actionsObject, String actionName);
	
	void fireAction(Action action);

}