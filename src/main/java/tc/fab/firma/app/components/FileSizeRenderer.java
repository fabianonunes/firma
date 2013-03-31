package tc.fab.firma.app.components;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import tc.fab.firma.utils.FormatUtils;

public class FileSizeRenderer extends SubstanceDefaultTableCellRenderer {
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