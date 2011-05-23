package tc.fab.security;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Vector;

import javax.security.auth.login.LoginException;
import javax.security.auth.x500.X500Principal;

public abstract class AbstractKeyStoreAdapter implements KeyStoreAdapter {

	protected KeyStore keystore;
	protected Vector<String> aliases = new Vector<String>();
	protected SmartCardReader reader;

	public Vector<String> getAliases() {
		return aliases;
	}

	protected void setAliases(Enumeration<String> aliases) {

		getAliases().clear();

		while (aliases.hasMoreElements()) {
			this.aliases.add(aliases.nextElement());
		}

	}

	public String getAlias(Integer index) {
		return getAliases().get(index);
	}

	public PrivateKey getPrivateKey(String alias)
			throws UnrecoverableKeyException, KeyStoreException,
			NoSuchAlgorithmException {

		return (PrivateKey) keystore.getKey(alias, null);

	}

	public PublicKey getPublicKey(String alias) throws KeyStoreException {
		return keystore.getCertificate(alias).getPublicKey();
	}

	public X509Certificate getCertificate(Integer index)
			throws KeyStoreException {
		return (X509Certificate) getKeystore().getCertificate(getAlias(index));
	}

	public void logout() throws LoginException {
		// TODO: estudar método logout da classe Token
		reader.getProvider().logout();
		Security.removeProvider(reader.getProvider().getName());
	}

	public KeyStore getKeystore() {
		return keystore;
	}

	@SuppressWarnings("unused")
	public String getFriendlyName(int index) throws KeyStoreException {
		X509Certificate cert = (X509Certificate) getKeystore().getCertificate(
				getAlias(index));
		String name = cert.getSubjectX500Principal().getName();
		PublicKey pk = cert.getPublicKey();
		String name2 = cert.getSubjectX500Principal().getName(
				X500Principal.CANONICAL);
		String name3 = cert.getSubjectX500Principal().getName(
				X500Principal.RFC1779);
		String name4 = cert.getSubjectX500Principal().getName(
				X500Principal.RFC2253);

		Certificate[] certs = getKeystore().getCertificateChain(getAlias(0));

		return cert.getNotAfter().toString();
	}

}
