package tc.fab.firma.app;

import java.awt.Component;

import javax.inject.Singleton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.SingleFrameApplication;

import tc.fab.firma.app.AppContext;
import tc.fab.firma.app.ResourceReader;

@Singleton
public class FirmaAppContext implements AppContext {

	private ApplicationContext	appContext;
	private ResourceReader		resReader;

	public FirmaAppContext(ApplicationContext context) {
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

}
