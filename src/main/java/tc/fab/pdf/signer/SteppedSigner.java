package tc.fab.pdf.signer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.util.Calendar;
import java.util.Date;

import tc.fab.pdf.signer.BlankContainer.PostSign;
import tc.fab.pdf.signer.message.Envelope;
import tc.fab.pdf.signer.message.SignatureClaim;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignatureContainer;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.pdf.security.ProviderDigest;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;

public class SteppedSigner {

	public static Envelope getSignableStream(PdfSignatureAppearance appearance, Certificate[] chain)
		throws GeneralSecurityException, IOException, DocumentException {

		final ExternalDigest externalDigest = new ProviderDigest(null);
		final String hashAlgorithm = "SHA1";
		PdfPKCS7 sgn = new PdfPKCS7(null, chain, hashAlgorithm, null, externalDigest, false);

		PostSign postSign = new PostSign() {

			byte[] hash = null;

			@Override
			public void save(InputStream dataToSign) throws GeneralSecurityException {
				try {
					hash = DigestAlgorithms.digest(dataToSign,
						externalDigest.getMessageDigest(hashAlgorithm));
				} catch (Exception e) {
					throw new GeneralSecurityException(e);
				}
			}

			@Override
			public byte[] read() {
				return hash;
			}

		};

		BlankContainer container = new BlankContainer(postSign);

		MakeSignature.signExternalContainer(appearance, container, 15000);

		Calendar cal = Calendar.getInstance();

		byte[] hash = postSign.read();
		byte[] dataToSign = sgn.getAuthenticatedAttributeBytes(hash, cal, null, null,
			CryptoStandard.CMS);

		return new Envelope(hash, dataToSign, cal.getTimeInMillis());

	}

	public static void signDeferred(PdfReader reader, File output, final SignatureClaim claim)
		throws Exception {

		MakeSignature.signDeferred(reader, "Signature1", new FileOutputStream(output),
			new ExternalSignatureContainer() {

				@Override
				public byte[] sign(InputStream data) throws GeneralSecurityException {

					ExternalDigest externalDigest = new ProviderDigest(null);
					PdfPKCS7 sgn = new PdfPKCS7(null, claim.getChain(), "SHA1", null,
						externalDigest, false);
					sgn.setExternalDigest(claim.getMessage(), null, "RSA");

					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date(claim.getTime()));

					return sgn.getEncodedPKCS7(claim.getHash(), cal, null, null, null,
						CryptoStandard.CMS);

				}

				@Override
				public void modifySigningDictionary(PdfDictionary signDic) {
				}
			});

	}

}
