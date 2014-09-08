package org.isf.commons.data;

public enum OrderDirection {

	NONE(0),
	ASC(1),
	DESC(-1);
	
	private final int modificator;
	
	private OrderDirection(int modificator) {
		this.modificator = modificator;
	}
	
	public int modificator() { return modificator; }
	
}
