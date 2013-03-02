package tc.fab.pdf.signer.message;

import java.io.Serializable;

public class Envelope extends MessageAdapter<Envelope> implements Serializable {

	private static final long serialVersionUID = 3050579502760433630L;

	public Envelope(byte[] hash, byte[] data, long time) {
		this.hash = hash;
		this.data = data;
		this.time = time;
	}

}
