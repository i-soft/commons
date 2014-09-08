package org.isf.commons.bean;

public class DefaultConverter implements IConverter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3775213137470231737L;

	@Override
	public <T> T convert(Class<T> clazz, Object data) {
		return Converter.convert(clazz, data);
	}

}
