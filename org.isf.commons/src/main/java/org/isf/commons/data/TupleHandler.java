package org.isf.commons.data;

public class TupleHandler implements ITupleHandler {

	private IContainer container;
	private int position = -1;
	
	public TupleHandler(IContainer container) {
		setContainer(container);
	}
	
	@Override
	public IHeader getHeader() { return getContainer(); }

	@Override
	public IContainer getContainer() { return container; }
	protected void setContainer(IContainer container) { this.container = container; }
	
	@Override
	public int[] columnIndexes(String[] colnames) {
		int[] indexes = new int[colnames.length];
		for (int i=0;i<colnames.length; i++) { 
			indexes[i] = getHeader().indexOfColumn(colnames[i]);
		}
		
		return indexes;
	}

	@Override
	public int size() { return getContainer().size(); }

	@Override
	public boolean select(int index) {
		if (index >= 0 && index < size()) {
			position = index;
			return true;
		} return false;
	}

	@Override
	public int position() {
		return position;
	}
	
	public boolean first() {
		if (size() == 0) return false;
		else return select(0);
	}
	
	public boolean next() {
		return select(position()+1);
	}
	
	public boolean previous() {
		return select(position()-1);
	}
	
	public boolean last() {
		if (size() == 0) return false;
		else return select(size()-1);
	}

	@Override
	public Tuple get() { return getContainer().getTuple(position()); }

	@Override
	public Object get(int colIndex) {
		return get().getData(colIndex);
	}

	@Override
	public Object get(String colName) { return get(getHeader().indexOfColumn(colName)); }

	@Override
	public void set(Tuple t) { getContainer().setTuple(position(), t); }

	@Override
	public void set(int colIndex, Object data) {
		try { 
			Column col = getHeader().getColumn(colIndex);
			get().setData(colIndex, col.convert(data));
		} catch(Exception e) {
			// TODO throw Exception // indexOutOfBounds and ConvertException!!
		}
	}

	@Override
	public void set(String colName, Object data) { set(getHeader().indexOfColumn(colName), data); }

	@Override
	public void add() {
		add(State.NEW);
	}
	
	@Override
	public void add(State state) {
		Tuple t = getContainer().createTuple();
		t.setState(state);
		add(t);
	}
	
	@Override
	public void add(Tuple t) {
		getContainer().addTuple(t);
		select(size()-1);
	}

}
