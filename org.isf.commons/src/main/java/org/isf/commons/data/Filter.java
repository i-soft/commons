package org.isf.commons.data;

public class Filter implements Cloneable {

	private LinkOperator link = LinkOperator.AND;
	private Column column;
	private FilterOperator operator = FilterOperator.EQUAL;
	private Object value;
	private String command;
	
	public Filter() {
		super();
	}
	
	public Filter(Column column, LinkOperator link, FilterOperator operator, Object value) {
		this();
		setColumn(column);
		setLink(link);
		setOperator(operator);
		setValue(value);
	}
	
	
	public Filter(Column column, FilterOperator operator, Object value) {
		this(column, LinkOperator.AND, operator, value);
	}
	
	public Filter(Column column, Object value) {
		this(column, LinkOperator.AND, FilterOperator.EQUAL, value);
	}
	
	public LinkOperator getLink() {
		return link;
	}
	public void setLink(LinkOperator link) {
		this.link = link;
	}
	public Column getColumn() {
		return column;
	}
	public void setColumn(Column column) {
		this.column = column;
	}
	public FilterOperator getOperator() {
		return operator;
	}
	public void setOperator(FilterOperator operator) {
		this.operator = operator;
	}
	
	public Object getValue() { return value; }
	public void setValue(Object value) { this.value = value; }
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	
	public boolean isCommand() { return getCommand() != null && getCommand().length() > 0; }
	
	public Filter clone() {
		try { 
			Filter cl = (Filter)super.clone();
			
			return cl;
		} catch(CloneNotSupportedException cnse) {
			return null;
		}
	}
}
