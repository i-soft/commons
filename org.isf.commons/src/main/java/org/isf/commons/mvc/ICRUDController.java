package org.isf.commons.mvc;

public interface ICRUDController<T> extends IController<T> {

	public T create() throws Exception;
	public T read(Object ... keys) throws Exception; // if keys.length == 0 return current
	public T update(T t) throws Exception;
	public void delete(T t) throws Exception;
	
}
