package tc.fab.pdf.signer.options;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;

import tc.fab.firma.utils.PropertyObservable;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.CertificateInfo.X500Name;

public class AppearanceOptions extends PropertyObservable implements Serializable {

	private static final long serialVersionUID = 5407956656681711303L;

	private RenderingMode renderMode = RenderingMode.NAME_AND_DESCRIPTION;

	private String name = "";
	private String image = null; // background image
	private String graphic = null; // graphic image

	// Position
	private float signatureWidth = 10f;
	private float signatureHeight = 2f;
	private float referenceDistance = 0.4f;

	// Rendering
	private boolean renderName = true;
	private boolean renderGraphic = false;

	// Description (templated)
	private boolean showName = true;
	private boolean showDN = true;
	private boolean showDate = true;
	private boolean showLocation = false;
	private boolean showReason = false;
	private boolean showLabels = true;

	// Signature properties
	private String location = null;
	private String reason = null;
	private String contact = null;

	public void apply(PdfSignatureAppearance appearance, Rectangle pCoords, Integer pageToSign,
		String fieldName) throws BadElementException, MalformedURLException, IOException {

		if (renderName) {

			setRenderMode(RenderingMode.NAME_AND_DESCRIPTION);

		} else if (renderGraphic) {

			if (graphic != null) {

				if (showName || showDate || showLocation || showReason) {
					setRenderMode(RenderingMode.GRAPHIC_AND_DESCRIPTION);
				} else {
					setRenderMode(RenderingMode.GRAPHIC);
				}

			} else {
				setRenderMode(RenderingMode.DESCRIPTION);
			}

		} else {

			setRenderMode(RenderingMode.DESCRIPTION);

		}

		appearance.setRenderingMode(getRenderMode());
		appearance.setLocation(getLocation());
		appearance.setReason(getReason());
		appearance.setContact(getContact());

		if (getGraphic() != null) {
			switch (getRenderMode()) {
				case GRAPHIC:
				case GRAPHIC_AND_DESCRIPTION:
					Image img = Image.getInstance(getGraphic());
					appearance.setSignatureGraphic(img);
					break;
				default:
					break;
			}
		}

		X509Certificate cert = (X509Certificate) appearance.getCertificate();
		X500Name certInfo = CertificateInfo.getSubjectFields(cert);

		StringBuffer buffer = new StringBuffer();

		if (showName) {
			if (showLabels)
				buffer.append("Assinado digitalmente por ");
			String cname = certInfo.getField("CN");
			if (cname == null)
				cname = certInfo.getField("E");
			buffer.append(cname);
			buffer.append('\n');
		}
		if (showDN) {
			if (showLabels)
				buffer.append("DN: ");
			buffer.append(cert.getSubjectDN());
			buffer.append('\n');
		}
		if (showReason && reason != null) {
			if (showLabels)
				buffer.append("Motivo: ");
			buffer.append(getReason());
			buffer.append('\n');
		}
		if (showLocation && location != null) {
			if (showLabels)
				buffer.append("Local: ");
			buffer.append(getLocation());
			buffer.append('\n');

		}
		if (showDate) {

			if (showLabels)
				buffer.append("Data: ");

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");

			String date = formatter.format(appearance.getSignDate().getTime());
			buffer.append(date);

		}

		appearance.setLayer2Text(buffer.toString());

		// background image - disabled for simplicity purpose
		// if (getImage() != null) {
		// appearance.setImage(Image.getInstance(getImage().getAbsolutePath()));
		// appearance.setImageScale(-1f);
		// }

		appearance.setVisibleSignature(pCoords, pageToSign, fieldName);

	}

	public AppearanceOptions() {
	}

	@Override
	public String toString() {
		return this.getName();
	}

	public RenderingMode getRenderMode() {
		return renderMode;
	}

	public String getName() {
		return name;
	}

	public String getImage() {
		return image;
	}

	public String getGraphic() {
		return graphic;
	}

	public float getSignatureWidth() {
		return signatureWidth;
	}

	public float getSignatureHeight() {
		return signatureHeight;
	}

	public float getReferenceDistance() {
		return referenceDistance;
	}

	public boolean isRenderName() {
		return renderName;
	}

	public boolean isRenderGraphic() {
		return renderGraphic;
	}

	public boolean isShowName() {
		return showName;
	}

	public boolean isShowDN() {
		return showDN;
	}

	public boolean isShowDate() {
		return showDate;
	}

	public boolean isShowLocation() {
		return showLocation;
	}

	public boolean isShowReason() {
		return showReason;
	}

	public boolean isShowLabels() {
		return showLabels;
	}

	public String getLocation() {
		return location;
	}

	public String getReason() {
		return reason;
	}

	public String getContact() {
		return contact;
	}

	public void setRenderMode(RenderingMode renderMode) {
		this.renderMode = renderMode;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setGraphic(String graphic) {
		this.graphic = graphic;
	}

	public void setSignatureWidth(float signatureWidth) {
		this.signatureWidth = signatureWidth;
	}

	public void setSignatureHeight(float signatureHeight) {
		this.signatureHeight = signatureHeight;
	}

	public void setReferenceDistance(float referenceDistance) {
		this.referenceDistance = referenceDistance;
	}

	public void setRenderName(boolean renderName) {
		this.renderName = renderName;
	}

	public void setRenderGraphic(boolean renderGraphic) {
		this.renderGraphic = renderGraphic;
	}

	public void setShowName(boolean showName) {
		this.showName = showName;
	}

	public void setShowDN(boolean showDN) {
		this.showDN = showDN;
	}

	public void setShowDate(boolean showDate) {
		this.showDate = showDate;
	}

	public void setShowLocation(boolean showLocation) {
		this.showLocation = showLocation;
	}

	public void setShowReason(boolean showReason) {
		this.showReason = showReason;
	}

	public void setShowLabels(boolean showLabels) {
		this.showLabels = showLabels;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

}
