package tc.fab.firma.app.tasks;

import java.awt.image.BufferedImage;
import java.security.cert.Certificate;

import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.jdesktop.swingx.JXImageView;

import tc.fab.pdf.signer.SignaturePreview;
import tc.fab.pdf.signer.options.AppearanceOptions;

public class PreviewTask extends Task<BufferedImage, Void> {

	private Certificate cert;
	private AppearanceOptions options;
	private JXImageView imagePane;

	public PreviewTask(Application app, Certificate cert, JXImageView imagePane,
		AppearanceOptions options) {
		super(app);
		this.cert = cert;
		this.imagePane = imagePane;
		this.options = options;
	}

	@Override
	protected BufferedImage doInBackground() throws Exception {
		return SignaturePreview.generate(cert, imagePane.getSize(), options);
	}

	@Override
	protected void succeeded(BufferedImage result) {
		imagePane.setImage(result);
	}

	@Override
	protected void failed(Throwable cause) {
		// cause.printStackTrace();
		imagePane.setImage((BufferedImage) null);
	}

}
