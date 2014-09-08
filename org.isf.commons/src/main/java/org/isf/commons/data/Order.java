package org.isf.commons.data;

public class Order {

	private OrderDirection direction;
	private String columnName;
	
	public Order() {
		this("");
	}
	
	public Order(String name, OrderDirection direction) {
		super();
		setColumnName(name);
		setDirection(direction);
	}
	
	public Order(String name) {
		this(name, OrderDirection.ASC);
	}
	
	public Order(Column column) {
		this(column.getName());
	}
	
	public Order(Column column, OrderDirection direction) {
		this(column.getName(), direction);
	}
	
	public OrderDirection getDirection() {
		return direction;
	}
	public void setDirection(OrderDirection direction) {
		this.direction = direction;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
}
