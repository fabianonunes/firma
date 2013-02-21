package tc.fab.file.selector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.filechooser.FileFilter;

public class FileSelector implements IFileSelection {

    private Vector<File> files = new Vector<File>();
    private FileFilter filter;

    public FileSelector() {
    }

    @Override
    public Vector<String> getChildNames() throws FileNotFoundException {
	Vector<String> vs = new Vector<String>();
	for (File file : getFiles()) {
	    vs.add(file.getAbsolutePath());
	}
	return vs;
    }

    @Override
    public Vector<File> getChilds() {
	Vector<File> vc = new Vector<File>();
	for (File iFile : getFiles()) {
	    vc.add(iFile);
	}
	return vc;
    }

    @Override
    public void addSource(File source) throws FileNotFoundException {
	if (source.exists()) {
	    if (getFilter().accept(source)) {
		getFiles().add(source);
	    }
	} else {
	    throw new FileNotFoundException(source + " not found");
	}
    }

    public void addSource(File[] files) {

	Vector<File> vfiles = new Vector<File>(Arrays.asList(files));

	// Quietly
	for (File file : vfiles) {
	    try {
		if (getFilter() != null) {
		    if (getFilter().accept(file)) {
			addSource(file);
		    }
		}
	    } catch (FileNotFoundException e) {
	    }
	}

    }

    public Vector<File> getFiles() {
	return files;
    }

    public void setFilter(FileFilter filter) {
	this.filter = filter;
    }

    public FileFilter getFilter() {
	return filter;
    }

    @Override
    public void removeSource(File source) {
	files.removeElement(source);

    }
}
