package tc.fab.firma.app.components;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import tc.fab.firma.FirmaView;
import tc.fab.firma.utils.FormatUtils;

public class JFileTable extends JTable {

	private static final long serialVersionUID = 1L;
	private ImageIcon loadingIcon;
	private ImageIcon doneIcon;

	public JFileTable() {

		super();

		loadingIcon = new ImageIcon(FirmaView.class.getResource("/icons/loading.gif"));
		loadingIcon.setImageObserver(this);

		doneIcon = new ImageIcon(FirmaView.class.getResource("/icons/bullet_error.png"));
		doneIcon.setImageObserver(this);

		setAutoCreateRowSorter(true);

		setShowGrid(false);
		setShowHorizontalLines(true);
		setRowSelectionAllowed(true);
		setFocusable(false);

		FileTableModel tableModel = new FileTableModel();
		setModel(tableModel);

		TableColumn column = null;
		column = getColumnModel().getColumn(0);
		column.setMinWidth(28);
		column.setMaxWidth(28);
		column.setCellRenderer(getDefaultRenderer(ImageIcon.class));

		column = getColumnModel().getColumn(1);
		column.setPreferredWidth(300);

		column = getColumnModel().getColumn(2);
		column.setMaxWidth(110);
		column.setMinWidth(110);

		column.setCellRenderer(new FileSizeRenderer());

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

	private class FileTableModel extends DefaultTableModel {

		public FileTableModel() {
			addColumn("");
			addColumn("Arquivo");
			addColumn("Tamanho");
		}

		private static final long serialVersionUID = 1L;

		boolean[] canEdit = new boolean[] { false, false, false };

		Class<?>[] types = new Class[] { ImageIcon.class, Object.class, Long.class };

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return types[columnIndex];
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return canEdit[columnIndex];
		}

	}

}
