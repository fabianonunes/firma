package tc.fab.file.selector.gui;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class JFileTable extends JTable {

	private static final long serialVersionUID = 1L;

	public JFileTable() {
		super();

		setAutoCreateRowSorter(true);

		TableColumn column = null;

		FileTableModel model = new FileTableModel();

		setModel(model);

		column = getColumnModel().getColumn(0);
		column.setMinWidth(28);
		column.setMaxWidth(28);
		column.setCellRenderer(getDefaultRenderer(ImageIcon.class));

		column = getColumnModel().getColumn(1);
		column.setPreferredWidth(300);
		// column.setCellRenderer(new FilenameCellRenderer());

		column = getColumnModel().getColumn(2);
		column.setPreferredWidth(250);
		column.setMinWidth(250);

		column = getColumnModel().getColumn(3);
		column.setMaxWidth(110);
		column.setMinWidth(110);

		column.setCellRenderer(new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public void setValue(Object value) {
				setHorizontalAlignment(SwingConstants.RIGHT);
				value = FileLengthFormat.format((Number) value);
				super.setValue(value);
			}

		});

		setAutoCreateColumnsFromModel(false);

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

}
