package tc.fab.pdf;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

public class PDFTextPosition {

	private PdfReader reader;
	private PdfReaderContentParser parser;

	public PDFTextPosition(String filename) throws IOException {
		reader = new PdfReader(filename);
		parser = new PdfReaderContentParser(reader);
	}

	public PDFTextPosition(File file) throws IOException {
		this(file.getAbsolutePath());
	}

	public Float getCharacterPosition(Integer pageNumber, Float marginBottom,
			String reference, Boolean lastOccurrence) throws IOException {

		reference = Normalizer.normalize(reference, Normalizer.Form.NFD);
		reference = reference.replaceAll("[^\\p{ASCII}]", "");
		reference = reference.replaceAll("\\s+", "").trim().toLowerCase();

		PositionStrategy strategy = new PositionStrategy();
		strategy = parser.processContent(pageNumber, strategy);

		return strategy.getReferencePosition(marginBottom, reference, lastOccurrence);

	}

	public Float getLastLinePosition(Integer pageNumber, Float marginBottom)
			throws IOException {

		PositionStrategy strategy = new PositionStrategy();
		strategy = parser.processContent(pageNumber, strategy);

		return strategy.getLastLinePosition(marginBottom);

	}

	public String getText(int pageNumber) throws IOException {

		PositionStrategy strategy = new PositionStrategy();
		strategy = parser.processContent(pageNumber, strategy);

		return strategy.getReturnedText();

	}

}
