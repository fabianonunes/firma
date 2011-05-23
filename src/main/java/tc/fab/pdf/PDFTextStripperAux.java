package tc.fab.pdf;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

/**
 * Devido a quantidade de bugs em recuperar alguns trechos de textos, essa
 * classe será desativada em benefício de uma implementação semelhante em iText
 * 
 * @deprecated
 * @author fabiano
 * 
 */
public class PDFTextStripperAux extends PDFTextStripper {

	public PDFTextStripperAux() throws IOException {
		super();
	}

	public Vector<List<TextPosition>> getCharactersByArticle() {
		return charactersByArticle;
	}

}
