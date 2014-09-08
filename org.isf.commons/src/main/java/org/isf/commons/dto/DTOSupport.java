package org.isf.commons.dto;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

public interface DTOSupport extends Serializable, PropertyChangeListener {

	public DTOChangeEvent[] listChanges();
	
}
