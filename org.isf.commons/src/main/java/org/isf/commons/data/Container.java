package org.isf.commons.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.isf.commons.Util;

import com.google.common.collect.ArrayListMultimap;

public class Container extends Header implements IContainer, TupleChangeListener {

	private String name;
	private List<Tuple> tuples;
	private Map<String, ArrayListMultimap<IndexKey, Tuple>> indexMap;
	private Map<String, IndexDefinition> indexDefinitionMap;
	private List<ContainerChangeListener> listeners = new ArrayList<ContainerChangeListener>();
	
	public Container() {
		super();
		tuples = new ArrayList<Tuple>();
		indexMap = new HashMap<String, ArrayListMultimap<IndexKey,Tuple>>();
		indexDefinitionMap = new HashMap<String, IndexDefinition>();
	}
	
	@Override
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public ITupleHandler getTupleHandler() { return new TupleHandler(this); }
	
	@Override
	public Tuple createTuple() {
		Tuple t = new Tuple(columnCount());
		try {
			for (int i=0;i<columnCount();i++) {
				Column c = getColumn(i);
				t.setData(i, c.getDefaultValue());
			}
//			t.addTupleChangeListener(this);  TODO Check
			return t;
		} finally {
			fireContainerTupleCreatedEvent(new ContainerChangeEvent(this, t, -1));
		}
	}
	
	@Override
	public int size() { return tuples.size(); }

	@Override
	public Tuple getTuple(int index) {
		Tuple t = tuples.get(index);
		if (!t.containsTupleChangeListener(this))
			t.addTupleChangeListener(this);
		return  t;
	}

	@Override
	public void addTuple(Tuple t) {
		if (!t.containsTupleChangeListener(this))
			t.addTupleChangeListener(this);
		tuples.add(t);
		fireContainerTupleAddedEvent(new ContainerChangeEvent(this, t, indexOfTuple(t)));
	}

	@Override
	public void addTuple(int index, Tuple t) {
		if (!t.containsTupleChangeListener(this))
			t.addTupleChangeListener(this);
		tuples.add(index, t); 
		fireContainerTupleAddedEvent(new ContainerChangeEvent(this, t, index));
	}

	@Override
	public void setTuple(int index, Tuple t) {
		if (!t.containsTupleChangeListener(this))
			t.addTupleChangeListener(this);
		tuples.set(index, t);
		fireContainerTupleAddedEvent(new ContainerChangeEvent(this, t, index));  // TODO Tuple replaced
	}

	@Override
	public void removeTuple(int index) {
		Tuple t = getTuple(index);
		tuples.remove(index);
		fireContainerTupleRemovedEvent(new ContainerChangeEvent(this, t, index));
	}

	@Override
	public void removeTuple(Tuple t) {
		int index = indexOfTuple(t);
		//tuples.remove(t);
		removeTuple(index);
	}

	@Override
	public int indexOfTuple(Tuple t) {
		return tuples.indexOf(t);
	}
	
	@Override
	public void clear() {
		tuples.clear();
	}

	@Override
	public boolean isDirty() {
		for (Tuple t : tuples)
			if (t.isDirty()) return true;
		return false;
	}

	@Override
	public void sort(Column... columns) {
		Order[] sorts = new Order[columns.length];
		for (int i=0;i<columns.length;i++)
			sorts[i] = new Order(columns[i]);
		sort(sorts);
	}

	@Override
	public void sort(final Order... sorts) {
		if (sorts.length == 0) return;
		final int[] indexes = new int[sorts.length];
		for (int i=0;i<sorts.length;i++)
			indexes[i] = indexOfColumn(sorts[i].getColumnName());
		
		Collections.sort(tuples, new Comparator<Tuple>() {
			@SuppressWarnings("unchecked")
			@Override
			public int compare(Tuple o1, Tuple o2) {
				for (int i=0;i<sorts.length;i++) {
					Order s = sorts[i];
					int idx = indexes[i];
					Object a1 = o1.getData(idx);
					Object a2 = o2.getData(idx);
					if ((a1 == null && a2 == null) || s.getDirection() == OrderDirection.NONE) continue;
					else if (a1 == null && a2 != null) return s.getDirection().modificator() * -1;
					else if (a1 != null && a2 == null) return s.getDirection().modificator() * 1;
					else if (!(a1 instanceof Comparable)) continue;
					else {
						Comparable<Object> ca1 = (Comparable<Object>)a1;
						int ret = ca1.compareTo(a2);
						if (ret == 0) continue;
						return s.getDirection().modificator() * ret;
					}
				}
				return 0;
			}
		});
	}
	
