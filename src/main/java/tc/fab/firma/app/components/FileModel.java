package tc.fab.firma.app.components;

import java.io.File;

import tc.fab.firma.utils.PropertyObservable;

public class FileModel extends PropertyObservable {

	public enum Status {
		IDLE, LOADING, DONE, FAILED
	};

	private Status status;
	private String fileType;
	private String fileName;
	private long fileSize;
	private File file;

	public FileModel(Status status, File file, long size) {
		super();
		this.status = status;
		this.fileName = file.getName();
		this.fileSize = size;
		this.fileType = "";
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public Status getStatus() {
		return status;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileSize(long size) {
		this.fileSize = size;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public File getFile() {
		return file;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

};