package tc.fab.security.callback;

import java.io.IOException;

import javax.inject.Inject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import tc.fab.app.AppContext;

public class PINCallback implements CallbackHandler {

	private AppContext context;

	@Inject
	public PINCallback(AppContext context) {
		this.context = context;
	}

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

		// Callback cb = callbacks[0];
		//
		// if (cb instanceof PasswordCallback) {
		//
		// PasswordCallback passCallBack = (PasswordCallback) cb;
		//
		// PINDialog dialog = new PINDialog(context);
		//
		// if (dialog.getStatus()) {
		// passCallBack.setPassword(dialog.getPassword());
		// }
		//
		// }

	}

}
