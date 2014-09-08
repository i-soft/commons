package org.isf.commons;

import java.util.Properties;

public interface IPropertySupport {

	public Properties getProperties();
	public void setProperties(Properties props);
	
	public String getProperty(String name);
	public void setProperty(String name, String value);
	
}
