package org.isf.commons.mvc;

public interface IController<T> {

	public T getModel();
	public void setModel(T t);
	
	public void load(Object ... objs) throws Exception;
	public void reset() throws Exception;
	public void refresh() throws Exception;
	public void save(Object ... objs) throws Exception;
	
	public boolean isDirty();
	
	public void adaptView(Object view);
	public void removeView(Object view);
	
	public void resetViews(Object ... views);
	public void refreshViews(Object ... views);
	
	public void resetAllViews();
	public void refreshAllViews();
	
}
