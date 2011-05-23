package tc.fab.file.selector.filters;

import java.io.File;
import java.util.Arrays;

import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FilenameUtils;

public class ExtensionFilter extends FileFilter {

	private String[] acceptables;
	private String description;

	public ExtensionFilter(String[] acceptables) {
		setAcceptables(acceptables);
	}

	public ExtensionFilter(String[] acceptables, String description) {
		setAcceptables(acceptables);
		setDescription(description);
	}

	@Override
	public boolean accept(File file) {

		if (getAcceptables() != null) {
			
			String ext = FilenameUtils.getExtension(file.getName()); 

			if (file.isDirectory()) {
				return true;
			}

			for (String extension : getAcceptables()) {
				if (ext.toLowerCase().equals(extension.toLowerCase())) {
					return true;
				}
			}

		}

		return false;
	}

	@Override
	public String getDescription() {
		if (description != null) {
			return description;
		} else {
			if (acceptables != null) {
				return Arrays.toString(acceptables);
			}
		}
		return "";
	}

	public void setAcceptables(String[] acceptables) {
		this.acceptables = acceptables;
	}

	public String[] getAcceptables() {
		return acceptables;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
