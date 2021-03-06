package tc.fab.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.swing.JFrame;

public class PINDialogCallback implements CallbackHandler {

	JFrame frame;

	public PINDialogCallback(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {

		Callback cb = callbacks[0];

		if (cb instanceof PasswordCallback) {

			final PasswordCallback passCallBack = (PasswordCallback) cb;

			PINDialog dialog = new PINDialog(frame, true);
			
			dialog.setLocationRelativeTo(frame);
			
			dialog.setVisible(true);
			
			if(dialog.getStatus()){

				passCallBack.setPassword(dialog.getPassword());
			
			} else {
				throw new UnsupportedCallbackException(cb);
			}

		}

	}

}
