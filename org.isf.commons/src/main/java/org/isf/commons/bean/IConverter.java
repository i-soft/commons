package org.isf.commons.bean;

import java.io.Serializable;

public interface IConverter extends Serializable { 

	public <T> T convert(Class<T> clazz, Object data);
	
}