	protected boolean filterResult(Tuple t, Filter filter) {
		int srci = indexOfColumn(filter.getColumn().getName());
		Column srcc = getColumn(srci);
		Object srcv = t.getData(srci);
		Object val = srcc.convert(filter.getValue());
		switch (filter.getOperator()) {
			case LIKE: 
				if (srcv != null && val != null && srcv instanceof String && val instanceof String) {
					String sval = (String)val;
					String srcvval = (String)srcv;
					String regex = "^"+sval.replace("_", ".").replace("%", ".*")+"$";
					return Util.hasMatches(regex, srcvval);
				} else return false;
			case NOT_LIKE: 
				if (srcv != null && val != null && srcv instanceof String && val instanceof String) {
					String sval = (String)val;
					String srcvval = (String)srcv;
					String regex = "^"+sval.replace("_", ".").replace("%", ".*")+"$";
					return !Util.hasMatches(regex, srcvval);
				} else return true;
			case EQUAL: return val.equals(srcv);
			case NOT_EQUAL: return !val.equals(srcv);
			case GREATER: return Util.isGreater(srcv, val);
			case GREATER_OR_EQUAL: return Util.isGreaterOrEqual(srcv, val);
			case LESS: return Util.isLess(srcv, val);
			case LESS_OR_EQUAL: return Util.isLessOrEqual(srcv, val);
			case IS_NOT_NULL: return srcv != null;
			case IS_NULL: return srcv == null;
		}
		return false;
	}

	protected boolean validateAgainstFilter(Tuple t, Filter ... filters) {
		boolean ret = false;
		for (int i=0;i<filters.length;i++) {
			Filter f = filters[i];
			if (i == 0) ret = filterResult(t, f);
			else {
				switch (f.getLink()) {
					case OR: ret |= filterResult(t, f); break;
					case OR_NOT: ret |= !filterResult(t, f); break;
					case XOR: ret ^= filterResult(t, f); break;
					case XOR_NOT: ret ^= !filterResult(t, f); break;
					case AND_NOT: ret &= !filterResult(t, f); break;
					default: ret &= filterResult(t, f); break;
				}
			}
		}
		return ret;
	}
	
	@Override
	public List<Tuple> list(Filter... filters) {
		if (filters.length == 0) return tuples;
		else {
			List<Tuple> l = new ArrayList<Tuple>();
			for (Tuple t : tuples)
				if (validateAgainstFilter(t, filters)) 
					l.add(t);
			return l;
		}
	}

	@Override
	public void buildPrimaryIndex() {
		buildIndex(DEFAULT_INDEX, primaryColumns());
	}
	
	@Override
	public void buildIndex(String indexName, Column... columns) {
		String[] names = new String[columns.length];
		for (int i=0;i<columns.length;i++)
			names[i] = columns[i].getName();
		buildIndex(indexName, names);
	}

	@Override
	public void buildIndex(String indexName, String... names) {
		if (names.length == 0) return;
		ArrayListMultimap<IndexKey, Tuple> alm = ArrayListMultimap.create();
		
		IndexDefinition idef = new IndexDefinition(indexName);
		int[] indexes = new int[names.length];
		for (int i=0;i<names.length;i++) {
			indexes[i] = indexOfColumn(names[i]);
			Column c = getColumn(indexes[i]);
			idef.addColumn(c, indexes[i]);
			System.out.println(Thread.currentThread().getName()+" "+getName()+"."+c.getName()+"["+c.getType()+"]");
		}
		indexDefinitionMap.put(indexName, idef);
		
		for (int i=0;i<size();i++) {
			Tuple t = getTuple(i);
			alm.put(new IndexKey(t.getData(indexes)), t);
		}
		
		indexMap.put(indexName, alm);
	}
	
	public void rebuildPrimaryIndex() {
		rebuildIndex(DEFAULT_INDEX, primaryColumns());
	}
	
