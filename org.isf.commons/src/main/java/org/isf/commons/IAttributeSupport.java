package org.isf.commons;

import java.util.Map;

public interface IAttributeSupport {

	public Map<String, Object> getAttributes();
	public void setAttributes(Map<String, Object> attributes);
	
	public Object getAttribute(String key);
	public void setAttribute(String key, Object value);
	
	public boolean hasAttribute(String key);
	
}
