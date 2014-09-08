package org.isf.commons.bean;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="Type")
@XmlEnum(String.class)
public enum Type {

	@XmlEnumValue("INTEGER") INTEGER(Integer.class, int.class),
	@XmlEnumValue("SHORT") SHORT(Short.class, short.class),
	@XmlEnumValue("BYTE") BYTE(Byte.class, byte.class),
	@XmlEnumValue("LONG") LONG(Long.class, long.class),
	@XmlEnumValue("FLOAT") FLOAT(Float.class, float.class),
	@XmlEnumValue("DOUBLE") DOUBLE(Double.class, double.class),
	@XmlEnumValue("BIGDECIMAL") BIGDECIMAL(BigDecimal.class),
	@XmlEnumValue("BOOLEAN") BOOLEAN(Boolean.class, boolean.class),
	@XmlEnumValue("STRING") STRING(String.class),
	@XmlEnumValue("DATE") DATE(Date.class, java.util.Date.class),
	@XmlEnumValue("TIME") TIME(Time.class),
	@XmlEnumValue("TIMESTAMP") TIMESTAMP(Timestamp.class),
	@XmlEnumValue("BINARY") BINARY(byte[].class),
	@XmlEnumValue("COLLECTION") COLLECTION(Collection.class),
	@XmlEnumValue("MAP") MAP(Map.class),
	@XmlEnumValue("ARRAY") ARRAY(Object[].class),
	@XmlEnumValue("OBJECT") OBJECT(Object.class);
	
	private final Class<?>[] wrapperClasses;
	
	private Type(Class<?> ... wrapperClasses) {
		this.wrapperClasses = wrapperClasses;
	}
	
	public Class<?>[] wrapper() { return wrapperClasses; }
	
	public static Type getTypePerClass(Class<?> c) {
		for (Type t : Type.values())
			for (Class<?> wrp : t.wrapper())
				if (wrp.isAssignableFrom(c))
					return t;
		return OBJECT;
	}
	
	public static Type getArrayTypePerClass(Class<?> c) {
		if (c.isArray()) return getTypePerClass(c.getSuperclass());
		else return getTypePerClass(c);
		
	}
}
