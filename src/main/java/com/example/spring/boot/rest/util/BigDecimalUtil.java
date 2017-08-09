package com.example.spring.boot.rest.util;

import java.math.BigDecimal;

/**
 * Converts a {@link BigDecimal} value to the canonical string representation.
 * 
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

public class BigDecimalUtil {

    public static String bigDecimalToString(BigDecimal value) {
        if (value == null) {
            return null;
        }

        String result = String.valueOf(value.doubleValue());

        if (result.endsWith(".0")) {
            result = result.substring(0, result.length() - 2);
        }

        return result;
    }
}