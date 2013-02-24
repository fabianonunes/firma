package tc.fab.firma;

import java.io.Serializable;

public class FirmaOptions implements Serializable {

	private static final long serialVersionUID = -6934691165042731405L;

	private String certificateProvider;
	private String alias;

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

		return options;
	}

}
