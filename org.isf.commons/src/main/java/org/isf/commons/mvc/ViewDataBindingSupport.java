package org.isf.commons.mvc;

import java.util.ArrayList;
import java.util.List;

public class ViewDataBindingSupport {

	private IController<?> controller;
	private Object view;
	private List<DataBindingSupport> bindings = new ArrayList<DataBindingSupport>();
	
	public ViewDataBindingSupport(IController<?> controller, Object view) {
		setController(controller);
		setView(view);
	}
	
	public IController<?> getController() { return controller; }
	protected void setController(IController<?> controller) { this.controller = controller; }
	
	public Object getView() { return view; }
	protected void setView(Object view) { this.view = view; }
	
	public void addBinding(DataBindingSupport binding) {
		bindings.add(binding);
	}
	
	public int size() { return bindings.size(); }
	
	public DataBindingSupport getBinding(int index) {
		return bindings.get(index);
	}
	
}
