package tc.fab.mechanisms;

import iaik.pkcs.pkcs11.Module;
import iaik.pkcs.pkcs11.Session;
import iaik.pkcs.pkcs11.Slot;
import iaik.pkcs.pkcs11.Token;
import iaik.pkcs.pkcs11.TokenException;
import iaik.pkcs.pkcs11.objects.Attribute;
import iaik.pkcs.pkcs11.objects.Certificate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.inject.Singleton;

import org.apache.commons.io.IOUtils;

@Singleton
public class TokenInfo {

	private File wrapperFile;

	public TokenInfo() throws IOException {

		wrapperFile = File.createTempFile("lib", "wrapper");

		InputStream wrapperLib = TokenInfo.class
			.getResourceAsStream("lib/unix/64/libpkcs11wrapper.so");
		wrapperFile.deleteOnExit();

		OutputStream fout = new FileOutputStream(wrapperFile);
		IOUtils.copy(wrapperLib, fout);

	}

	public synchronized ArrayList<String> getAliases(String libModule) throws IOException,
		TokenException {

		ArrayList<String> aliases = new ArrayList<>();

		Module module = Module.getInstance(libModule, wrapperFile.getAbsolutePath());
		module.initialize(null);

		Slot[] slotsWithToken = module.getSlotList(Module.SlotRequirement.TOKEN_PRESENT);

		for (Slot slot : slotsWithToken) {

			Session session = slot.getToken().openSession(Token.SessionType.SERIAL_SESSION,
				Token.SessionReadWriteBehavior.RO_SESSION, null, null);

			Certificate searchTemplate = new Certificate();

			session.findObjectsInit(searchTemplate);

			for (Object object : session.findObjects(10)) {
				Certificate certificate = (Certificate) object;
				String label = certificate.getLabel().toString();
				if (label.equals("<NULL_PTR>")) {
					label = "0x" + certificate.getAttributeTable().get(Attribute.ID);
				}
				aliases.add(label);

			}

			session.findObjectsFinal();

		}

		module.finalize(null);

		return aliases;

	}

}
