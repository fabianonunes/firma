package tc.fab.pdf.signer.options;

import java.io.File;
import java.io.Serializable;

import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;

public class AppearanceOptions implements Serializable {

	private static final long serialVersionUID = 5407956656681711303L;

	private RenderingMode renderMode = RenderingMode.NAME_AND_DESCRIPTION;

	private File image = null;

	// Position
	private float signatureWidth = 10f;
	private float signatureHeight = 2f;
	private int pageToSign = -1;
	private float referenceDistance = 0.4f;
	private String referenceText = null;

	// Description
	private boolean showName = true;
	private boolean showDate = true;
	private boolean sbowLocal = false;
	private boolean showReason = false;
	private boolean showLabels = false;
	private String local = null;
	private String reason = null;

	public AppearanceOptions() {
	}

	public File getImage() {
		return image;
	}

	public String getLocation() {
		return local;
	}

	public int getPageToSign() {
		return pageToSign;
	}

	public String getReason() {
		return reason;
	}

	public float getReferenceDistance() {
		return referenceDistance;
	}

	public String getReferenceText() {
		return referenceText;
	}

	public RenderingMode getRenderMode() {
		return renderMode;
	}

	public boolean getSbowLocal() {
		return sbowLocal;
	}

	public boolean getShowDate() {
		return showDate;
	}

	public boolean getShowLabels() {
		return showLabels;
	}

	public boolean getShowName() {
		return showName;
	}

	public boolean getShowReason() {
		return showReason;
	}

	public float getSignatureHeight() {
		return signatureHeight;
	}

	public float getSignatureWidth() {
		return signatureWidth;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public void setPageToSign(int pageToSign) {
		this.pageToSign = pageToSign;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setReferenceDistance(float referenceDistance) {
		this.referenceDistance = referenceDistance;
	}

	public void setReferenceText(String referenceText) {
		this.referenceText = referenceText;
	}

	public void setRenderMode(RenderingMode renderMode) {
		this.renderMode = renderMode;
	}

	public void setSbowLocal(boolean sbowLocal) {
		this.sbowLocal = sbowLocal;
	}

	public void setShowDate(boolean showDate) {
		this.showDate = showDate;
	}

	public void setShowLabels(boolean showLabels) {
		this.showLabels = showLabels;
	}

	public void setShowName(boolean showName) {
		this.showName = showName;
	}

	public void setShowReason(boolean showReason) {
		this.showReason = showReason;
	}

	public void setSignatureHeight(float signatureHeight) {
		this.signatureHeight = signatureHeight;
	}

	public void setSignatureWidth(float signatureWidth) {
		this.signatureWidth = signatureWidth;
	}

}
