package org.isf.commons.data;

import java.util.EventListener;

public interface ContainerChangeListener extends EventListener {

	public void tupleCreated(ContainerChangeEvent event);
	public void tupleAdded(ContainerChangeEvent event);
	public void tupleChanged(ContainerChangeEvent event);
	public void tupleRemoved(ContainerChangeEvent event);
	
	// TODO indexCreated
	// TODO containerCleared
	
}
