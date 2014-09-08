package org.isf.commons.data;

import java.util.EventListener;

public interface TupleChangeListener extends EventListener {

	public void dataChanged(TupleChangeEvent event);
	
}
