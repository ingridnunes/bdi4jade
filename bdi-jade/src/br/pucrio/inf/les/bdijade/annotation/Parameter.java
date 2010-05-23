/*
 * Created on 20/12/2009 21:12:31 
 */
package br.pucrio.inf.les.bdijade.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ingrid
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Parameter {

	public enum Direction { IN, OUT, INOUT };

	Direction direction() default Direction.IN;
	boolean mandatory() default false;
	
}
