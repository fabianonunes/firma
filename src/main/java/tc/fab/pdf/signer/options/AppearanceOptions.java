package tc.fab.pdf.signer.options;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;

import tc.fab.firma.utils.PropertyObservable;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.text.pdf.PdfTemplate;
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
		String fieldName) throws MalformedURLException, IOException, BadElementException {

		if (renderName) {

			setRenderMode(RenderingMode.NAME_AND_DESCRIPTION);

		} else if (renderGraphic) {

			if (graphic != null && new File(getGraphic()).exists()) {

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
		String cname = certInfo.getField("CN");
		if (cname == null) {
			cname = certInfo.getField("E");
		}
		if (cname == null) {
			cname = "";
		}

		StringBuffer buffer = new StringBuffer();

		if (showName) {
			if (showLabels)
				buffer.append("Assinado digitalmente por ");
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
			appearance.setReason(getReason());
			if (showLabels)
				buffer.append("Motivo: ");
			buffer.append(getReason());
			buffer.append('\n');
		}
		if (showLocation && location != null) {
			appearance.setLocation(getLocation());
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

		if (contact != null) {
			appearance.setContact(getContact());
		}

		String description = buffer.toString();

		appearance.setLayer2Text(description);

		// background image - disabled for simplicity purpose
		// if (getImage() != null) {
		// appearance.setImage(Image.getInstance(getImage().getAbsolutePath()));
		// appearance.setImageScale(-1f);
		// }

		appearance.setVisibleSignature(pCoords, pageToSign, fieldName);

	}

	protected void addImageToLayer(PdfTemplate n2, Image im) throws DocumentException,
		MalformedURLException, IOException {

		n2.beginText();

		Float pSize = im.getWidth() / im.getHeight();

		Float w = im.getWidth();
		Float h = im.getHeight();

		if (im.getWidth() > signatureWidth) {
			w = signatureWidth;
			h = w / pSize;
		} else if (im.getHeight() > signatureHeight) {
			h = signatureHeight;
			w = h * pSize;
		}

		Float pbLeft = signatureWidth / 2 - w / 2;
		Float pbTop = signatureHeight / 2 - h / 2;

		n2.addImage(im, w, 0, 0, h, pbLeft, pbTop);

		n2.endText();

	}

	protected void addTextToLayer(PdfTemplate n2, String text) throws IOException,
		DocumentException {

		Float fontSize = 10F;

		BaseFont bf = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
		Float tWidth = bf.getWidthPoint(text, fontSize);

		// TODO: Estudar altura de caracteres para melhor determinação da margem
		// 1.2F = 20% de margem de segurança para caracteres fora de linha
		signatureHeight = fontSize * 1.2F;
		signatureWidth = tWidth + 2F;

		if (n2 != null) {

			n2.beginText();
			n2.setFontAndSize(bf, fontSize);
			n2.showTextAligned(PdfTemplate.ALIGN_CENTER, text, signatureWidth / 2, signatureHeight
				- fontSize, 0);
			n2.endText();

		}

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
