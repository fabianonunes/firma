package tc.fab.mechanisms.callback;

import java.io.IOException;

import javax.inject.Inject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import tc.fab.app.AppContext;
import tc.fab.firma.app.dialogs.PINDialog;

public class PINCallback implements CallbackHandler {

	private AppContext context;

	@Inject
	public PINCallback(AppContext context) {
		this.context = context;
	}

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

		for (Callback callback : callbacks) {

			if (callback instanceof PasswordCallback) {

				PINDialog dialog = new PINDialog(context);

				if (dialog.getStatus()) {
					((PasswordCallback) callback).setPassword(dialog.getPassword());
				} else {
					throw new UserCancelledException("user.cancelled");
				}

			}

		}

	}
	
	public class UserCancelledException extends IOException {
		
		private static final long serialVersionUID = 3805140820020791737L;

		public UserCancelledException(String message) {
			super(message);
		}
		
	}

}
