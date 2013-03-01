package tc.fab.pdf.signer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

import org.apache.commons.io.IOUtils;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.security.ExternalBlankSignatureContainer;

public class BlankContainer extends ExternalBlankSignatureContainer {

	private InputStream data;

	public BlankContainer(PdfName filter, PdfName subFilter) {
		super(filter, subFilter);
	}

	@Override
	public byte[] sign(InputStream data) throws GeneralSecurityException {
		this.setData(data);
		try {
			IOUtils.copy(data, new FileOutputStream(new File("/tmp/ram/data2")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return super.sign(data);
	}

	public InputStream getData() {
		return data;
	}

	public void setData(InputStream data) {
		this.data = data;
	}

}
