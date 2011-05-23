package tc.fab.file.selector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;

import javax.swing.filechooser.FileFilter;

public interface IFileSelection {

    public Vector<String> getChildNames() throws FileNotFoundException;

    public Vector<File> getChilds() throws FileNotFoundException;

    public void addSource(File source) throws FileNotFoundException;

    public void setFilter(FileFilter filter);

    public FileFilter getFilter();
    
    public void removeSource(File source);
}
