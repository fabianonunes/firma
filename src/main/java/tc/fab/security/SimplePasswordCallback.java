package tc.fab.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class SimplePasswordCallback implements CallbackHandler {

	private char[] password;

	public SimplePasswordCallback(char[] password) {
		this.password = password;
	}

	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {

		Callback cb = callbacks[0];

		if (cb instanceof PasswordCallback) {

			final PasswordCallback passCallBack = (PasswordCallback) cb;

			passCallBack.setPassword(this.password);

		}

	}

}
