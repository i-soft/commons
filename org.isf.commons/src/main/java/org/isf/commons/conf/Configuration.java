package org.isf.commons.conf;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="Configuration")
@XmlRootElement(name="configuration")
public class Configuration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3617297108271060974L;

	private String name;
	private Map<String, Configuration> subConfigurations;
	private Map<String, ConfigurationProperty> properties;
	
	public Configuration() {
		super();
		subConfigurations = Collections.synchronizedMap(new HashMap<String, Configuration>());
		properties = Collections.synchronizedMap(new HashMap<String, ConfigurationProperty>());
	}

	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name="sub-config")
	public Collection<Configuration> getSubConfigurations() {
		return subConfigurations.values();
	}
	public void setSubConfigurations(Collection<Configuration> list) {
		for (Configuration sub : list) 
			putSubConfiguration(sub);
	}
	
	public void putSubConfiguration(Configuration sub) {
		this.subConfigurations.put(sub.getName(), sub);
	}
	public Configuration getSubConfiguration(String name) {
		return subConfigurations.get(name);
	}
	public void removeSubConfiguration(String name) {
		subConfigurations.remove(name);
	}
	
	@XmlElement(name="property")
	public Collection<ConfigurationProperty> getProperties() { 
		return properties.values();
	}
	public void setProperties(Collection<ConfigurationProperty> properties) {
		for (ConfigurationProperty prop : properties)
			putProperty(prop);
	}
	
	public void putProperty(ConfigurationProperty property) {
		properties.put(property.getName(), property);
	}
	public ConfigurationProperty getProperty(String name) {
		return properties.get(name);
	}
	public void removeProperty(String name) {
		properties.remove(name);
	}
	
	
	
}
