package tc.fab.app;

import tc.fab.firma.FirmaOptions;

public interface AppDocument {

	FirmaOptions getOptions();

	void loadOptions();

	void storeOptions();

}
