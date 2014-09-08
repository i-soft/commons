package org.isf.commons.mvc;

import org.isf.commons.bean.Bean;

public class DataBindingContext {

	private IController<?> controller;
	private Object view;
	private Object target;
	private Class<?> targetClass;
	private String targetAttribute;
	private String modelAttribute;
	
	public IController<?> getController() {
		return controller;
	}
	public void setController(IController<?> controller) {
		this.controller = controller;
	}
	public Object getView() {
		return view;
	}
	public void setView(Object view) {
		this.view = view;
	}
	public Object getTarget() {
		return target;
	}
	public void setTarget(Object target) {
		this.target = target;
	}
	public Class<?> getTargetClass() {
		return targetClass;
	}
	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}
	public String getTargetAttribute() {
		return targetAttribute;
	}
	public void setTargetAttribute(String targetAttribute) {
		this.targetAttribute = targetAttribute;
	}
	public String getModelAttribute() {
		return modelAttribute;
	}
	public void setModelAttribute(String modelAttribute) {
		this.modelAttribute = modelAttribute;
	}
	
	public Object getModel() { return getController().getModel(); }
	
	public Class<?> getModelClass() { return Bean.getGenericType(getController().getClass()); }
	
	
	
}
