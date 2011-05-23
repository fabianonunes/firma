package tc.fab.file.selector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.filechooser.FileFilter;

public class DirectorySelector implements IFileSelection {

	private List<File> dirs = Collections.synchronizedList(new Vector<File>());
	private Boolean recursive = false;
	private FileFilter filter;

	public DirectorySelector(File source, Boolean recursive)
			throws FileNotFoundException {
		addSource(source);
		setRecursive(recursive);
	}

	public DirectorySelector(){
		setRecursive(false);
	}
	
	public DirectorySelector(File source) throws FileNotFoundException {
		addSource(source);
	}


	public DirectorySelector(boolean b) {
		setRecursive(b);
	}

	@Override
	public Vector<String> getChildNames() throws FileNotFoundException {

		Vector<String> fileNames = new Vector<String>();

		Vector<File> files = getChilds();

		for (File file : files) {
			fileNames.add(file.getAbsolutePath());
		}

		return fileNames;

	}
	
	@Override
	public Vector<File> getChilds() throws FileNotFoundException {
		
		Vector<File> files = new Vector<File>();
		
		for (File dir : dirs) {
			
			files.addAll(getChildsFromSource(dir));
			
		}
		
		return files;
	}

	public Vector<File> getChildsFromSource(File source) throws FileNotFoundException {

		File _folder = source;

		Vector<String> filesInFolder = new Vector<String>();

		Vector<File> files = new Vector<File>();

		if (_folder.isDirectory()) {

			if (_folder.list() != null) {

				filesInFolder.addAll(Arrays.asList(_folder.list()));

				Iterator<String> itr = filesInFolder.iterator();

				while (itr.hasNext()) {

					String inFileName = itr.next().toString();

					File _fileName = new File(_folder.getAbsolutePath()
							+ File.separator + inFileName);

					if (_fileName.isDirectory()) {

						if (isRecursive()) {

							DirectorySelector inFilter = new DirectorySelector();
							inFilter.addSource(_fileName);
							inFilter.setRecursive(isRecursive());
							inFilter.setFilter(getFilter());

							files.addAll(inFilter.getChilds());
						}

					} else {

						if (getFilter() != null) {
							if (getFilter().accept(new File(inFileName))) {
								files.add(_fileName);
							}

						} else {
							files.add(_fileName);
						}

					}

				}

			}

			return files;

		} else {
			throw new FileNotFoundException(source + " isn't a directory");
		}

	}

	public void addSource(File source) throws FileNotFoundException {
		if (source.isDirectory()) {
			this.dirs.add(source);
		} else {
			throw new FileNotFoundException(source.toString());
		}
	}

	public void setRecursive(Boolean recursive) {
		this.recursive = recursive;
	}

	public Boolean isRecursive() {
		return recursive;
	}

	public void setFilter(FileFilter filter) {
		this.filter = filter;
	}

	public FileFilter getFilter() {
		return filter;
	}

	@Override
	public void removeSource(File source) {
		dirs.remove(source);
		
	}
	
	public List<File> getDirs(){
		return this.dirs;
	}

}
