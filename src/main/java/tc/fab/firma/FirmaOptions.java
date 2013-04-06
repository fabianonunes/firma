package tc.fab.firma;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import tc.fab.firma.utils.PropertyObservable;
import tc.fab.pdf.signer.options.AppearanceOptions;
import tc.fab.pdf.signer.options.ReferencePosition;

public class FirmaOptions extends PropertyObservable implements Serializable {

	private static final long serialVersionUID = -6934691165042731405L;
	private String certificateProvider;
	private String alias;
	private String provider;
	private List<String> libs;
	private List<AppearanceOptions> appearances;
	private AppearanceOptions appearance;

	private ReferencePosition referencePosition = ReferencePosition.BELOW;
	private int pageToSign = -1;
	private String referenceText = null;
	
	public static FirmaOptions createDefaultInstance(String name) {

		FirmaOptions options = new FirmaOptions();

		List<AppearanceOptions> appearances = new ArrayList<>();
		AppearanceOptions appearanceOptions = new AppearanceOptions();
		
		appearanceOptions.setName(name);

		appearances.add(appearanceOptions);
		options.setAppearances(appearances);

		options.setAppearance(appearanceOptions);

		return options;
	}
	
	public String getAlias() {
		return alias;
	}

	public AppearanceOptions getAppearance() {
		return appearance;
	}

	public List<AppearanceOptions> getAppearances() {
		return appearances;
	}

	public String getCertificateProvider() {
		return certificateProvider;
	}

	public List<String> getLibs() {
		return libs;
	}

	public int getPageToSign() {
		return pageToSign;
	}

	public String getProvider() {
		return provider;
	}

	public ReferencePosition getReferencePosition() {
		return referencePosition;
	}

	public String getReferenceText() {
		return referenceText;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public void setAppearance(AppearanceOptions appearance) {
		this.appearance = appearance;
	}
	public void setAppearances(List<AppearanceOptions> appearances) {
		this.appearances = appearances;
	}

	public void setCertificateProvider(String certificateProvider) {
		this.certificateProvider = certificateProvider;
	}

	public void setLibs(List<String> libs) {
		this.libs = libs;
	}

	public void setPageToSign(int pageToSign) {
		this.pageToSign = pageToSign;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public void setReferencePosition(ReferencePosition referencePosition) {
		this.referencePosition = referencePosition;
	}

	public void setReferenceText(String referenceText) {
		this.referenceText = referenceText;
	}

}
