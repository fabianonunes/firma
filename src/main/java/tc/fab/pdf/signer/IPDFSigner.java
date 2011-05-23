package tc.fab.pdf.signer;

import java.io.File;

public interface IPDFSigner {
	
	void sign(File file) throws Exception;

}
