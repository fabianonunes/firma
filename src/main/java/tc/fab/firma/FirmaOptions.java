package tc.fab.firma;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.List;

public class FirmaOptions implements Serializable {

	private static final long serialVersionUID = -6934691165042731405L;

	private String certificateProvider;
	private String alias;
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

	private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}
}
