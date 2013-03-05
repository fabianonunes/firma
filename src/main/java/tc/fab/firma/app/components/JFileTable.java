package tc.fab.firma.app.components;

import java.awt.Dimension;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import tc.fab.firma.FirmaView;
import tc.fab.firma.app.components.JFileTable.FileModel.Status;
import tc.fab.firma.utils.FormatUtils;

public class JFileTable extends JTable {

	private enum Columns {
		STATUS, ICON, FILENAME, SIZE
	}

	private class FileTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		Class<?>[] types = new Class[] { ImageIcon.class, ImageIcon.class, File.class, Long.class };

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return types[columnIndex];
		}

		@Override
		public int getColumnCount() {
			return Columns.values().length;
		}

		@Override
		public String getColumnName(int column) {
			return getColumnNameById(Columns.values()[column]);
		}

		public String getColumnNameById(Columns colId) {
			switch (colId) {
				case STATUS:
					return "";
				case ICON:
					return "";
				case FILENAME:
					return "Arquivo";
				case SIZE:
					return "Tamanho";
				default:
					return colId.toString();
			}
		}

		@Override
		public int getRowCount() {
			return model.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			FileModel row = model.get(rowIndex);
			switch (columnToId(columnIndex)) {
				case FILENAME:
					return row.getFileName();
				case SIZE:
					return row.getSize();
				case ICON:
					return pdfIcon;
				case STATUS:
					switch (row.getStatus()) {
						case DONE:
							return doneIcon;
						case FAILED:
							return failedIcon;
						case LOADING:
							return loadingIcon;
						case IDLE:
							return null;
					}
					return null;
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			FileModel row = model.get(rowIndex);
			switch (columnToId(columnIndex)) {
				case FILENAME:
					row.setFileName((String) aValue);
				case SIZE:
					row.setSize((long) aValue);
				case STATUS:
					row.setStatus((Status) aValue);
				default:
					break;
			}
		}

	}
	private static final long serialVersionUID = 1L;
	private ImageIcon pdfIcon;

	private ImageIcon doneIcon;
	private ImageIcon failedIcon;
	private ImageIcon loadingIcon;

	private Vector<FileModel> model;

	private List<Columns> columns = Arrays.asList(Columns.values());

	private FileTableModel tableModel;

	public JFileTable(Vector<FileModel> model) {

		super();
		
		if (model == null){
			model = new Vector<>();
		}

		this.model = model;

		loadingIcon = new ImageIcon(FirmaView.class.getResource("/icons/spinner-mini-2.gif"));
		loadingIcon.setImageObserver(this); // animated gif

		doneIcon = new ImageIcon(FirmaView.class.getResource("/icons/bullet_green.png"));
		failedIcon = new ImageIcon(FirmaView.class.getResource("/icons/bullet_error.png"));
		pdfIcon = new ImageIcon(FirmaView.class.getResource("/icons/page_white_acrobat.png"));

		setAutoCreateRowSorter(true);

		setShowGrid(false);
		setFocusable(false);
		setRowSelectionAllowed(true);
		
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		getTableHeader().setResizingAllowed(false);
		getTableHeader().setReorderingAllowed(false);

		tableModel = new FileTableModel();
		setModel(tableModel);
		tableModel.addTableModelListener(this);

		TableColumnModel tcModel = getColumnModel();

		tcModel.getColumn(idToColumn(Columns.STATUS)).setMaxWidth(28);
		tcModel.getColumn(idToColumn(Columns.ICON)).setMaxWidth(30);
		tcModel.getColumn(idToColumn(Columns.SIZE)).setMaxWidth(110);

		getTableHeader().setPreferredSize(new Dimension(getTableHeader().getWidth(), 21));
		setRowHeight(24);

	}

	protected Columns columnToId(int id) {
		return columns.get(id);
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {

		switch (columnToId(column)) {
			case ICON:
				return getDefaultRenderer(ImageIcon.class);
			case SIZE:
				return new FileSizeRenderer();
			default:
				return super.getCellRenderer(row, column);
		}

	}

	protected int idToColumn(Columns id) {
		return columns.indexOf(id);
	}

	public void setStatus(int rowIndex, Status status) {
		setValueAt(status, rowIndex, idToColumn(Columns.STATUS));
		tableModel.fireTableRowsUpdated(rowIndex, rowIndex);
	}

	public void updateView() {
		tableModel.fireTableDataChanged();
	}
	
	public Vector<FileModel> getData(){
		return this.model;
	}
	
	public static class FileModel {

		public enum Status {
			IDLE, LOADING, DONE, FAILED
		};

		private Status status;
		private String fileName;
		private long size;
		private File file;

		public FileModel(Status status, File file, long size) {
			super();
			this.status = status;
			this.fileName = file.getName();
			this.size = size;
			this.file = file;
		}

		public String getFileName() {
			return fileName;
		}

		public long getSize() {
			return size;
		}

		public Status getStatus() {
			return status;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public void setSize(long size) {
			this.size = size;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

		public File getFile() {
			return file;
		}

	};

	static class FileSizeRenderer extends SubstanceDefaultTableCellRenderer {
		private static final long serialVersionUID = -8245332154395631871L;

		public FileSizeRenderer() {
			super();
			setHorizontalAlignment(RIGHT);
		}

		@Override
		public void setValue(Object value) {
			value = FormatUtils.format((Number) value);
			super.setValue(value);
		}

	}

}
