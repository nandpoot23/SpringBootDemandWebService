package com.example.spring.boot.rest.util;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

public class LengthValidator {

    private int length;

    public boolean isValid(Object value) {
        if (value == null)
            return true;

        if (value instanceof String && length == ((String) value).length()) {
            return true;
        } else if (value.getClass().isArray() && length == ((Object[]) value).length) {
            return true;
        }

        return false;
    }
}
