package org.isf.commons.data;

public interface ITupleHandler {

	public IHeader getHeader();
	public IContainer getContainer();
	
	public int[] columnIndexes(String[] colnames); 
	
	public int size();
	public boolean select(int index);
	public int position();
	
	public boolean first();
	public boolean next();
	public boolean previous();
	public boolean last();
	
	public Tuple get();
	public Object get(int colIndex);
	public Object get(String colName);
	public void set(Tuple t);
	public void set(int colIndex, Object data);
	public void set(String colName, Object data);
	public void add();
	public void add(State state);
	public void add(Tuple t);
		
}
