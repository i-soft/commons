package org.isf.commons.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.isf.commons.bean.DefaultConverter;
import org.isf.commons.bean.IConverter;
import org.isf.commons.bean.Type;

public class Column implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9175385141034882231L;
	
	public static final int UNLIMITED = -1;

	private String name;
	private String displayName;
	private char fillChar = ' ';
	private boolean fillDirectionRight = true;
	private int length = UNLIMITED;
	private int precision = 0;
	private int scale = 0;
	private boolean primary = false;
	private boolean unique = false;
	private boolean nullable = true;
	private Type type = Type.STRING;
	private Object defaultValue;
	private IConverter converter;
	
	private Column parent = null;
	private List<Column> columns = new ArrayList<Column>();
	
	private int rowSpan = 1;
	private int columnSpan = 1;
	
	public Column() {
		converter = new DefaultConverter();
	}
	
	public Column(String name, Type type) {
		this(name,null,type);
	}
	
	public Column(String name, String displayName, Type type) {
		this();
		setName(name);
		setDisplayName(displayName);
		setType(type);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return (displayName == null) ? getName() : displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public char getFillChar() {
		return fillChar;
	}
	public void setFillChar(char fillChar) {
		this.fillChar = fillChar;
	}
	public boolean isFillDirectionRight() {
		return fillDirectionRight;
	}
	public void setFillDirectionRight(boolean fillDirectionRight) {
		this.fillDirectionRight = fillDirectionRight;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public int getScale() { 
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	public boolean isPrimary() {
		return primary;
	}
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	public boolean isUnique() {
		return unique;
	}
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	public boolean isNullable() {
		return nullable;
	}
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	public Object getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public IConverter getConverter() {
		return converter;
	}
	public void setConverter(IConverter converter) {
		this.converter = converter;
	}
	public int getRowSpan() {
		return rowSpan;
	}
	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}
	public int getColumnSpan() {
		return columnSpan;
	}
	public void setColumnSpan(int columnSpan) {
		this.columnSpan = columnSpan;
	}

	public Column getParentColumn() { return parent; }
	public void setParentColumn(Column parent) { this.parent = parent; }
	
	public List<Column> getColumns() { return columns; }
	public void setColumns(List<Column> columns) { this.columns = columns; }
	public int columnSize() { return getColumns().size(); }
	public boolean hasColumns() { return columnSize() > 0; }
	public void addColumn(Column col) {
		col.setParentColumn(this);
		getColumns().add(col);
	}
	public Column getColumn(int index) { return getColumns().get(index); }

	public Object convert(Object data) {
		return getConverter().convert(getType().wrapper()[0], data);
	}
	
	public Column clone() {
		try {
			return (Column)super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
}
