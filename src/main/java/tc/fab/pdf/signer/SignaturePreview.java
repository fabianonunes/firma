package tc.fab.pdf.signer;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import tc.fab.pdf.signer.options.AppearanceOptions;

import com.Ostermiller.util.CircularByteBuffer;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class SignaturePreview implements AutoCloseable {

	private Certificate cert;
	float height;
	float width;
	private PdfWriter writer;
	private PdfStamper stamper;
	private PDDocument pddoc;

	public static BufferedImage generate(Certificate cert, Dimension dimension,
		AppearanceOptions options) {

		try (SignaturePreview preview = new SignaturePreview(cert, dimension)) {
			return preview.getImagePreview(options);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	protected SignaturePreview(Certificate cert, Dimension dimension) throws CertificateException {
		this.cert = (cert != null) ? cert : sampleCert();
		// PDFBox generates the page images in double screen resolution
		height = dimension.height / 2;
		width = dimension.width / 2;
	}

	private Certificate sampleCert() throws CertificateException {
		return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(
			new ByteArrayInputStream(this.certData.getBytes()));
	}

	// public Image getImagePreview_(AppearanceOptions options) throws
	// DocumentException, IOException,
	// GeneralSecurityException {
	//
	// CircularByteBuffer createBuffer = new
	// CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
	//
	// try (InputStream in = createBuffer.getInputStream();
	// OutputStream out = createBuffer.getOutputStream();) {
	//
	// Document doc = new Document(new Rectangle(width, height));
	// writer = PdfWriter.getInstance(doc, out);
	// doc.open();
	// doc.newPage();
	// writer.setPageEmpty(false);
	// doc.close();
	//
	// PdfReader reader = new PdfReader(in);
	// createBuffer.clear();
	//
	// stamper = PdfStamper.createSignature(reader, out, '\0');
	//
	// PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
	// appearance.setCertificate(cert);
	//
	// options.apply(appearance, new Rectangle(4, 4, width - 2, height - 2),
	// "Signature1");
	//
	// SteppedSigner.getSignableStream(appearance, new Certificate[] { cert });
	//
	// PdfDecoder decoder = new PdfDecoder();
	// decoder.openPdfFileFromInputStream(in, true);
	//
	// return decoder.getPageAsImage(1);
	//
	//
	//
	// } catch (Exception e) {
	// System.out.println(e);
	// }
	// return null;
	//
	// }

	public BufferedImage getImagePreview(AppearanceOptions options) throws DocumentException,
		IOException, GeneralSecurityException {

		CircularByteBuffer createBuffer = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);

		try (InputStream in = createBuffer.getInputStream();
			OutputStream out = createBuffer.getOutputStream();) {

			Document doc = new Document(new Rectangle(width, height));
			writer = PdfWriter.getInstance(doc, out);
			doc.open();
			doc.newPage();
			writer.setPageEmpty(false);
			doc.close();

			PdfReader reader = new PdfReader(in);
			createBuffer.clear();

			stamper = PdfStamper.createSignature(reader, out, '\0');

			PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
			appearance.setCertificate(cert);

			options.apply(appearance, new Rectangle(4, 4, width - 2, height - 2), "Signature1");

			SteppedSigner.getSignableStream(appearance, new Certificate[] { cert });

			pddoc = PDDocument.load(in);
			PDPage page = (PDPage) pddoc.getDocumentCatalog().getAllPages().get(0);
			BufferedImage buffered = page.convertToImage();
			return buffered;

		}

	}

	public Certificate getCert() {
		return cert;
	}

	public void setCert(Certificate cert) {
		this.cert = cert;
	}

	@Override
	public void close() {
		if (writer != null) {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
		if (stamper != null) {
			try {
				stamper.close();
			} catch (Exception e) {
			}
		}
		if (pddoc != null) {
			try {
				pddoc.close();
			} catch (Exception e) {
			}
		}
	}

	private String certData = "-----BEGIN CERTIFICATE-----\n"
		+ "MIIFuzCCA6OgAwIBAgIJAN+wgi0eUfu1MA0GCSqGSIb3DQEBBQUAMHQxCzAJBgNV\n"
		+ "BAYTAkZSMREwDwYDVQQIDAhOb3JtYW5keTEZMBcGA1UEBwwQQmVhdW1vbnQtZW4t\n"
		+ "QXVnZTEYMBYGA1UECgwPRWNvbGUgTWlsaXRhaXJlMR0wGwYDVQQDDBRQaWVycmUt\n"
		+ "U2ltb24gTGFwbGFjZTAeFw0xMzAzMjgyMzE5MTdaFw0xODAzMjgyMzE5MTdaMHQx\n"
		+ "CzAJBgNVBAYTAkZSMREwDwYDVQQIDAhOb3JtYW5keTEZMBcGA1UEBwwQQmVhdW1v\n"
		+ "bnQtZW4tQXVnZTEYMBYGA1UECgwPRWNvbGUgTWlsaXRhaXJlMR0wGwYDVQQDDBRQ\n"
		+ "aWVycmUtU2ltb24gTGFwbGFjZTCCAiIwDQYJKoZIhvcNAQEBBQADggIPADCCAgoC\n"
		+ "ggIBALAaiZmyTQkgpJCpteggf3qV3hTmJCTvEF8zsTYvnRZe3OYmQU67wQ9ar/CF\n"
		+ "qOpocv3QzKVg/B8D88p0+/Q5MbRz1gF+Sel7l1kK4TQRYQUV3UyndD5TMBEkKyWh\n"
		+ "6Rgq18fXGh6klOyxTN5oCc5vRw/AUZJ0v+Q9EEZqdti0JfODFaaXHjY+J1iAdXN6\n"
		+ "p4oK3NpNoBXOiH0DLtKJoZdxhhFHzA38GiYwtkdw93xwxxmu6ei+ZYpu1MhY9ep5\n"
		+ "WH1K+EuRbiCZ2bkHKE5ZcEK6tmx4WLLjbBjy9PtZCQHDJxxLbhsWNEleNk0KqqO3\n"
		+ "UdEuT5l541Ruef9jhYlsbp9nKYO1eLnSFkGs5xt88vzgohcP+LzpkSr3WSHXkffx\n"
		+ "mnHsZWQdUQlk7R0z6u12fliT1n0Ojp8PffvyNFf60eW4RMygD1uKijRy+WcdTIcW\n"
		+ "q1F74OjT0XB20rhRnh4KvrW+Kc+yG/e5LpwTG9c4KqHJbGzpf/i+9AGEFVXcQORY\n"
		+ "9H2v5fNMKw6mzzUivmlYNyDCKyJaYskN8jinNpj6ZapXdGRusNHBE9ydTSDOrobC\n"
		+ "T64pbtcFZPn7MgDlmcXz+pcZhd7UtuFP+f2Ve4JU74gZeJ88WSA3Go5v9f/4SyGu\n"
		+ "Xibjm85j2XXCJ1cpgehdnIgt8j4kRlwZgl4MKuUrJedRO2R5AgMBAAGjUDBOMB0G\n"
		+ "A1UdDgQWBBSucshZppRKVJNC5WrMK+JQ46VCATAfBgNVHSMEGDAWgBSucshZppRK\n"
		+ "VJNC5WrMK+JQ46VCATAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBBQUAA4ICAQBn\n"
		+ "0TOBLGE9XISbAsaGGg3s1d+GNalnP7JhmhmFS+iD1uaEHZavSIST7MMF+UpN/ipd\n"
		+ "5G91RfeYqs34gU6JUq4RBlj0q0qTked0uXYkmFWH7sFcQbqEWkVrdHu7BFu4Vja2\n"
		+ "2wYbsJL4OM9kbQrNXF+TF+RWjTClKAtlD4xZREH/bArzoOmI8fzcAefZ/O+v1kuX\n"
		+ "L1TdstB0yZblbniivUnI66eg9C9edRJzWso+GhzWuxCH8jd7f5BLmWY6Nh2mMBnB\n"
		+ "LLxKPh4zZawWO7u+XbBvM57Gy+S4aXQLV1HEwJCiAb6V7JKahVtdAcIv4G5L/59V\n"
		+ "DAIvz9WfrsMwFOnyE8m0HTnj8RXLSOvZDc0fzWE840S2BYZw6X85ioz3XHqGrxpc\n"
		+ "eG0Miu0dqgkRp+Dnn3liy8pbJ5z0PS4Lnm9UrT2SIBHKWH8QDKFAl9osCDbmmP8+\n"
		+ "dBS9NBPjHK/AU/CdEFyHwnfIEZRkZpi8tWvOcnNG6H/SyhSl328+TDjmPN6dQWru\n"
		+ "6cnbKZNFIxnZcKT2RoyKKoVHwm0yC9dXVrkf3EYnKmIHHwwQAM0nmumIFgyyshYx\n"
		+ "W1Z16u2xrhhjgXfzGyjbsLWZg1P49534gMcvQWDfIKdACIPme2vgJ9UTST0QkvFf\n"
		+ "WCBsdiARqRTYfuZXsE2W6nKC+FK3ruG5DYbnezt8Xw==\n" + "-----END CERTIFICATE-----";

}
