package org.isf.commons.data;

import java.util.List;

public interface IContainer extends IHeader {

	public static final String DEFAULT_INDEX = "primary";
	
	public String getName();
	public void setName(String name);
	
	// IHeader Methods

	public ITupleHandler getTupleHandler();
	
	public Tuple createTuple();
	public int size();
	public Tuple getTuple(int index);
	public void addTuple(Tuple t);
	public void addTuple(int index, Tuple t);
	public void setTuple(int index, Tuple t);
	public void removeTuple(int index);
	public void removeTuple(Tuple t);
	public int indexOfTuple(Tuple t);
	
	public void clear();
	
	public boolean isDirty();
	
	public void sort(Column ... columns);
	public void sort(Order ... sorts);
	
	public List<Tuple> list(Filter ... filters);
	
	public void buildPrimaryIndex();
	
	public void buildIndex(String indexName, Column ... columns);
	public void buildIndex(String indexName, String ... names);
	
	public void rebuildPrimaryIndex();
	public void rebuildIndex(String indexName, Column ... columns);
	public void rebuildIndex(String indexName, String ... names);
	public void rebuildIndex(IndexDefinition idef);
	
	public List<IndexKey> listIndexes();
	public List<IndexKey> listIndexes(String indexName);
	
	public List<Tuple> listPerIndex(String indexName, IndexKey key);
	public List<Tuple> listPerIndex(IndexKey key);
	public List<Tuple> listPerIndex(String indexName, Object ... data);
	public List<Tuple> listPerIndex(Object ... data);
	
	public boolean containsIndexKey(IndexKey key);
	public boolean containsIndexKey(String indexName, IndexKey key);
	
	public List<IndexDefinition> listIndexDefinitions();
	public IndexDefinition getIndexDefinition();
	public IndexDefinition getIndexDefinition(String indexName);
	
	public List<ContainerChangeListener> listContainerChangeListeners();
	public void addContainerChangeListener(ContainerChangeListener listener);
	public void removeContainerChangeListener(ContainerChangeListener listener);
	
}
