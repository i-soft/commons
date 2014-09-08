package org.isf.commons.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataBinding {
	String modelAttribute();
	String targetAttribute() default "";
	Class<? extends IDataBindingRule> rule();
	DataBindingProperty[] properties() default {};
}
