package org.isf.commons.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IndexDefinition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2256326786055491428L;

	private String indexName;
	private List<Column> columns = new ArrayList<Column>();
	private List<Integer> indexes = new ArrayList<Integer>();
	
	public IndexDefinition(String indexName) {
		setIndexName(indexName);
	}
	
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public List<Column> getColumns() {
		return columns;
	}
	protected void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	
	public int columnCount() { return getColumns().size(); }
	public void addColumn(Column c, int index) { 
		getColumns().add(c);
		getIndexes().add(new Integer(index));
	}
	public Column getColumn(int index) { return getColumns().get(index); }
	
	public List<Integer> getIndexes() {
		return indexes;
	}
	protected void setIndexes(List<Integer> indexes) {
		this.indexes = indexes;
	}
	
	public int[] indexes() {
		int[] ret = new int[getIndexes().size()];
		for (int i=0;i<getIndexes().size();i++)
			ret[i] = getIndexes().get(i).intValue();
		return ret;
	}
	
	public boolean containsIndex(int index) {
		for (Integer idx : getIndexes())
			if (idx.intValue() == index) return true;
		return false;
	}
	
	public String[] names() {
		String[] ret = new String[columnCount()];
		for (int i=0;i<columnCount();i++)
			ret[i] = getColumn(i).getName();
		return ret;
	}
	
}
