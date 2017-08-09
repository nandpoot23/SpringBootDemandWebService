package com.example.spring.boot.rest.util;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

public class EmailAddressValidator {

    public boolean isValid(Object value) {
        if (value == null)
            return true;

        if (value instanceof String) {
            return ConditionUtil.isEmail((String) value);
        }

        return false;
    }
}
