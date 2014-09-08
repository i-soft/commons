package org.isf.commons.data;

import java.io.Serializable;

public class IndexKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6175346111253421707L;

	private Object[] keys = new Object[0];
	
	public IndexKey(Object ... keys) {
		this.keys = keys;
	}
	
	public int size() { return keys.length; }
	
	public Object get(int index) { return keys[index]; }
	
	public boolean equals(Object o) {
		if (o == null && !(o instanceof IndexKey)) return false;
		IndexKey ik = (IndexKey)o;
		if (size() != ik.size()) return false;
		for (int i=0;i<size();i++) {
			Object o1 = get(i);
			Object o2 = ik.get(i);
			if ((o1 == null && o2 != null) || (o1 != null && o2 == null) || (o1 != null && !o1.equals(o2))) return false;
		}			
		return true;
	}
	
	public int hashCode() {
		int hash = 0;
		for (int i=0;i<size();i++)
			hash += (get(i) == null) ? 0 : get(i).hashCode();
		return hash;
	}
	
}
