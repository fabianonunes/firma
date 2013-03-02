package tc.fab.pdf.signer.message;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public abstract class MessageAdapter<I> implements Message {

	static Gson gson;
	static {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(byte[].class, new ByteArrayToHexTypeAdapter());
		gsonBuilder.registerTypeAdapter(Certificate.class, new CertificateToHexTypeAdapter());
		gson = gsonBuilder.setPrettyPrinting().create();
	}

	byte[] hash;
	long time;
	byte[] data;

	public byte[] getHash() {
		return hash;
	}

	public void setHash(byte[] hash) {
		this.hash = hash;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public byte[] getMessage() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public String toJson() {
		return gson.toJson(this);
	}

	public static Message fromJson(String json, Class<? extends Message> clazz) {
		return gson.fromJson(json, clazz);
	}

	private static class ByteArrayToHexTypeAdapter implements JsonSerializer<byte[]>,
		JsonDeserializer<byte[]> {
		public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
			try {
				return Hex.decodeHex(json.getAsString().toCharArray());
			} catch (DecoderException e) {
				throw new JsonParseException(e);
			}
		}

		public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(new String(Hex.encodeHex(src)));
		}
	}

	private static class CertificateToHexTypeAdapter implements JsonSerializer<Certificate>,
		JsonDeserializer<Certificate> {

		public Certificate deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
			try {

				byte[] data = Hex.decodeHex(json.getAsString().toCharArray());

				Certificate cert = CertificateFactory.getInstance("X.509").generateCertificate(
					new ByteArrayInputStream(data));

				return cert;

			} catch (Exception e) {
				throw new JsonParseException(e);
			}
		}

		@Override
		public JsonElement serialize(Certificate src, Type typeOfSrc,
			JsonSerializationContext context) {
			try {
				return new JsonPrimitive(new String(Hex.encodeHex(src.getEncoded())));
			} catch (CertificateEncodingException e) {
				e.printStackTrace();
			}
			return null;
		}

	}

}
