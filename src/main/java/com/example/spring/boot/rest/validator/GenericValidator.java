package com.example.spring.boot.rest.validator;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

public interface GenericValidator<T> {
    
    /**
     * Initializes the validator with the annotation object.
     */
    public void initialize(T t);

    /**
     * Returns false if the value is not valid based on the annoation.
     */
    public boolean isValid(Object value);
}
