package tc.fab.firma.utils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class PropertyObservable {

	private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}
	
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue){
		changeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

}
