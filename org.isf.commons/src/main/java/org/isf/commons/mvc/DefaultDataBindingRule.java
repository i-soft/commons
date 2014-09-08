package org.isf.commons.mvc;

public class DefaultDataBindingRule implements IDataBindingRule {

	@Override
	public DataBindingSupport bind(DataBindingContext context) {
		DataBindingSupport support = new DataBindingSupport(context);
		
		return support;
	}

}
