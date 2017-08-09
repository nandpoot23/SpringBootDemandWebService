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
@Target(ElementType.ANNOTATION_TYPE)
public @interface ValidatorClass {
    @SuppressWarnings("rawtypes")
    Class<? extends GenericValidator> value();
}