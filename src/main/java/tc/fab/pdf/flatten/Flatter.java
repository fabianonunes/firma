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
	private File tempFile;

	public Flatter() {
	}
	
	public void flat(File input) throws IOException, DocumentException  {

		File output = new File(input.getAbsolutePath());
		tempFile = new File(input.getAbsolutePath() + "_" + RandomStringUtils.randomAlphanumeric(10));
		FileUtils.moveFile(input, tempFile);
		input = tempFile;
		
		try (FileOutputStream fos = new FileOutputStream(output)) {
			
			reader = new PdfReader(input.getAbsolutePath());
			stamper = new PdfStamper(reader, fos);
			stamper.setFormFlattening(true);
			stamper.close();
			
		} catch (Exception e) {

			FileUtils.moveFile(tempFile, output);
			throw e;
			
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
