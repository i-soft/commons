package org.isf.commons.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
public @interface DataBindingProperty {
	String name();
	String value() default "";
}
