package tc.fab.pdf.signer.application;

import java.awt.Component;

import org.jdesktop.application.Task;
import org.jdesktop.application.Task.BlockingScope;
import org.jdesktop.application.Task.InputBlocker;

public class ComponentsInputBlocker extends InputBlocker {

	private Component[] components;

	public ComponentsInputBlocker(Task<?, ?> task, Component ... components) {
		super(task, BlockingScope.APPLICATION, null);
		this.components = components;
	}
	
	public static final InputBlocker builder(Task<?, ?> task, Component ... components) {
		return new ComponentsInputBlocker(task, components);
	}

	@Override
	protected void block() {
		for (Component component : components) {
			component.setEnabled(false);
		}
	}

	@Override
	protected void unblock() {
		for (Component component : components) {
			component.setEnabled(true);
		}
	}

}
 