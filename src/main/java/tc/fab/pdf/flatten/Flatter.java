package tc.fab.pdf.flatten;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class Flatter implements AutoCloseable {
	
	private PdfReader reader;
	private PdfStamper stamper;

	public Flatter() {
	}
	
	public void flat(File input) throws IOException, DocumentException  {

		File tempFile = new File(input.getParentFile(), RandomStringUtils.randomAlphanumeric(10));

		try (FileOutputStream fos = new FileOutputStream(tempFile)) {
			
			reader = new PdfReader(input.getAbsolutePath());
			stamper = new PdfStamper(reader, fos);
			stamper.setFormFlattening(true);
			stamper.close();
			tempFile.renameTo(input);
			
		} finally {
			
			if (tempFile != null) {
				FileUtils.deleteQuietly(tempFile);
			}
			
		}

	}
	
	@Override
	public void close() {
		if (reader != null) {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
	}
}
