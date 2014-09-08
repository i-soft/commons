package org.isf.commons;

import java.util.HashMap;
import java.util.Map;

public class AttributeSupport implements IAttributeSupport {

	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	@Override
	public Map<String, Object> getAttributes() { return attributes; }

	@Override
	public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }

	@Override
	public Object getAttribute(String key) {
		return getAttributes().get(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		getAttributes().put(key, value);
	}
	
	@Override
	public boolean hasAttribute(String key) {
		return getAttributes().containsKey(key);
	}

}
