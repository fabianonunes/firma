package tc.fab.firma;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import tc.fab.app.AppContext;
import tc.fab.app.AppDocument;

@Singleton
public class FirmaDocument implements AppDocument {

	private static final Logger LOGGER = Logger.getLogger(FirmaDocument.class.getName());

	private AppContext context;
	private FirmaOptions options;

	private static final String FILE_OPTIONS = "options.xml";

	@Inject
	public FirmaDocument(AppContext context) {
		this.context = context;
	}

	@Override
	public void loadOptions() {

		try {
			options = (FirmaOptions) context.getAppContext().getLocalStorage().load(FILE_OPTIONS);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Failed to load application options from '" + FILE_OPTIONS
				+ "', using default values ...", e);
		}

		if (options == null) {
			options = FirmaOptions.createDefaultInstance();
		}

	}

	@Override
	public void storeOptions() {
		try {
			context.getAppContext().getLocalStorage().save(options, FILE_OPTIONS);
		} catch (IOException ioe) {
			LOGGER.log(Level.SEVERE, "Failed to write application options to '" + FILE_OPTIONS
				+ "' ...", ioe);
		}
	}

	@Override
	public FirmaOptions getOptions() {
		return options;
	}

}
