package org.isf.commons.dto;

import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.util.Date;

public class DTOChangeEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2143423759205956625L;

	private Date date;
	private String property;
	private Object oldValue;
	private Object newValue;
	
	public DTOChangeEvent(String property, Object oldValue, Object newValue) { 
		setDate(new Date());
		setProperty(property);
		setOldValue(oldValue);
		setNewValue(newValue);
	}
	
	public DTOChangeEvent(PropertyChangeEvent evt) {
		this(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
	}

	public Date getDate() {
		return date;
	}

	private void setDate(Date date) {
		this.date = date;
	}

	public String getProperty() {
		return property;
	}

	private void setProperty(String property) {
		this.property = property;
	}

	public Object getOldValue() {
		return oldValue;
	}

	private void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

	private void setNewValue(Object newValue) {
		this.newValue = newValue;
	}
	
}
