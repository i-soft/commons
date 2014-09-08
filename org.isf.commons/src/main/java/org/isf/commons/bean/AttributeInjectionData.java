package org.isf.commons.bean;

public class AttributeInjectionData {

	private Attribute attribute;
	private Object bean;
	private Object data;
	private Object[] params;
	
	public AttributeInjectionData(Attribute attribute, Object bean, Object data, Object ... params) {
		this.attribute = attribute;
		this.bean = bean;
		this.data = data;
		this.params = params;
	}
	
	public Attribute getAttribute() {
		return attribute;
	}
	public Object getBean() {
		return bean;
	}
	public Object getData() {
		return data;
	}
	public Object[] getParams() {
		return params;
	}
	
}
