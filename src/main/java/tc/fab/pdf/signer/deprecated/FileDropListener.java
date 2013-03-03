package tc.fab.pdf.signer.deprecated;

import java.io.File;
import java.io.FileNotFoundException;

import tc.fab.file.selector.DirectorySelector;
import tc.fab.file.selector.FileSelector;

@Deprecated
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

		FileSelector fs = new FileSelector();
		// fs.setFilter(PDFSignerApp.acceptedFiles);

		DirectorySelector ds = new DirectorySelector();
		// ds.setFilter(PDFSignerApp.acceptedFiles);
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

		// try {
		// opt.addSelection(ds);
		// sopt.addSelection(fs);
		// } catch (FileNotFoundException e) {
		// Quietly
		// }

	}

}
