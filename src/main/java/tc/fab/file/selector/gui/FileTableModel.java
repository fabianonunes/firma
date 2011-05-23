package tc.fab.file.selector.gui;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

public class FileTableModel extends DefaultTableModel {
	
	public FileTableModel() {
		addColumn("");
		addColumn("Arquivo");
		addColumn("Caminho");
		addColumn("Tamanho");
		
	}

	private static final long serialVersionUID = 1L;
	
	boolean[] canEdit = new boolean[] { false, false, false, false };
	
	Class<?>[] types = new Class [] {
        ImageIcon.class, Object.class, String.class, Long.class
    };
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return types[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return canEdit[columnIndex];
	}

}
