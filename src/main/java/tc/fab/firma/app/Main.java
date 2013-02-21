package tc.fab.firma.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;

import tc.fab.mechanisms.Mechanism;
import tc.fab.mechanisms.SmartCardAdapter;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main extends Firma {

	private AppContext context;

	private static final Logger LOGGER = Logger
			.getLogger(Firma.class.getName());

	@Override
	protected void startup() {


		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {

				context = new FirmaAppContext(getContext());

				bind(AppContext.class).toInstance(context);
				bind(Mechanism.class).to(SmartCardAdapter.class);
				
			}
		});

		initLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel");
		
	}

	private void initLookAndFeel(String lafClass) {
		try {
			String lf = "org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel";
			UIManager.setLookAndFeel(lf);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Failed to set look&feel to " + lafClass
					+ "!", e);
		}
	}

	@Override
	protected void initialize(String[] args) {
		super.initialize(args);
	}

}
