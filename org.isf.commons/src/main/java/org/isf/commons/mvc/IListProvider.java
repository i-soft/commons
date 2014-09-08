package org.isf.commons.mvc;

import java.util.Collection;
import java.util.Iterator;

public interface IListProvider<K> {
	
	public boolean isDirty();
	public int[] dirtyIndexes();
	
	public K[] select(int ... indexes);
	
	public boolean add(K k);
	public void add(int index, K k);
	
	public boolean addAll(Collection<? extends K> c);
	public boolean addAll(int index, Collection<? extends K> c);
	
//	public void clear();
	
	public boolean contains(K k);
	
	public K get(int index);
	
	public int indexOf(Object o);
	
	public Iterator<K> iterator();
	
	public K remove(int index);
	
	public boolean remove(Object o);
	
	public K set(int index, K k);
	
	public int size();
}
