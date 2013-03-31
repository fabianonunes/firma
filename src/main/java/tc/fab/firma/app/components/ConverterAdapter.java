package tc.fab.firma.app.components;

import org.jdesktop.beansbinding.Converter;

public abstract class ConverterAdapter<S, T> extends Converter<S, T> {
	
	@Override
	public S convertReverse(T value) {
		return null;
	}
	
	@Override
	public T convertForward(S value) {
		return null;
	}

}
