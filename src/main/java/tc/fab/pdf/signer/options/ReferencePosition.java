package tc.fab.pdf.signer.options;

import tc.fab.firma.Firma;

public enum ReferencePosition {

	ABOVE("firma.msg.above_reference"), BELOW("firma.msg.below_reference");

	private String text;

	private ReferencePosition(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		// TODO: essa referência estática direta não está legal
		return Firma.getInstance().getContext().getResourceMap().getString(text);
	}

}