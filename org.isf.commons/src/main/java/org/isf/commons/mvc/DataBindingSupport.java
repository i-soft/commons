package org.isf.commons.mvc;

public class DataBindingSupport {

	private DataBindingContext context;
	
	public DataBindingSupport(DataBindingContext context) {
		setContext(context);
	}
	
	public DataBindingContext getContext() {
		return context;
	}
	protected void setContext(DataBindingContext context) {
		this.context = context;
	}
	
}