	public void rebuildIndex(String indexName, Column ... columns) {
		String[] names = new String[columns.length];
		for (int i=0;i<columns.length;i++)
			names[i] = columns[i].getName();
		rebuildIndex(indexName, names);
	}
	
	public void rebuildIndex(String indexName, String ... names) {
		if (names.length == 0) return;
		indexDefinitionMap.remove(indexName);
		indexMap.remove(indexName);
		buildIndex(indexName, names);
	}
	
	public void rebuildIndex(IndexDefinition idef) {
		rebuildIndex(idef.getIndexName(), idef.names());
	}
	
	@Override
	public List<IndexKey> listIndexes() {
		return listIndexes(DEFAULT_INDEX);
	}
	
	@Override
	public List<IndexKey> listIndexes(String indexName) {
		List<IndexKey> ret = new ArrayList<IndexKey>();
		if (indexMap.containsKey(indexName)) 
			for (IndexKey key : indexMap.get(indexName).keySet())
				ret.add(key);
		return ret;
	}

	@Override
	public List<Tuple> listPerIndex(String indexName, IndexKey key) {
		if (!indexMap.containsKey(indexName)) return new ArrayList<Tuple>();
		else return indexMap.get(indexName).get(key);
	}

	@Override
	public List<Tuple> listPerIndex(IndexKey key) {
		if (!indexMap.containsKey(DEFAULT_INDEX)) 
			buildIndex(DEFAULT_INDEX, primaryColumns());
		return listPerIndex(DEFAULT_INDEX, key);
	}

	@Override
	public List<Tuple> listPerIndex(String indexName, Object... data) {
		return listPerIndex(new IndexKey(data));
	}

	@Override
	public List<Tuple> listPerIndex(Object... data) {
		return listPerIndex(new IndexKey(data));
	}
	
	public boolean containsIndexKey(IndexKey key) {
		return containsIndexKey(DEFAULT_INDEX, key);
	}
	
	public boolean containsIndexKey(String indexName, IndexKey key) {
		return listPerIndex(indexName, key).size() > 0;
	}

	@Override
	public List<IndexDefinition> listIndexDefinitions() {
		return new ArrayList<IndexDefinition>(indexDefinitionMap.values());
	}
	
	@Override
	public IndexDefinition getIndexDefinition() {
		return getIndexDefinition(DEFAULT_INDEX);
	}
	
	@Override
	public IndexDefinition getIndexDefinition(String indexName) {
		return indexDefinitionMap.get(indexName);
	}
	
	@Override
	public List<ContainerChangeListener> listContainerChangeListeners() {
		return listeners;
	}

	@Override
	public void addContainerChangeListener(ContainerChangeListener listener) {
		listeners.add(listener);		
	}

	@Override
	public void removeContainerChangeListener(ContainerChangeListener listener) {
		listeners.remove(listener);		
	}

	protected void fireContainerTupleCreatedEvent(ContainerChangeEvent evt) {
		for (ContainerChangeListener listener : listContainerChangeListeners())
			listener.tupleCreated(evt);
	}
	
	protected void fireContainerTupleAddedEvent(ContainerChangeEvent evt) {
		for (ContainerChangeListener listener : listContainerChangeListeners())
			listener.tupleAdded(evt);
	}
	
	protected void fireContainerTupleChangedEvent(ContainerChangeEvent evt) {
//		System.out.println("Tuple changed: "+getName()+"."+evt.getColumn().getName()+" old: "+evt.getOldTupleData()+" new: "+evt.getNewTupleData());
		for (ContainerChangeListener listener : listContainerChangeListeners())
			listener.tupleChanged(evt);
	}
	
	protected void fireContainerTupleRemovedEvent(ContainerChangeEvent evt) {
		for (ContainerChangeListener listener : listContainerChangeListeners())
			listener.tupleRemoved(evt);
	}
	
	@Override
	public void dataChanged(TupleChangeEvent event) {
		fireContainerTupleChangedEvent(new ContainerChangeEvent(this, getColumn(event.getIndex()), event.getTuple(), event.getOldValue(), event.getNewValue(), indexOfTuple(event.getTuple())));
		// refresh indexes
		for (IndexDefinition idef : listIndexDefinitions())
			if (idef.containsIndex(event.getIndex())) {
				int[] indexes = new int[idef.columnCount()];
				// TODO implement reindex
			}
	}

}
