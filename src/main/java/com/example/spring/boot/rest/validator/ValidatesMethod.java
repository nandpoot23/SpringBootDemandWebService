package com.example.spring.boot.rest.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ValidatesMethod {

    /**
     * The name of the method to validate against.
     */
    String value();

}
