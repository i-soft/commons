package org.isf.commons.dto;

public interface DTOAttributeConvertRule {

	public static final String MAP_KEY_INVOKE = "invoke";
	
	public Object marshallAttribute(DTOInjectionData did) throws Exception;
	public Object unmarshallAttribute(DTOInjectionData did) throws Exception;
	
}
