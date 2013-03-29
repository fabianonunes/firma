package tc.fab.firma;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.inject.Singleton;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;

import tc.fab.app.AppContext;
import tc.fab.app.ResourceReader;

@Singleton
public class FirmaContext implements AppContext {

	private ApplicationContext appContext;
	private ResourceReader resReader;

	public FirmaContext(ApplicationContext context) {
		appContext = context;
		resReader = new ResourceReader(appContext.getResourceMap());
	}

	@Override
	public ApplicationContext getAppContext() {
		return appContext;
	}

	@Override
	public JFrame getMainFrame() {
		return getApplication().getMainFrame();
	}

	@Override
	public void showDialog(JDialog dlg) {
		getApplication().show(dlg);

	}

	@Override
	public void showMessageDialog(Component parent, int msgType, String titleKey,
		String messageKey, Object... arguments) {
		JOptionPane.showMessageDialog(parent, resReader.getString(messageKey, arguments),
			resReader.getString(titleKey), msgType);
	}

	@Override
	public int showConfirmDialog(Component parent, String titleKey, String messageKey) {
		return JOptionPane.showConfirmDialog(parent, resReader.getString(messageKey),
			resReader.getString(titleKey), JOptionPane.YES_NO_OPTION);
	}

	private SingleFrameApplication getApplication() {
		return (SingleFrameApplication) appContext.getApplication();
	}

	@Override
	public ResourceReader getResReader() {
		return resReader;
	}

	@Override
	public ResourceMap getResourceMap() {
		return appContext.getResourceMap();
	}

	@Override
	public void fireAction(Object actionsObject, String actionName) {
		fireAction(getAction(actionsObject, actionName));
	}

	@Override
	public Action getAction(Object actionsObject, String actionName) {
		return appContext.getActionMap(actionsObject).get(actionName);
	}

	@Override
	public void fireAction(Action action) {
		action.actionPerformed(new ActionEvent(getMainFrame(), ActionEvent.ACTION_PERFORMED, action
			.getValue(Action.NAME).toString()));

	}

}
