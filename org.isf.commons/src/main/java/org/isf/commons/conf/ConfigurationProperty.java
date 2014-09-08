package org.isf.commons.conf;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.isf.commons.bean.Converter;
import org.isf.commons.bean.Type;

@XmlType(name="Property")
@XmlRootElement(name="property")
public class ConfigurationProperty implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1568819833302344695L;

	private String name;
	private Type type = Type.STRING;
	private Object value;
	private Object defaultValue;

	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="type")
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	@XmlTransient
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	@XmlTransient
	public Object getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	@XmlElement(name="value")
	public String getStringValue() {
		try {
			//return StringConverter.toString(getType(), getValue());
			return Converter.convert(String.class, getValue());
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	public void setStringValue(String value) {
		try {
			//StringConverter.fromString(getType(), value);
			setValue(Converter.convert(getType().wrapper()[0], value));
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@XmlElement(name="default-value")
	public String getStringDefaultValue() {
		try {
			//return StringConverter.toString(getType(), getDefaultValue());
			return Converter.convert(String.class, getDefaultValue());
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	public void setStringDefaultValue(String defaultValue) {
		try {
			//setDefaultValue(StringConverter.fromString(getType(), defaultValue));
			setDefaultValue(Converter.convert(getType().wrapper()[0], defaultValue));
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

}
