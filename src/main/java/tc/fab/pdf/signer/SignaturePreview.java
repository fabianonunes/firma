package tc.fab.pdf.signer;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.Ostermiller.util.CircularByteBuffer;
import com.google.common.base.Joiner;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.security.CertificateInfo;

public class SignaturePreview {

	private Certificate cert;
	float height;
	float width;

	public SignaturePreview(Certificate cert, Dimension dimension) throws Exception {
		this.cert = cert;
		// PDFBox generates the page images in double screen resolution
		height = dimension.height / 2;
		width = dimension.width / 2;

	}

	public BufferedImage getImagePreview() throws DocumentException, IOException,
		GeneralSecurityException {

		CircularByteBuffer createBuffer = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);

		Signer signer = new Signer(null);

		try (InputStream in = createBuffer.getInputStream();
			OutputStream out = createBuffer.getOutputStream();) {

			Document doc = new Document(new Rectangle(width, height));
			PdfWriter writer = PdfWriter.getInstance(doc, out);
			doc.open();
			doc.newPage();
			writer.setPageEmpty(false);
			doc.close();

			PdfReader reader = new PdfReader(in);
			createBuffer.clear();

			Map<String, ArrayList<String>> values = CertificateInfo.getSubjectFields((X509Certificate) cert).getFields();
			String layer2Text = Joiner.on(", ").withKeyValueSeparator("=").join(values);

			PdfStamper stamper = PdfStamper.createSignature(reader, out, '\0');

			PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
			appearance.setVisibleSignature(new Rectangle(0, 0, width, height), 1, "Signature1");
			appearance.setCertificate(cert);
			appearance.setRenderingMode(RenderingMode.NAME_AND_DESCRIPTION);
			appearance.setLayer2Text(layer2Text);
			signer.getSignableStream(appearance, new Certificate[] { cert });
			
			PDDocument pddoc = PDDocument.load(in);
			PDPage page = (PDPage) pddoc.getDocumentCatalog().getAllPages().get(0);
			BufferedImage buffered = page.convertToImage();
			pddoc.close();
			return buffered;
		}

	}

	protected BufferedImage transform(BufferedImage img, float alpha) {
		BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null),
			BufferedImage.TYPE_INT_ARGB);
		bi.getGraphics().drawImage(img, 0, 0, null);
		BufferedImageOp rop = new RescaleOp(new float[] { 1.0f, 1.0f, 1.0f, alpha }, new float[] {
			0.0f, 0.0f, 0.0f, 0.0f }, null);
		BufferedImage result;
		result = rop.filter(bi, null);
		return result;
	}

	public Certificate getCert() {
		return cert;
	}

	public void setCert(Certificate cert) {
		this.cert = cert;
	}

}
