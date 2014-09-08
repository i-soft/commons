package org.isf.commons.data;

import java.io.Serializable;

public class ContainerChangeEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6549213117293726438L;

	private IContainer container;
	private Column column;
	private Tuple tuple;
	private Object oldTupleData;
	private Object newTupleData;
	private int index;
	
	public ContainerChangeEvent() { super(); }
	
	public ContainerChangeEvent(IContainer container, Tuple tuple, int index) {
		this();
		setContainer(container);
		setTuple(tuple);
		setIndex(index);
	}
	
	public ContainerChangeEvent(IContainer container, Column column, Tuple tuple, Object oldTupleData, Object newTupleData, int index) {
		this(container,tuple,index);
		setColumn(column);		
		setOldTupleData(oldTupleData);
		setNewTupleData(newTupleData);
		
	}	
	
	public IContainer getContainer() {
		return container;
	}
	public void setContainer(IContainer container) {
		this.container = container;
	}
	public Column getColumn() {
		return column;
	}
	public void setColumn(Column column) {
		this.column = column;
	}
	public Tuple getTuple() {
		return tuple;
	}
	public void setTuple(Tuple tuple) {
		this.tuple = tuple;
	}
	public Object getOldTupleData() {
		return oldTupleData;
	}
	public void setOldTupleData(Object oldTupleData) {
		this.oldTupleData = oldTupleData;
	}
	public Object getNewTupleData() {
		return newTupleData;
	}
	public void setNewTupleData(Object newTupleData) {
		this.newTupleData = newTupleData;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
}
