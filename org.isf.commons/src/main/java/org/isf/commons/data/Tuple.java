package org.isf.commons.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tuple implements Serializable,Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5620443385768873874L;

	private State state = State.NEW;
	private Object[] arrData;
	private int[] dirtyIndexes;
	private List<TupleChangeListener> listeners = new ArrayList<TupleChangeListener>();
	
	public Tuple(int size) {
		arrData = new Object[size];
		dirtyIndexes = new int[0];
	}
	
	public Tuple() {
		this(0);
	}
	
	public Tuple(Object ... data) {
		this(data.length);
		setData(data);
		dirtyIndexes = new int[0];
	}
	
	public Tuple(int size, Object ... data) {
		this(size);
		setData(data);
		dirtyIndexes = new int[0];
	}
	
	public State getState() { return state; }
	public void setState(State state) { this.state = state; }
	
	public int count() { return arrData.length; }
	
	public Object[] getData(int ... indexes) {
		if (indexes.length == 0) return arrData;
		Object[] ret = new Object[indexes.length];
		for (int i=0;i<indexes.length;i++)
			ret[i] = getData(indexes[i]);
		return ret;
	}
	public Object getData(int index) { return arrData[index]; }
	
	public void setData(Object ... data) {
		for (int i=0;i<data.length;i++)
			setData(i, data[i]);
	}
	public void setData(int index, Object data) {
		if (index >= count()) {
			int nsize = index+1;
			Object[] nldata = new Object[nsize];
			System.arraycopy(arrData, 0, nldata, 0, arrData.length);
			arrData = nldata;
		}
		if (arrData[index] == null && data == null) return;
		else if (arrData[index] != null && arrData[index].equals(data)) return;
		Object oldData = arrData[index];
		arrData[index] = data;
		// mark as dirty
		boolean drfnd = false;
		for (int didx : dirtyIndexes)
			if (didx == index) {
				drfnd = true;
				break;
			}
		if (!drfnd) {
			int nsize = dirtyIndexes.length+1;
			int[] ndi = new int[nsize];
			System.arraycopy(dirtyIndexes, 0, ndi, 0, dirtyIndexes.length);
			ndi[ndi.length-1] = index;
			fireTupleChangeEvent(new TupleChangeEvent(this, oldData, data, index));
			dirtyIndexes = ndi;
		}
	}
	public void addData(Object data) {
		setData(count(), data);
	}
	
	public boolean isDirty() {
		boolean ret = dirtyIndexes.length > 0;
		return ret;
	}
	public int[] getDirtyIndexes() { return dirtyIndexes; }
	
	public void resetDirtyIndexes() {
		dirtyIndexes = new int[0];
	}
	
	public void clear() {
		arrData = new Object[0];
		resetDirtyIndexes();
	}
	
	public List<TupleChangeListener> listTupleChangeListeners() { return listeners; }
	
	public boolean containsTupleChangeListener(TupleChangeListener listener) {
		return listeners.contains(listener);
	}
	
	public void addTupleChangeListener(TupleChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removeTupleChangeListener(TupleChangeListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireTupleChangeEvent(TupleChangeEvent evt) {
//		System.out.println("Tuple("+evt.getIndex()+" changed old: '"+evt.getOldValue()+"' new: '"+evt.getNewValue()+"'");
		for (TupleChangeListener tcl : listTupleChangeListeners())
			tcl.dataChanged(evt);
	}
	
	public Tuple clone() {
		Tuple clone = null;
		try {
			clone = (Tuple)super.clone();
		} catch(CloneNotSupportedException cnse) {
			clone = new Tuple();
		}
		clone.arrData = (Object[])arrData.clone();
		clone.resetDirtyIndexes();
		return clone;
	}
	
}
