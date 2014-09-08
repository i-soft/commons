package org.isf.commons.data;

import java.util.List;

public interface IHeader {

	public List<Column> columns();
	
	public int columnCount();	
	public int indexOfColumn(Column col);
	public int indexOfColumn(String name);
		
	public Column getColumn(int index);
	public Column getColumn(String name);
	
	public void addColumn(Column col);
	
	public void removeColumn(Column col);
	public void removeColumn(String name);
	public void removeColumn(int index);
	public int[] indexes();
	public Column[] getColumns(int ... indexes);
	
	public int primaryCount();
	public int[] primaryIndexes();
	public Column[] primaryColumns();
	public boolean hasPrimaryColumns();
	
	public int levelCount();
	public List<Column> getColumnsFromLevel(int level);
	
}
