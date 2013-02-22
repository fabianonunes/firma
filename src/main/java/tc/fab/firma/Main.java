package tc.fab.firma;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.callback.CallbackHandler;
import javax.swing.UIManager;

import org.jdesktop.application.View;
import org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.app.AppView;
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

				// security
				bind(Mechanism.class).to(SmartCardAdapter.class);
				bind(CallbackHandler.class).to(PINCallback.class);

			}
		});

		initLookAndFeel(SubstanceCremeLookAndFeel.class.toString());

		controller = injector.getInstance(AppController.class);
		view = injector.getInstance(AppView.class);

		view.initView();

		show((View) view);

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
	protected void initialize(String[] args) {
		super.initialize(args);
	}

}
