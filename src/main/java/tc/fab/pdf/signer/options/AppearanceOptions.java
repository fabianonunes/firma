package tc.fab.pdf.signer.options;

import java.io.File;

import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;

public class AppearanceOptions {

	private RenderingMode renderMode = RenderingMode.NAME_AND_DESCRIPTION;

	private File image = null;

	// Position
	private Float signatureWidth = 10f;
	private Float signatureHeight = 4f;
	private Integer pageToSign = -1;
	private String referenceText = null;

	// Description
	private Boolean showName = true;
	private Boolean showDate = true;
	private Boolean sbowLocal = false;
	private Boolean showReason = false;
	private Boolean showLabels = false;
	private String local = null;
	private String reason = null;

	public AppearanceOptions() {
	}

	public void applyTo(PdfSignatureAppearance appearance) {
		
		appearance.setRenderingMode(renderMode);
		appearance.setLocation(local);

	}

	private static Float cmToPoint(Number cm) {
		return (cm.floatValue() / 2.54F) * 72;
	}

	public File getImage() {
		return image;
	}

	public String getLocal() {
		return local;
	}

	public Integer getPageToSign() {
		return pageToSign;
	}

	public String getReason() {
		return reason;
	}

	public String getReferenceText() {
		return referenceText;
	}

	public Boolean getSbowLocal() {
		return sbowLocal;
	}

	public Boolean getShowDate() {
		return showDate;
	}

	public Boolean getShowLabels() {
		return showLabels;
	}

	public Boolean getShowName() {
		return showName;
	}

	public Boolean getShowReason() {
		return showReason;
	}

	public Float getSignatureHeight() {
		return signatureHeight;
	}

	public Float getSignatureWidth() {
		return signatureWidth;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public void setPageToSign(Integer pageToSign) {
		this.pageToSign = pageToSign;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setReferenceText(String referenceText) {
		this.referenceText = referenceText;
	}

	public void setSbowLocal(Boolean sbowLocal) {
		this.sbowLocal = sbowLocal;
	}

	public void setShowDate(Boolean showDate) {
		this.showDate = showDate;
	}

	public void setShowLabels(Boolean showLabels) {
		this.showLabels = showLabels;
	}

	public void setShowName(Boolean showName) {
		this.showName = showName;
	}

	public void setShowReason(Boolean showReason) {
		this.showReason = showReason;
	}

	public void setSignatureHeight(Float signatureHeight) {
		this.signatureHeight = signatureHeight;
	}

	public void setSignatureWidth(Float signatureWidth) {
		this.signatureWidth = signatureWidth;
	}

}
