package org.isf.commons.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DTOAttribute {
	String value();
	Class<?> assignableFrom() default Object.class;
	int hierarchy() default 99;
	Class<? extends DTOAttributeConvertRule> convertRule() default DefaultDTOAttributeConvertRule.class;
	String mapKey() default "";
	boolean oneWay() default false;
	boolean force() default true;
}
