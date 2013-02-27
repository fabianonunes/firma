package tc.fab.firma;

import java.io.Serializable;
import java.util.List;

import tc.fab.mechanisms.Mechanism;
import tc.fab.mechanisms.Mechanism.Entry;

public class FirmaOptions implements Serializable {

	private static final long serialVersionUID = -6934691165042731405L;

	private String certificateProvider;
	private String alias;
	private Mechanism.Entry provider;
	private List<String> libs;

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

	public List<String> getLibs() {
		return libs;
	}

	public void setLibs(List<String> libs) {
		this.libs = libs;
	}

	public Entry getProvider() {
		return provider;
	}

	public void setProvider(Entry provider) {
		this.provider = provider;
	}

}
