package tc.fab.firma.app.components;

import java.awt.Color;
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

import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import tc.fab.firma.FirmaView;
import tc.fab.firma.app.components.JFileTable.FileModel.Status;
import tc.fab.firma.utils.FormatUtils;

public class JFileTable extends JTable {

	private static final long serialVersionUID = 1L;

	enum ColumnId {
		STATUS, ICON, FILENAME, SIZE
	};

	private ImageIcon pdfIcon;
	private ImageIcon doneIcon;
	private ImageIcon failedIcon;
	private ImageIcon loadingIcon;

	private Vector<FileModel> model;
	private List<ColumnId> columns = Arrays.asList(ColumnId.values());

	public JFileTable(Vector<FileModel> model) {

		super();
		
		this.model = model;

		loadingIcon = new ImageIcon(FirmaView.class.getResource("/icons/spinner-mini-2.gif"));
		loadingIcon.setImageObserver(this); // animated gif

		doneIcon = new ImageIcon(FirmaView.class.getResource("/icons/bullet_green.png"));
		failedIcon = new ImageIcon(FirmaView.class.getResource("/icons/bullet_error.png"));
		pdfIcon = new ImageIcon(FirmaView.class.getResource("/icons/page_white_acrobat.png"));

		setAutoCreateRowSorter(true);

		setShowGrid(false);
		setShowHorizontalLines(true);
		setFocusable(false);
		
		setRowSelectionAllowed(false);
		setCellSelectionEnabled(false);
		
		getTableHeader().setResizingAllowed(false);
		getTableHeader().setReorderingAllowed(false);

		FileTableModel tableModel = new FileTableModel();
		setModel(tableModel);
		tableModel.addTableModelListener(this);

		TableColumnModel tcModel = getColumnModel();

		tcModel.getColumn(idToColumn(ColumnId.STATUS)).setMaxWidth(28);
		tcModel.getColumn(idToColumn(ColumnId.ICON)).setMaxWidth(30);
		tcModel.getColumn(idToColumn(ColumnId.SIZE)).setMaxWidth(110);

		getTableHeader().setPreferredSize(new Dimension(getTableHeader().getWidth(), 21));
		setRowHeight(24);

		tcModel.getColumn(1).setCellRenderer(getDefaultRenderer(ImageIcon.class));
		// column.setCellRenderer(new FileSizeRenderer());

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

	protected ColumnId columnToId(int id) {
		return columns.get(id);
	}

	protected int idToColumn(ColumnId id) {
		return columns.indexOf(id);
	}

	@Override
	public AbstractTableModel getModel() {
		return (AbstractTableModel) super.getModel();
	}

	public void setLoading(int rowIndex) {
		getModel().setValueAt(Status.LOADING, rowIndex, idToColumn(ColumnId.STATUS));
		getModel().fireTableRowsUpdated(rowIndex, rowIndex);
	}

	public void setFailed(int rowIndex) {
		getModel().setValueAt(Status.FAILED, rowIndex, idToColumn(ColumnId.STATUS));
		getModel().fireTableRowsUpdated(rowIndex, rowIndex);
	}

	public void setDone(int rowIndex) {
		getModel().setValueAt(Status.DONE, rowIndex, idToColumn(ColumnId.STATUS));
		getModel().fireTableRowsUpdated(rowIndex, rowIndex);
	}

	private class FileTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		Class<?>[] types = new Class[] { ImageIcon.class, ImageIcon.class, File.class, Long.class };

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return types[columnIndex];
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		@Override
		public String getColumnName(int column) {
			return getColumnNameById(ColumnId.values()[column]);
		}

		public String getColumnNameById(ColumnId colId) {
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
		public int getColumnCount() {
			return ColumnId.values().length;
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

	public static class FileModel {

		public enum Status {
			IDLE, LOADING, DONE, FAILED
		};

		private Status status;
		private String fileName;
		private long size;

		public FileModel(Status status, String fileName, long size) {
			super();
			this.status = status;
			this.fileName = fileName;
			this.size = size;
		}

		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public long getSize() {
			return size;
		}

		public void setSize(long size) {
			this.size = size;
		}

	}

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
		
		@Override
		public void setForeground(Color c) {
		}

	}

}
