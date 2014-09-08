package org.isf.commons.data;

import java.util.ArrayList;
import java.util.List;

public class Header implements IHeader {

	private List<Column> columns;
	
	public Header() {
		super();
		columns = new ArrayList<Column>();
	}
	
	@Override
	public List<Column> columns() { return columns; }

	@Override
	public int columnCount() { return columns.size(); }

	@Override
	public int indexOfColumn(Column col) { return indexOfColumn(col.getName()); }

	@Override
	public int indexOfColumn(String name) {
		for (int i=0;i<columnCount();i++)
			if (name.equalsIgnoreCase(getColumn(i).getName())) return i;
		return -1;
	}

	@Override
	public Column getColumn(int index) { return columns.get(index); }

	@Override
	public Column getColumn(String name) {
		int idx = indexOfColumn(name);
		return idx < 0 ? null : columns.get(idx); 
	}

	@Override
	public void addColumn(Column col) {
		columns.add(col);
	}

	@Override
	public void removeColumn(Column col) { removeColumn(indexOfColumn(col)); }

	@Override
	public void removeColumn(String name) { removeColumn(indexOfColumn(name)); }

	@Override
	public void removeColumn(int index) { columns.remove(index); }

	@Override
	public int[] indexes() {
		int[] indexes = new int[columnCount()];
		for (int i=0;i<columnCount();i++)
			indexes[i] = i;
		return indexes;
	}
	
	@Override
	public Column[] getColumns(int ... indexes) {
		List<Column> ret = new ArrayList<Column>();
		for (int index : indexes)
			ret.add(getColumn(index));
		return ret.toArray(new Column[0]);
	}
	
	@Override
	public int primaryCount() {
		int cnt = 0;
		for (Column c : columns)
			if (c.isPrimary()) cnt++;
		return cnt;
	}
	
	@Override
	public int[] primaryIndexes() {
		int[] indexes = new int[primaryCount()];
		int ii = 0;
		for (int i=0;i<columnCount();i++)
			if (getColumn(i).isPrimary())
				indexes[ii++] = i; 
		return indexes;
	}
	
	@Override
	public Column[] primaryColumns() {
		List<Column> ret = new ArrayList<Column>();
		for (Column col : columns)
			if (col.isPrimary()) ret.add(col);
		return ret.toArray(new Column[0]);
	}
	
	@Override
	public boolean hasPrimaryColumns() {
		for (Column c : columns)
			if (c.isPrimary()) return true;
		return false;
	}

	protected int levelCount(final Column col, int hop) {
		int xhop = hop+1;
		for (Column c : col.getColumns()) 
			xhop = Math.max(xhop, levelCount(c, hop+1));
		return xhop;
	}	
	
	@Override
	public int levelCount() {
		int max = 0;
		for (Column c : columns()) 
			max = Math.max(max, levelCount(c, 0));
		return max;
	}
	
	protected List<Column> getColumnsFromLevel(final Column col, int hop, int level) {
		List<Column> l = new ArrayList<Column>();
		int xhop = hop+1;
		if (xhop == level) l.add(col);
		else {
			for (Column c : col.getColumns())
				l.addAll(getColumnsFromLevel(c, xhop, level));
		}
		return l;
	}

	@Override
	public List<Column> getColumnsFromLevel(int level) {
		List<Column> l = new ArrayList<Column>();
		for (Column c : columns())
			l.addAll(getColumnsFromLevel(c, 0, level));
		return l;
	}
	
//	public static void main(String[] args) throws Exception {
//		Header h = new Header();
//		
//		Column c = new Column("Type", Type.STRING);
//		h.addColumn(c);
//		
//		Date[] dates = DateUtil.getLastWeeks(2);
//		for (int i=0;i<dates.length;i++) {
//			Date d = dates[i];
//			if (i%7 == 0) {
//				Calendar cal = DateUtil.getISOCalendar();
//				cal.setTime(d);
//				c = new Column("KW "+cal.get(Calendar.WEEK_OF_YEAR)+" "+cal.get(Calendar.YEAR), Type.DOUBLE);
//				h.addColumn(c);
//			}
//			Column sub = new Column(DateUtil.toString("dd.MM.", d), Type.DOUBLE);
//			c.addColumn(sub);
//		}
//		
//		System.out.println("level count: "+h.levelCount());
//		for (Column cc : h.getColumnsFromLevel(2))
//			System.out.println(cc.getDisplayName());
//	}
}
