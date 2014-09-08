package org.isf.commons.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

@Qualifier
@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD,ElementType.PARAMETER})
//@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DTO {
//	Class<?> value();
	@Nonbinding String value() default "";
	@Nonbinding Class<? extends DTOConvertRule> convertRule() default DefaultDTOConvertRule.class; 
	@Nonbinding boolean propertySupport() default false;
}
