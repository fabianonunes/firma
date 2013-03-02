package tc.fab.pdf.signer.message;

import java.io.Serializable;
import java.security.cert.Certificate;

public class SignatureClaim extends MessageAdapter<SignatureClaim> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Certificate[] chain;

	public SignatureClaim(byte[] hash, byte[] signature, Certificate[] chain, long time) {
		this.hash = hash;
		this.data = signature;
		this.chain = chain;
		this.time = time;
	}

	public Certificate[] getChain() {
		return chain;
	}

	public void setChain(Certificate[] chain) {
		this.chain = chain;
	}

}
