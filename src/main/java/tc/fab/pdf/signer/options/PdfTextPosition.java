package tc.fab.pdf.signer.options;

import java.io.IOException;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

public class PdfTextPosition {

	private PdfReaderContentParser parser;
	private Integer pageNumber;
	private Rectangle pageSize;

	public PdfTextPosition(PdfReader reader, Integer pageNumber) throws IOException {
		this.parser = new PdfReaderContentParser(reader);
		this.pageNumber = pageNumber;
		this.pageSize = reader.getPageSize(pageNumber);
	}

	public Float getCharacterPosition(Float marginBottom, String reference, Boolean lastOccurrence)
		throws IOException {
		PositionStrategy strategy = new PositionStrategy();
		strategy = parser.processContent(pageNumber, strategy);
		return strategy.getReferencePosition(marginBottom, reference, lastOccurrence);
	}

	public Float getCharacterPosition(String reference) throws IOException {
		return getCharacterPosition(pageSize.getHeight(), reference, true);
	}

	public Float getLastLinePosition(Float marginBottom) throws IOException {
		PositionStrategy strategy = new PositionStrategy();
		strategy = parser.processContent(pageNumber, strategy);
		return strategy.getLastLinePosition(marginBottom);
	}

	public String getText(int pageNumber) throws IOException {
		PositionStrategy strategy = new PositionStrategy();
		strategy = parser.processContent(pageNumber, strategy);
		return strategy.getReturnedText();
	}

	public Float getLastLinePosition() throws IOException {
		return getLastLinePosition(pageSize.getHeight());
	}

}
