package org.isf.commons.data;

import java.io.Serializable;

public class TupleChangeEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5633008154711255365L;
	
	private Tuple tuple;
	private Object oldValue;
	private Object newValue;
	private int index;
	
	public TupleChangeEvent() { super(); }
	
	public TupleChangeEvent(Tuple tuple, Object oldValue, Object newValue, int index) {
		this();
		setTuple(tuple);
		setOldValue(oldValue);
		setNewValue(newValue);
		setIndex(index);
	}
	
	public Tuple getTuple() {
		return tuple;
	}
	public void setTuple(Tuple tuple) {
		this.tuple = tuple;
	}
	public Object getOldValue() {
		return oldValue;
	}
	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}
	public Object getNewValue() {
		return newValue;
	}
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
}
