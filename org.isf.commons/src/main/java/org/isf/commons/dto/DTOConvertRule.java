package org.isf.commons.dto;

public interface DTOConvertRule {

	public <T> T marshall(Class<T> dtoClazz, Object data) throws Exception;
	
//	public Object unmarshall(Object dto, Object data) throws Exception;
	public <T> T unmarshall(Object dto, T data) throws Exception;
	
}
