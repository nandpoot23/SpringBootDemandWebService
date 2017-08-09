package com.example.spring.boot.rest.validator;

import org.springframework.validation.Errors;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

public interface Validator<T> {
    public void validate(T object, Errors errors);
}
