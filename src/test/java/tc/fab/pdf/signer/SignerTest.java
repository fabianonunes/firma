package tc.fab.pdf.signer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import tc.fab.mechanisms.Mechanism;
import tc.fab.mechanisms.Pkcs11Config;
import tc.fab.mechanisms.callback.SimplePasswordCallback;
import tc.fab.pdf.signer.Signer.Message;
import tc.fab.pdf.signer.Signer.SignatureClaim;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.itextpdf.text.pdf.PdfReader;

public class SignerTest extends TestCase {

	String eTPassword;
	private Pkcs11Config config;
	private Mechanism m;

	public void setUp() throws IOException {
		InputStream is = getClass().getResourceAsStream("/eTPassword.text");
		eTPassword = IOUtils.toString(is);
	}

	public void test() throws Exception {

		File file_to_sign = new File("/tmp/ram/sign.pdf");
		File file_to_save = new File("/tmp/ram/save_blank.pdf");

		String pkcs11Module = "/usr/lib/libaetpkss.so";
		pkcs11Module = "/usr/lib/libeTPkcs11.so";
		List<String> lib = new ArrayList<>();
		lib.add(pkcs11Module);

		config = new Pkcs11Config(lib, new SimplePasswordCallback(eTPassword.toCharArray()));

		config.loadPkcs11Wrapper();

		ArrayList<String> aliases = config.aliases(pkcs11Module);

		m = config.getMechanism(pkcs11Module, aliases.get(0));
		m.login();

		Signer signer = new Signer(m);

		SignatureAppearance sapp = new SignatureAppearance(signer);

		Message message = sapp.signBlank(file_to_sign, file_to_save, m.getCertificateChain());

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(byte[].class, new ByteArrayToBase64TypeAdapter());
		Gson gson = gsonBuilder.setPrettyPrinting().create();

		System.out.println(gson.toJson(message));
		
		message = gson.fromJson(gson.toJson(message), Message.class);

		byte[] signed = m.sign(message.getDataToSign());

		SignatureClaim claim = signer.new SignatureClaim(message.getHash(), signed,
			m.getCertificateChain(), message.getTime());
		
		String messageClaim = gson.toJson(claim);
		System.out.println(messageClaim);
		
		
		claim = gson.fromJson(messageClaim, SignatureClaim.class);

		signer.signDeferred(new PdfReader("/tmp/ram/save_blank.pdf"), new File(
			"/tmp/ram/signed.pdf"), claim);

	}

	@Override
	protected void tearDown() throws Exception {
		System.out.println("tearing down...");
		m.logout();
		config.finalizeModules();
	}

	private static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>,
		JsonDeserializer<byte[]> {
		public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
			try {
				return Hex.decodeHex(json.getAsString().toCharArray());
			} catch (DecoderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(new String(Hex.encodeHex(src)));
		}
	}

}
