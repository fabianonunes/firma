package tc.fab.pdf.signer;

import java.io.InputStream;
import java.security.GeneralSecurityException;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.security.ExternalBlankSignatureContainer;

public class BlankContainer extends ExternalBlankSignatureContainer {

	private PostSign postSign;

	public BlankContainer(PostSign postSign) {
		super(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
		this.postSign = postSign;
	}

	@Override
	public byte[] sign(InputStream data) throws GeneralSecurityException {
		postSign.save(data);
		return new byte[0];
	}

	public interface PostSign {
		public void save(InputStream data) throws GeneralSecurityException;
		public byte[] read();
	}

}
