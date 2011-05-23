package tc.fab.pdf.signer;

import java.io.File;
import java.io.FileNotFoundException;

import net.iharder.dnd.FileDrop;
import tc.fab.file.selector.DirectorySelector;
import tc.fab.file.selector.FileSelector;
import tc.fab.pdf.signer.application.PDFSignerApp;
import tc.fab.pdf.signer.options.SignerOptions;

public class FileDropListener implements FileDrop.Listener {

	FileDrop.Listener listener;

	private FileDropListener() {
	}

	private static class Holder {
		private static final FileDropListener listener = new FileDropListener();
	}

	public static FileDropListener getInstance() {
		return Holder.listener;
	}

	@Override
	public void filesDropped(File[] files) {

		SignerOptions opt = PDFSignerApp.getApplication().getOptions();

		FileSelector fs = new FileSelector();
		fs.setFilter(PDFSignerApp.acceptedFiles);

		DirectorySelector ds = new DirectorySelector();
		ds.setFilter(PDFSignerApp.acceptedFiles);
		ds.setRecursive(Boolean.TRUE);

		for (File file : files) {

			if (file.isDirectory()) {
				try {
					ds.addSource(file);
				} catch (FileNotFoundException e) {
					// Quietly
				}
			} else {
				try {
					fs.addSource(file);
				} catch (FileNotFoundException e) {
					// Quietly
				}
			}

		}

		try {
			opt.addSelection(ds);
			opt.addSelection(fs);
		} catch (FileNotFoundException e) {
			// Quietly
		}
		
	}

}
