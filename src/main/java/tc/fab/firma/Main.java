package tc.fab.firma;

import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.callback.CallbackHandler;
import javax.swing.UIManager;

import org.jdesktop.application.View;
import org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.app.AppDocument;
import tc.fab.app.AppView;
import tc.fab.mechanisms.LibraryManager;
import tc.fab.mechanisms.LibraryManagerImpl;
import tc.fab.mechanisms.Mechanism;
import tc.fab.mechanisms.SmartCardAdapter;
import tc.fab.security.callback.PINCallback;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main extends Firma {

	private AppContext context;
	private AppController controller;
	private AppView view;
	private AppDocument document;

	private LibraryManager libs;

	private static final Logger LOGGER = Logger.getLogger(Firma.class.getName());

	public static void main(String args[]) {
		launch(Main.class, args);
	}

	@Override
	protected void startup() {

		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {

				context = new FirmaContext(getContext());

				// app
				bind(AppContext.class).toInstance(context);
				bind(AppView.class).to(FirmaView.class);
				bind(AppController.class).to(FirmaController.class);
				bind(AppDocument.class).to(FirmaDocument.class);

				// security
				bind(LibraryManager.class).to(LibraryManagerImpl.class);
				bind(Mechanism.class).to(SmartCardAdapter.class);
				bind(CallbackHandler.class).to(PINCallback.class);

			}
		});
		
		controller = injector.getInstance(AppController.class);

		addExitListener(new ExitListener() {
			@Override
			public boolean canExit(EventObject e) {
				return controller.saveBeforeExit();
			}

			@Override
			public void willExit(EventObject e) {
			}
		});

		document = injector.getInstance(AppDocument.class);
		document.loadOptions();

		libs = injector.getInstance(LibraryManager.class);
		context.getResourceMap().injectFields(libs);
		
		document.getOptions().setLibs(libs.getLibs());

		view = injector.getInstance(AppView.class);
		initLookAndFeel(SubstanceCremeLookAndFeel.class.toString());
		view.initView();

		show((View) view);

	}

	private boolean executeSave() {
		return true;
	}

	private void initLookAndFeel(String lafClass) {
		try {
			String lf = "org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel";
			UIManager.setLookAndFeel(lf);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Failed to set look&feel to " + lafClass + "!", e);
		}
	}

	@Override
	protected void shutdown() {
		super.shutdown();
		document.storeOptions();
	}

	@Override
	protected void initialize(String[] args) {
		super.initialize(args);
	}

	@Override
	protected void ready() {
		super.ready();
	}

}
