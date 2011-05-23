package tc.fab.pdf.signer.options;

import java.io.IOException;
import java.net.URISyntaxException;

public interface ISignerOptionsDialog {
	
	public void loadOptions(SignerOptions options);
	
	public SignerOptions saveOptions() throws IOException, URISyntaxException;
	
	public SignerOptions getOptions();
	
	public void setOptions(SignerOptions options);

}
