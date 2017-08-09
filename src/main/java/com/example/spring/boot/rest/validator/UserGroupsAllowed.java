package com.example.spring.boot.rest.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a field that can only be returned for certain user groups.
 * 
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface UserGroupsAllowed {
    /**
     * Comma separated list of user groups.
     */
    String value();
}