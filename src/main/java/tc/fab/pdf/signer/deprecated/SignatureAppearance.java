package tc.fab.pdf.signer.deprecated;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import tc.fab.pdf.signer.message.Envelope;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfTemplate;

@Deprecated
public class SignatureAppearance {

	private Float signatureWidth;
	private Float signatureHeight;
	private PdfSignatureAppearance appearance;

	public SignatureAppearance() {
	}

	public void sign(File inputFile, File outputFile) throws Exception {
	}

	public Envelope signBlank(File inputFile, File outputFile, Certificate[] chain) {
		return null;

	}

	protected void configAppearance(X509Certificate certificate) throws IOException,
		DocumentException, KeyStoreException {

		// if (getOptions().hasGraphic()) {
		// img = Image.getInstance(getOptions().getImage().getAbsolutePath());
		// }
		//
		// if (renderMode.equals(RenderMode.Graphic)) {
		//
		// appearance.setRenderingMode(RenderingMode.GRAPHIC);
		//
		// Float scale = getOptions().getImageScale();
		//
		// if (getOptions().isSignatureAutomaticSize()) {
		// signatureWidth = img.getWidth() * scale;
		// signatureHeight = img.getHeight() * scale;
		// } else {
		// // TODO: nesse caso, expande a imagem ao tamanho total da
		// // assinatura
		// scale = -1F;
		// }
		//
		// appearance.setSignatureGraphic(img);
		// // ap.setImage(img);
		// // ap.setLayer2Text("");
		//
		// offsetSignature();
		//
		// // TODO: descobrir pq o Acrobat 7 não mostra a imagem qdo na layer2
		// // addImageToLayer(ap.getLayer(2), img);
		//
		// } else if (renderMode.equals(RenderMode.GraphicAndDescription)) {
		//
		// appearance.setRenderingMode(RenderingMode.GRAPHIC_AND_DESCRIPTION);
		//
		// appearance.setSignatureGraphic(img);
		//
		// offsetSignature();
		//
		// } else if (renderMode.equals(RenderMode.Name)) {
		//
		// appearance.setRenderingMode(RenderingMode.DESCRIPTION);
		//
		// String name;
		//
		// if (getOptions().getImageCustom()) {
		// name = getOptions().getImageCustomText();
		// } else {
		// name = CertificateInfo.getSubjectFields(certificate).getField("CN");
		// }
		//
		// addTextToLayer(2, name);
		//
		// } else if (renderMode.equals(RenderMode.Description)) {
		//
		// appearance.setRenderingMode(RenderingMode.DESCRIPTION);
		// offsetSignature();
		//
		// } else if (renderMode.equals(RenderMode.NameAndDescription)) {
		//
		// appearance.setRenderingMode(RenderingMode.NAME_AND_DESCRIPTION);
		// offsetSignature();
		//
		// }
		//
		// if (getOptions().hasDescription()) {
		//
		// // TODO: implementar description
		//
		// PlainDescription desc = getOptions().getDescription();
		//
		// String name =
		// CertificateInfo.getSubjectFields(certificate).getField("CN");
		//
		// desc.setName(name);
		//
		// // StringTemplate description = new
		// // StringTemplate(desc.getTemplate());
		// // description.setAttribute("description", desc);
		//
		// if (desc.getLocation() != null) {
		// appearance.setLocation(desc.getLocation());
		// }
		//
		// if (desc.getReason() != null) {
		// appearance.setReason(desc.getReason());
		// }
		//
		// // appearance.setLayer2Text(desc.toString());
		//
		// }

	}

	protected void offsetSignature() throws IOException {

		// Float pLeft;
		// Float pRight;
		// Float pTop;
		// Float pBottom;
		//
		// Integer pageToSign = getOptions().getPageToSign(reader);
		// Rectangle pageSize = reader.getPageSize(pageToSign);
		//
		// Float pageHeight = pageSize.getHeight();
		// Float pageWidth = pageSize.getWidth();
		//
		// if (getOptions().isAbsolute()) {
		//
		// pBottom = cmToPoint(getOptions().getFooterDistance() +
		// getOptions().getMarginBottom());
		//
		// pTop = pBottom + signatureHeight;
		//
		// } else {
		//
		// Float marginBottom = pageHeight -
		// cmToPoint(getOptions().getMarginBottom());
		//
		// Float lastLineDistance =
		// cmToPoint(getOptions().getLastLineDistance());
		//
		// Float yOffset;
		//
		// if (getOptions().getReferenceLastLine()) {
		// yOffset = textPosition.getLastLinePosition(pageToSign, marginBottom);
		// } else {
		//
		// yOffset = textPosition.getCharacterPosition(pageToSign, marginBottom,
		// getOptions()
		// .getReferenceText(), true);
		//
		// }
		//
		// Float sigHeight = signatureHeight;
		//
		// if (lastLineDistance < 0) {
		// sigHeight = 0F;
		// }
		//
		// pTop = yOffset + lastLineDistance + sigHeight;
		//
		// pBottom = pTop - signatureHeight;
		//
		// }
		//
		// if (getOptions().getSignatureAlign().equals("center")) {
		// pLeft = pageWidth / 2 - signatureWidth / 2;
		// } else if (getOptions().getSignatureAlign().equals("left")) {
		// pLeft = cmToPoint(1.0F);
		// } else {
		// pLeft = pageWidth - cmToPoint(0.5F) - signatureWidth;
		// }
		//
		// pRight = pLeft + signatureWidth;
		//
		// Rectangle pCoords = new Rectangle(pLeft, pBottom, pRight, pTop);
		//
		// appearance.setVisibleSignature(pCoords, pageToSign, null);

	}

	public void addImageToLayer(PdfTemplate n2, Image im) throws DocumentException,
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

		offsetSignature();

	}

	public void addTextToLayer(Integer layer, String text) throws IOException, DocumentException {

		Float fontSize = 10F;

		BaseFont bf = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
		Float tWidth = bf.getWidthPoint(text, fontSize);

		// TODO: Estudar altura de caracteres para melhor determinação da margem
		// 1.2F = 20% de margem de segurança para caracteres fora de linha
		signatureHeight = fontSize * 1.2F;
		signatureWidth = tWidth + 2F;

		offsetSignature();

		PdfTemplate n2 = appearance.getLayer(layer);

		n2.beginText();
		n2.setFontAndSize(bf, fontSize);
		n2.showTextAligned(PdfTemplate.ALIGN_CENTER, text, signatureWidth / 2, signatureHeight
			- fontSize, 0);
		n2.endText();

	}

}
