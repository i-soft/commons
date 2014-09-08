package org.isf.commons;

import java.util.Properties;

public class PropertySupport implements IPropertySupport {

	private Properties props;
	
	public PropertySupport() { props = new Properties(); }
	public PropertySupport(Properties props) {
		this();
		setProperties(props);
	}
	
	@Override
	public Properties getProperties() { return props; }
	@Override
	public void setProperties(Properties props) { this.props = props; }
	
	@Override
	public String getProperty(String name) { return getProperties().getProperty(name); }

	@Override
	public void setProperty(String name, String value) { getProperties().setProperty(name, value); }

}
