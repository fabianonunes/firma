package tc.fab.firma;

import java.io.Serializable;
import java.util.List;

import tc.fab.firma.utils.PropertyObservable;
import tc.fab.pdf.signer.options.AppearanceOptions;

public class FirmaOptions extends PropertyObservable implements Serializable {

	private static final long serialVersionUID = -6934691165042731405L;

	private String certificateProvider;
	private String alias;
	private String provider;
	private List<String> libs;
	private AppearanceOptions appearance;

	public String getCertificateProvider() {
		return certificateProvider;
	}

	public void setCertificateProvider(String certificateProvider) {
		this.certificateProvider = certificateProvider;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public static FirmaOptions createDefaultInstance() {
		FirmaOptions options = new FirmaOptions();
		options.setAppearance(new AppearanceOptions());
		return options;
	}

	public List<String> getLibs() {
		return libs;
	}

	public void setLibs(List<String> libs) {
		this.libs = libs;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public AppearanceOptions getAppearance() {
		if (appearance == null) {
			appearance = new AppearanceOptions();
		}
		return appearance;
	}

	public void setAppearance(AppearanceOptions appearance) {
		this.appearance = appearance;
	}

}
