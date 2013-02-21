package tc.fab.firma.app.components;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import tc.fab.firma.utils.FormatUtils;

public class JFileTable extends JTable {

    private static final long serialVersionUID = 1L;

    public JFileTable() {

	super();

	setAutoCreateRowSorter(true);

	setModel(new FileTableModel());

	TableColumn column = null;
	column = getColumnModel().getColumn(0);
	column.setMinWidth(28);
	column.setMaxWidth(28);
	column.setCellRenderer(getDefaultRenderer(ImageIcon.class));

	column = getColumnModel().getColumn(1);
	column.setPreferredWidth(300);

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
		value = FormatUtils.format((Number) value);
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

    private class FileTableModel extends DefaultTableModel {

	public FileTableModel() {
	    addColumn("");
	    addColumn("Arquivo");
	    addColumn("Caminho");
	    addColumn("Tamanho");
	}

	private static final long serialVersionUID = 1L;

	boolean[] canEdit = new boolean[] { false, false, false, false };

	Class<?>[] types = new Class[] { ImageIcon.class, Object.class,
		String.class, Long.class };

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
