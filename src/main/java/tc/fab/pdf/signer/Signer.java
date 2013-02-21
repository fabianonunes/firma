package tc.fab.pdf.signer;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;

import tc.fab.mechanisms.Mechanism;

import com.google.inject.Inject;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.TSAClient;
import com.itextpdf.text.pdf.security.TSAClientBouncyCastle;

public class Signer {

	private Mechanism mechanism;

	@Inject
	public Signer(Mechanism mechanism) throws KeyStoreException,
			UnrecoverableKeyException, NoSuchAlgorithmException {

		this.mechanism = mechanism;

	}

	public void sign(PdfSignatureAppearance appearance, boolean timestamp)
			throws IOException, DocumentException, GeneralSecurityException {

		this.mechanism.login();

		TSAClient tsc = null;
		if (timestamp) {
			String tsa_url = "http://tsa.swisssign.net";
			tsc = new TSAClientBouncyCastle(tsa_url, "", "");
		}

		ExternalSignature external = new PrivateKeySignature(
				mechanism.getPrivateKey(), "SHA-1", null);

		MakeSignature.signDetached(appearance, external,
				mechanism.getCertificateChain(), null, null, tsc, null, 15000,
				MakeSignature.CMS);

	}

	public X509Certificate getCertificate() throws KeyStoreException {
		return mechanism.getCertificate();
	}

}
