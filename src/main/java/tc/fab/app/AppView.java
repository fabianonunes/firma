package tc.fab.app;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import tc.fab.firma.app.components.JFileTable;

public interface AppView {
	
	public void initView();
	
	public AbstractTableModel getFileModel();

	public JFileTable getFileTable();
	

}
