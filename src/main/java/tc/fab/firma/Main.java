package tc.fab.firma;

import iaik.pkcs.pkcs11.TokenException;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.callback.CallbackHandler;
import javax.swing.UIManager;

import org.apache.commons.lang.SystemUtils;
import org.jdesktop.application.Action;
import org.jdesktop.application.Resource;
import org.jdesktop.application.Task;
import org.jdesktop.application.View;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.error.ErrorLevel;
import org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel;

import tc.fab.app.AppContext;
import tc.fab.app.AppController;
import tc.fab.app.AppDocument;
import tc.fab.app.AppView;
import tc.fab.mechanisms.Pkcs11Config;
import tc.fab.security.callback.PINCallback;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main extends Firma {

	@Resource(key = "firma.pkcs11.libs.unix")
	private String[] unixLibs = new String[20];

	@Resource(key = "firma.pkcs11.libs.win")
	private String[] winLibs = new String[20];

	private static final Logger LOGGER = Logger.getLogger(Firma.class.getName());

	private static final String ACTION_LOAD_PKCS11_WRAPPER = "firma.main.load_pkcs11_wrapper";
	private AppContext context;
	private AppController controller;
	private AppView view;
	private AppDocument document;

	private Injector injector;
	private Pkcs11Config pkcs11Config;

	public static void main(String args[]) {
		launch(Main.class, args);
	}

	@Override
	protected void startup() {

		context = new FirmaContext(getContext());
		context.getResourceMap().injectFields(this);

		List<String> libraries = getExistentsLibs();

		injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {

				// app
				bind(AppContext.class).toInstance(context);
				bind(AppView.class).to(FirmaView.class);
				bind(AppController.class).to(FirmaController.class);
				bind(AppDocument.class).to(FirmaDocument.class);
				
				// security
				bind(CallbackHandler.class).to(PINCallback.class);

			}
		});

		controller = injector.getInstance(AppController.class);

		addExitListener(new ExitListener() {
			@Override
			public boolean canExit(EventObject e) {
				return controller.saveBeforeExit();
			}

			@Override
			public void willExit(EventObject e) {
			}
		});

		document = injector.getInstance(AppDocument.class);
		document.loadOptions();

		pkcs11Config = new Pkcs11Config(libraries, injector.getInstance(CallbackHandler.class));

		view = injector.getInstance(AppView.class);
		initLookAndFeel(SubstanceCremeLookAndFeel.class.toString());

		EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
		queue.push(new EventQueueProxy());

		view.initView();

		show((View) view);

		context.fireAction(this, ACTION_LOAD_PKCS11_WRAPPER);

	}

	private List<String> getExistentsLibs() {
		String[] libs = SystemUtils.IS_OS_WINDOWS ? winLibs : unixLibs;
		List<String> exitentsLibs = new ArrayList<>();
		for (String lib : libs) {
			if (lib != null && new File(lib).exists()) {
				exitentsLibs.add(lib);
			}
		}
		return exitentsLibs;
	}

	@Action(name = ACTION_LOAD_PKCS11_WRAPPER)
	public Task<Void, Void> loadPkcs11Wrapper() {
		return new LoadPkcs11Task();
	}

	class LoadPkcs11Task extends Task<Void, Void> {

		public LoadPkcs11Task() {
			super(context.getAppContext().getApplication());
		}

		@Override
		protected Void doInBackground() throws Exception {
			try {
				pkcs11Config.loadPkcs11Wrapper();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}

	}

	private void initLookAndFeel(String lafClass) {
		try {
			String lf = "org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel";
			UIManager.setLookAndFeel(lf);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Failed to set look&feel to " + lafClass + "!", e);
		}
	}

	@Override
	protected void shutdown() {
		super.shutdown();
		document.storeOptions();
		try {
			pkcs11Config.finalizeModules();
		} catch (TokenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void initialize(String[] args) {
		super.initialize(args);
	}

	@Override
	protected void ready() {
		super.ready();
	}

	class EventQueueProxy extends EventQueue {

		protected void dispatchEvent(AWTEvent newEvent) {
			try {
				super.dispatchEvent(newEvent);
			} catch (Throwable t) {
				ErrorInfo info = new ErrorInfo("Erro",
					"Envie o texto abaixo para https://github.com/fabianonunes/firma/issues", null,
					null, t, ErrorLevel.SEVERE, null);
				JXErrorPane.showDialog(getMainFrame(), info);
			}
		}
	}

}
