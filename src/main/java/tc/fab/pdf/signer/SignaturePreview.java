package tc.fab.pdf.signer;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.RescaleOp;
import java.security.cert.Certificate;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.Ostermiller.util.CircularByteBuffer;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

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

	public BufferedImage getImagePreview() throws Exception {

		CircularByteBuffer createBuffer = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
		CircularByteBuffer signBuffer = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);

		Document doc = new Document(new Rectangle(width, height));
		PdfWriter writer = PdfWriter.getInstance(doc, createBuffer.getOutputStream());
		doc.open();
		doc.newPage();
		writer.setPageEmpty(false);
		doc.close();
		createBuffer.getOutputStream().close();

		PdfReader reader = new PdfReader(createBuffer.getInputStream());
		PdfStamper stamper = PdfStamper.createSignature(reader, signBuffer.getOutputStream(), '\0');

		Signer signer = new Signer(null);
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
		appearance.setVisibleSignature(new Rectangle(0, 0, width, height), 1, "Signature1");
		appearance.setRenderingMode(RenderingMode.NAME_AND_DESCRIPTION);

		// BufferedImage img = ImageIO.read(new File("/tmp/ram/im.png"));
		// img = transform(img, 0.2f);
		//
		// Image image = Image.getInstance(img, null);
		//
		// appearance.setImage(image);
		// appearance.setImageScale(-1f);
		appearance.setCertificate(cert);
		signer.getSignableStream(appearance, new Certificate[] { cert });
		signBuffer.getOutputStream().close();

		PDDocument pddoc = PDDocument.load(signBuffer.getInputStream());
		PDPage page = (PDPage) pddoc.getDocumentCatalog().getAllPages().get(0);

		BufferedImage buffered = page.convertToImage();

		pddoc.close();
		createBuffer.getOutputStream().close();
		signBuffer.getInputStream().close();

		return buffered;

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
