package tc.fab.pdf.signer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.security.cert.Certificate;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.Ostermiller.util.CircularByteBuffer;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class SignaturePreview {

	private Certificate cert;

	public SignaturePreview(Certificate cert) throws Exception {
		this.cert = cert;
	}

	public BufferedImage getImagePreview() throws Exception {

		Float height = 50f;
		Float width = 200f;

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
		
		Image image = Image.getInstance("/tmp/ram/im.png");
		appearance.setImage(image);
		appearance.setImageScale(-1f);
		appearance.setCertificate(cert);
		signer.getSignableStream(appearance, new Certificate[] { cert });
		signBuffer.getOutputStream().close();

		PDDocument pddoc = PDDocument.load(signBuffer.getInputStream());
		PDPage page = (PDPage) pddoc.getDocumentCatalog().getAllPages().get(0);

		BufferedImage buffered = page.convertToImage();

		createBuffer.getOutputStream().close();
		signBuffer.getInputStream().close();

		return buffered;

	}

	public Certificate getCert() {
		return cert;
	}

	public void setCert(Certificate cert) {
		this.cert = cert;
	}

}
