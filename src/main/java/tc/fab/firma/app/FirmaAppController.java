package tc.fab.firma.app;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ActionMap;

import org.jdesktop.application.Action;

@Singleton
public class FirmaAppController implements AppController {

	private ActionMap actionMap;
	private AppContext context;
	private AppView view;

	@Inject
	public FirmaAppController(AppContext context, AppView view) {
		this.context = context;
		this.view = view;

	}

	@Override
	public ActionMap getActionMap() {
		if (actionMap == null) {
			actionMap = context.getAppContext().getActionMap(getClass(), this);
		}
		return actionMap;
	}

	@Override
	public boolean saveBeforeExit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void startActionManually(String actionName) {
		// TODO Auto-generated method stub

	}

	@Action(name = ACTION_QUIT)
	public void quitApplication() {
		context.getAppContext().getApplication().exit();
	}

	@Override
	public void addFile(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addFolder(File folder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sign() {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeSelected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAlias() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showDropArea() {
		// TODO Auto-generated method stub

	}

	@Override
	public void settings() {
		// TODO Auto-generated method stub

	}

}
