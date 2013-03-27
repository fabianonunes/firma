package tc.fab.pdf.signer.message;

public interface Message {
	
	byte[] getHash();
	byte[] getMessage();
	long getTime();
	String toJson();
	
}
