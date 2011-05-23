package tc.fab.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.Normalizer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;
import org.apache.pdfbox.util.TextPositionComparator;

/**
 * Devido a quantidade de bugs em recuperar alguns trechos de textos, essa
 * classe será desativada em benefício de uma implementação semelhante em iText
 * 
 * @deprecated
 * @author fabiano
 * 
 */
public class PdfboxPDFTextPosition {

	PDDocument pddoc;

	Comparator<TextPosition> comparator;

	@SuppressWarnings("unchecked")
	public PdfboxPDFTextPosition(PDDocument pddoc) {
		this.pddoc = pddoc;
		comparator = new TextPositionComparator();
	}

	@SuppressWarnings("unchecked")
	public Float getCharacterPosition(Integer pageNumber, Float marginBottom,
			String reference, Boolean lastOccurrence) throws IOException {

		reference = Normalizer.normalize(reference, Normalizer.Form.NFD);
		reference = reference.replaceAll("[^\\p{ASCII}]", "");
		reference = reference.replaceAll("\\s+", "").trim().toLowerCase();

		List<PDPage> pages = pddoc.getDocumentCatalog().getAllPages();

		PDPage page = pages.get(pageNumber - 1);

		Float marginFooter = getPageHeight(page) - marginBottom;

		Vector<List<TextPosition>> text = getTextPositions(pageNumber);

		StringBuffer buf = new StringBuffer();

		TreeSet<Float> encounters = new TreeSet<Float>();

		String charFound;

		for (List<TextPosition> list : text) {

			Collections.sort(list, comparator);

			for (TextPosition textPosition : list) {

				if (textPosition.getY() < marginFooter) {

					if (textPosition.getCharacter().trim().length() > 0) {

						charFound = Normalizer.normalize(
								textPosition.getCharacter(),
								Normalizer.Form.NFD);
						charFound = charFound.replaceAll("[^\\p{ASCII}]", "");
						charFound = charFound.trim().toLowerCase()
								.replaceAll("\\s+", "");

						buf.append(charFound);

						// TODO: optimize
						if (buf.lastIndexOf(reference) > -1) {
							encounters.add(textPosition.getY());
							buf = new StringBuffer();
						}

					}

				}

			}

			if (encounters.size() > 0) {
				if (lastOccurrence) {
					return Collections.max(encounters);
				} else {
					return Collections.min(encounters);
				}
			}

		}

		return getLastLinePosition(pageNumber, marginBottom);

	}

	private Vector<List<TextPosition>> getTextPositions(Integer pageNumber)
			throws IOException {

		Integer numberOfPages = pddoc.getNumberOfPages();

		if (pageNumber < 0) {

			pageNumber = numberOfPages + pageNumber + 1;

		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Writer output = new OutputStreamWriter(baos);

		PDFTextStripperAux stripper = new PDFTextStripperAux();
		stripper.setStartPage(pageNumber);
		stripper.setEndPage(pageNumber + 1);
		stripper.writeText(pddoc, output);

		output.close();

		return stripper.getCharactersByArticle();

	}

	public String getText(Integer pageNumber) throws IOException {

		Integer numberOfPages = pddoc.getNumberOfPages();

		if (pageNumber < 0) {

			pageNumber = numberOfPages + pageNumber + 1;

		}

		pageNumber = Math.max(1, Math.min(pageNumber, numberOfPages));

		Writer output = new StringWriter();

		PDFTextStripper stripper = new PDFTextStripper();
		stripper.setStartPage(pageNumber);
		stripper.setEndPage(pageNumber + 1);
		stripper.writeText(pddoc, output);

		output.close();

		return output.toString();

	}

	@SuppressWarnings("unchecked")
	public Float getLastLinePosition(Integer pageNumber, Float marginBottom)
			throws IOException {

		List<PDPage> pages = pddoc.getDocumentCatalog().getAllPages();

		PDPage page = pages.get(pageNumber - 1);

		Vector<List<TextPosition>> text = getTextPositions(pageNumber);

		Set<Float> ys = new TreeSet<Float>();

		Float pageHeight = getPageHeight(page);

		Float marginFooter = pageHeight - marginBottom;

		for (List<TextPosition> list : text) {

			Collections.sort(list, comparator);

			for (TextPosition textPosition : list) {

				if (textPosition.getY() < marginFooter) {
					if (textPosition.getCharacter().trim().length() > 0) {
						ys.add(textPosition.getY());
					}
				}

			}

		}

		return Collections.max(ys);

	}

	private Float getPageHeight(PDPage page) {

		Float pageHeight;
		if (page.getMediaBox() == null) {
			pageHeight = pddoc.getDocumentCatalog().getPages().getMediaBox()
					.getHeight();
		} else {
			pageHeight = page.getMediaBox().getHeight();
		}

		return pageHeight;

	}

}
