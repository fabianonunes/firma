package tc.fab.app;

import tc.fab.firma.app.components.JFileTable;

public interface AppView {
	
	public void initView();

	JFileTable getFileTable();
	
}
