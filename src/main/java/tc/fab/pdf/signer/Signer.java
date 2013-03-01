package tc.fab.pdf.signer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Calendar;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import tc.fab.mechanisms.Mechanism;

import com.google.inject.Inject;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.ExternalSignatureContainer;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
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

	public void signBlank(PdfSignatureAppearance appearance, boolean timestamp) throws Exception {

		this.mechanism.login();

		TSAClient tsc = null;
		if (timestamp) {
			String tsa_url = "http://tsa.swisssign.net";
			tsc = new TSAClientBouncyCastle(tsa_url, "", "");
		}

		ExternalSignature external = new PrivateKeySignature(mechanism.getPrivateKey(), "SHA-1",
			null);

		BlankContainer container = new BlankContainer(PdfName.ADOBE_PPKLITE,
			PdfName.ADBE_PKCS7_DETACHED);

		MakeSignature.signExternalContainer(appearance, container, 15000);

		// System.out.println(
		// Hex.encodeHex(
		// IOUtils.toByteArray(container.getData())
		// )
		// );

	}

	public void signDeferred(PdfReader reader, boolean timestamp) throws Exception {
		
		MakeSignature.signDeferred(reader, "Signature1", new FileOutputStream(new File(
			"/tmp/ram/fout.pdf")), new ExternalSignatureContainer() {

			@Override
			public byte[] sign(InputStream data) throws GeneralSecurityException {
				
				
				try {

					Mechanism m = Signer.this.mechanism;
					InputStream is = new FileInputStream(new File("/tmp/ram/data_to_sign"));
					m.setAlias(m.aliases().get(0));
					
					Signature signature = Signature.getInstance("SHA1withRSA");
					signature.initSign(m.getPrivateKey());

					String hashAlgorithm = "SHA1";
					ExternalDigest externalDigest = new ProviderDigest(null);
					PdfPKCS7 sgn = new PdfPKCS7(null, mechanism.getCertificateChain(), hashAlgorithm, null, externalDigest, false);

					byte hash[] = DigestAlgorithms.digest(data, externalDigest.getMessageDigest(hashAlgorithm));
			        Calendar cal = Calendar.getInstance();
			        byte[] ocsp = null;
			        byte[] sh = sgn.getAuthenticatedAttributeBytes(hash, cal, ocsp, null, CryptoStandard.CMS);
			        signature.update(sh);
			        byte[] extSignature = signature.sign();
			        sgn.setExternalDigest(extSignature, null, "RSA");

			        return sgn.getEncodedPKCS7(hash, cal, null, ocsp, null, CryptoStandard.CMS);

					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			}

			@Override
			public void modifySigningDictionary(PdfDictionary signDic) {
				PdfSignature sign = (PdfSignature) signDic;
				try {
					sign.setCert(mechanism.getCertificate().getEncoded());
				} catch (CertificateEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// TODO Auto-generated method stub

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
