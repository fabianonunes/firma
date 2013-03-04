package tc.fab.firma.app.components;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import tc.fab.firma.FirmaView;
import tc.fab.firma.utils.FormatUtils;

public class JFileTable extends JTable {
	
	enum ColumnId { STATUS, ICON, FILENAME, SIZE };

	private static final long serialVersionUID = 1L;
	private ImageIcon loadingIcon;
	private ImageIcon errorIcon;
	private ImageIcon doneIcon;

	public JFileTable(Vector<FileModel> model) {

		super();
		
		
		
		loadingIcon = new ImageIcon(FirmaView.class.getResource("/icons/loader.gif"));
		loadingIcon.setImageObserver(this);

		errorIcon = new ImageIcon(FirmaView.class.getResource("/icons/bullet_error.png"));
		errorIcon.setImageObserver(this);

		doneIcon = new ImageIcon(FirmaView.class.getResource("/icons/bullet_green.png"));
		doneIcon.setImageObserver(this);

		setAutoCreateRowSorter(true);

		setShowGrid(false);
		setShowHorizontalLines(true);
		setRowSelectionAllowed(true);
		setFocusable(false);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		FileTableModel tableModel = new FileTableModel();
		setModel(tableModel);
		tableModel.addTableModelListener(this);
		
		List<ColumnId> columns = Arrays.asList(ColumnId.values());
		TableColumnModel tcModel = getColumnModel();
		
		tcModel.getColumn(columns.indexOf(ColumnId.STATUS)).setPreferredWidth(28);
		tcModel.getColumn(columns.indexOf(ColumnId.ICON)).setPreferredWidth(28);
		tcModel.getColumn(columns.indexOf(ColumnId.SIZE)).setPreferredWidth(110);
		
//	
//		column.setCellRenderer(getDefaultRenderer(ImageIcon.class));
//		column.setCellRenderer(new FileSizeRenderer());

		setAutoCreateColumnsFromModel(false);

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

	}

	@Override
	public DefaultTableModel getModel() {
		return (DefaultTableModel) super.getModel();
	}

	public void setWaiting(int rowIndex) {
		getModel().setValueAt(loadingIcon, rowIndex, 0);
	}

	public void setError(int rowIndex) {
		getModel().setValueAt(errorIcon, rowIndex, 0);
	}

	public void setDone(int rowIndex) {
		getModel().setValueAt(doneIcon, rowIndex, 0);
	}

	public void removeSelecteds() {

		DefaultTableModel model = (DefaultTableModel) getModel();

		while (getSelectedRowCount() > 0) {
			model.removeRow(convertRowIndexToModel(getSelectedRow()));
		}

	}

	public Vector<Object> getDataFromColumn(int column) {

		Vector<Object> columnData = new Vector<Object>();

		DefaultTableModel model = (DefaultTableModel) getModel();

		int rows = model.getRowCount();

		for (int i = 0; i < rows; i++) {
			columnData.add(model.getValueAt(i, column));
		}

		return columnData;

	}

	private static class FileTableModel extends AbstractTableModel {
		
//		public FileTableModel() {
////			addColumn("");
////			addColumn("Arquivo");
////			addColumn("Tamanho");
//		}

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
			return model;
		}

		@Override
		public int getColumnCount() {
			return ColumnId.values().length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public static class FileModel {
		
		enum Status {IDLE, STARTING, DONE, FAILED };
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

}
