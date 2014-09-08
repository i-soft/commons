package org.isf.commons.dto;

import org.isf.commons.bean.Attribute;
import org.isf.commons.bean.Bean;

public class DTOInjectionData {
	
	private Bean targetBean;
	private Bean sourceBean;
	private Attribute targetAttribute;
	private Attribute sourceAttribute;
	private Object targetObject;
	private Object sourceObject;
	private DTOAttribute dtoAttr;

	public DTOInjectionData(Bean targetBean, Bean sourceBean, Attribute targetAttribute, Attribute sourceAttribute,
			Object targetObject, Object sourceObject, DTOAttribute dtoAttr) {
		this.targetBean = targetBean;
		this.sourceBean = sourceBean;
		this.targetAttribute = targetAttribute;
		this.sourceAttribute = sourceAttribute;
		this.targetObject = targetObject;
		this.sourceObject = sourceObject;
		this.dtoAttr = dtoAttr;
	}

	public Bean getTargetBean() {
		return targetBean;
	}

	public Bean getSourceBean() {
		return sourceBean;
	}

	public Attribute getTargetAttribute() {
		return targetAttribute;
	}

	public Attribute getSourceAttribute() {
		return sourceAttribute;
	}

	public Object getTargetObject() {
		return targetObject;
	}

	public Object getSourceObject() {
		return sourceObject;
	}
	
	public DTOAttribute getDTOAttribute() {
		return dtoAttr;
	}
	
}
