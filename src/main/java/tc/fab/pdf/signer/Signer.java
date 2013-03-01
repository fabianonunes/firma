package tc.fab.pdf.signer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Calendar;

import tc.fab.mechanisms.Mechanism;
import tc.fab.pdf.signer.BlankContainer.PostSign;

import com.google.inject.Inject;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.ExternalSignatureContainer;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.ProviderDigest;
import com.itextpdf.text.pdf.security.TSAClient;
import com.itextpdf.text.pdf.security.TSAClientBouncyCastle;

public class Signer {

	private Mechanism mechanism;

	@Inject
	public Signer(Mechanism mechanism) throws KeyStoreException, UnrecoverableKeyException,
		NoSuchAlgorithmException {

		this.mechanism = mechanism;

	}

	public byte[] signBlank(PdfSignatureAppearance appearance, Certificate[] chain)
		throws Exception {

		final ExternalDigest externalDigest = new ProviderDigest(null);
		final String hashAlgorithm = "SHA1";
		PdfPKCS7 sgn = new PdfPKCS7(null, chain, hashAlgorithm, null, externalDigest, false);

		PostSign postSign = new PostSign() {
			byte[] hash;

			@Override
			public void save(InputStream data) throws GeneralSecurityException {
				try {
					hash = DigestAlgorithms.digest(data,
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
		byte[] sh = sgn.getAuthenticatedAttributeBytes(hash, cal, null, null,
			CryptoStandard.CMS);
		
		byte[] extSignature = mechanism.sign(sh);
		
		//TODO: get algorithm from certificate
		String encryptionAlgorithm = "RSA";
		
        sgn.setExternalDigest(extSignature, null, encryptionAlgorithm);

        byte[] encodedSig = sgn.getEncodedPKCS7(hash, cal, null, null, null, CryptoStandard.CMS);

		return encodedSig;

	}

	public void signDeferred(PdfReader reader, File output, final byte[] signedHash,
		final Certificate certificate) throws Exception {

		MakeSignature.signDeferred(reader, "Signature1", new FileOutputStream(output),
			new ExternalSignatureContainer() {

				@Override
				public byte[] sign(InputStream data) throws GeneralSecurityException {
					return signedHash;
				}

				@Override
				public void modifySigningDictionary(PdfDictionary signDic) {
					PdfSignature sign = (PdfSignature) signDic;
					try {
						sign.setCert(certificate.getEncoded());
					} catch (CertificateEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

	}

	public void signDetached(PdfSignatureAppearance appearance, boolean timestamp) throws Exception {

		this.mechanism.login();

		TSAClient tsc = null;
		if (timestamp) {
			String tsa_url = "http://tsa.swisssign.net";
			tsc = new TSAClientBouncyCastle(tsa_url, "", "");
		}

		ExternalSignature external = new PrivateKeySignature(mechanism.getPrivateKey(), "SHA-1",
			null);

		ExternalDigest externalDigest = new ProviderDigest(null);

		MakeSignature.signDetached(appearance, externalDigest, external,
			mechanism.getCertificateChain(), null, null, tsc, 0, CryptoStandard.CMS);

	}

	public X509Certificate getCertificate() throws Exception {
		return mechanism.getCertificate();
	}

}
