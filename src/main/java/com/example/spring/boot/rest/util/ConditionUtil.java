package com.example.spring.boot.rest.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

public final class ConditionUtil {

    private static final String DEFAULT_DATE_PATTERN = "MM/dd/yyyy";

    private ConditionUtil() {
    }

    /**
     * Validates whether an object is empty.
     *
     * @param bean
     *            bean to validate
     * @return true if the specified object is <code>null</code>, the length of
     *         the String is zero or the size of the array is zero.
     */
    public static boolean isEmpty(Object bean) {
        boolean error = bean == null;
        if (!error && bean instanceof String) {
            error = ((String) bean).length() == 0;
        }
        if (!error && bean instanceof Calendar) {
            error = ((Calendar) bean).getTimeInMillis() <= 0;
        }
        if (!error && bean.getClass().isArray()) {
            error = ((Object[]) bean).length == 0;
        }
        return error;
    }

    /**
     * Checks whether the given array contains any null valued elements
     *
     * @param array
     *            array to validate
     * @return true if the given array contains any elements that are null
     */
    public static boolean hasNullElements(Object[] array) {
        if (array == null) {
            throw new IllegalArgumentException("array should not be null");
        }
        for (int i = 0; i < array.length; i++) {
            Object object = array[i];
            if (object == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the given String array contains any null, blank or
     * whitespace valued elements
     *
     * @param array
     *            String array to validate
     * @return true if the given array contains any elements that are null,
     *         blank("") or whitespace
     */
    public static boolean hasBlankElements(String[] array) {
        if (array == null) {
            throw new IllegalArgumentException("array should not be null");
        }
        for (int i = 0; i < array.length; i++) {
            String object = array[i];
            if (StringUtils.isBlank(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates whether the specified date is greater than certain date.
     *
     * @param bean
     *            bean to validate.
     * @param daysFromNow
     *            If you want to check whether the specified date is today or
     *            later, you should specify daysFromNow as -1.
     * @return true if the specified date is greater than certain date.
     */
    public static boolean isGreaterThan(Calendar bean, int daysFromNow) {
        if (bean == null) {
            return false;
        }

        Calendar rhs = Calendar.getInstance();
        rhs.add(Calendar.DATE, daysFromNow);
        rhs.set(Calendar.HOUR, 0);
        rhs.set(Calendar.MINUTE, 0);
        rhs.set(Calendar.SECOND, 0);
        rhs.set(Calendar.MILLISECOND, 0);

        // Clone to avoid side effect
        Calendar lhs = (Calendar) bean.clone();
        lhs.set(Calendar.HOUR, 0);
        lhs.set(Calendar.MINUTE, 0);
        lhs.set(Calendar.SECOND, 0);
        lhs.set(Calendar.MILLISECOND, 0);
        return lhs.after(rhs);
    }

    /**
     * Validates whether the specified date is less than certain date.
     *
     * @param bean
     *            bean to validate.
     * @param daysFromNow
     *            If you want to check whether the specified date before today,
     *            you should specify daysFromNow as 0.
     * @return true if the specified date is less than certain date.
     */
    public static boolean isLessThan(Calendar bean, int daysFromNow) {
        if (bean == null) {
            return false;
        }

        Calendar rhs = Calendar.getInstance();
        rhs.add(Calendar.DATE, daysFromNow);
        rhs.set(Calendar.HOUR, 0);
        rhs.set(Calendar.MINUTE, 0);
        rhs.set(Calendar.SECOND, 0);
        rhs.set(Calendar.MILLISECOND, 0);

        Calendar lhs = (Calendar) bean.clone();
        lhs.set(Calendar.HOUR, 0);
        lhs.set(Calendar.MINUTE, 0);
        lhs.set(Calendar.SECOND, 0);
        lhs.set(Calendar.MILLISECOND, 0);
        return lhs.before(rhs);
    }

    /**
     * Validates whether the specified date is greater than certain date.
     *
     * @param daysFromNow
     *            If you want to check whether the specified date is today or
     *            later, you should specify daysFromNow as -1.
     * @param bean
     *            bean to validate.
     * @return true if the specified date is greater than certain date.
     */
    public static boolean isGreaterThan(int daysFromNow, Date bean) {
        if (bean == null) {
            return false;
        }

        Calendar target = Calendar.getInstance();
        target.setTime(bean);
        return isGreaterThan(target, daysFromNow);
    }

    /**
     * Validates whether the specified date is less than certain date.
     *
     * @param daysFromNow
     *            If you want to check whether the specified date before today,
     *            you should specify daysFromNow as 0.
     * @param bean
     *            bean to validate.
     * @return true if the specified date is less than certain date.
     */
    public static boolean isLessThan(int daysFromNow, Date bean) {
        if (bean == null) {
            return false;
        }

        Calendar target = Calendar.getInstance();
        target.setTime(bean);
        return isLessThan(target, daysFromNow);
    }

    /**
     * Validates that the value is in the specified range. The variable min
     * defines the minimum value. The variable max defines the maximum value
     *
     * @param bean
     *            bean to validate.
     * @param lMin
     *            defines the minimum value.
     * @param lMax
     *            defines the maximum value.
     * @return true if value is correctly formatted date.
     */
    public static boolean isInRange(Object bean, long lMin, long lMax) {
        if (bean == null) {
            return false;
        }

        try {
            long lValue = Long.parseLong(String.valueOf(bean));
            return GenericValidator.isInRange(lValue, lMin, lMax);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates that the value is a valid email address.
     *
     * @param bean
     *            bean to validate.
     * @return true if value is a valid email address.
     */
    public static boolean isEmail(String bean) {
        if (bean == null) {
            return false;
        }

        return GenericValidator.isEmail(bean);
    }

    /**
     * Validates that the value against a regular expression. The variable
     * expression defines the regular expression to validate against.
     *
     * @param bean
     *            bean to validate.
     * @param expression
     *            defines the regular expression to validate against.
     * @return true if value matches the regular expression.
     */
    public static boolean isRegExp(String bean, String expression) {
        if (bean == null || expression == null) {
            return false;
        }

        return GenericValidator.matchRegexp(bean, expression);
    }

    /**
     * Validates that the value is a valid credit card number.
     *
     * @param bean
     *            bean to validate.
     * @return true if value is a valid credit card number.
     */
    public static boolean isCreditCard(String bean) {
        if (bean == null) {
            return false;
        }
        return GenericValidator.isCreditCard(bean);
    }

    /**
     * Validates that the value is an integer.
     *
     * @param bean
     *            bean to validate.
     * @return true if value is an integer.
     */
    public static boolean isInteger(Object bean) {
        if (bean == null) {
            return false;
        }
        Long lValue = GenericTypeValidator.formatLong(String.valueOf(bean));
        return lValue != null;
    }

    /**
     * Validates that the value is a date. The variable datePattern defines the
     * pattern used the check date.
     *
     * @param bean
     *            bean to validate.
     * @param datePattern
     *            the pattern used the check date
     * @return true if value is correctly formatted date.
     */
    public static boolean isDate(String bean, String datePattern) {
        if (bean == null) {
            return false;
        }
        if (datePattern == null) {
            datePattern = DEFAULT_DATE_PATTERN;
        }

        Date dateValue = GenericTypeValidator.formatDate(bean, datePattern, false);

        return dateValue != null;
    }

}
