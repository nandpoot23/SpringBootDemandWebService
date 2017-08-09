package com.example.spring.boot.rest.validator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.validation.Errors;

/**
 * Base class for validator implementations for service operations.
 * 
 * The validation methods must be annotationed with <code>ValidatesMethod</code>
 * with the name of service method to be validated against. The parameters of
 * the validation methods must be <code>Errors</code> plus parameters of the
 * service method.
 * 
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

public abstract class ServiceValidator<T> {

    public final void validate(String methodName, Object[] args, Errors errors) {
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            ValidatesMethod annotation = method.getAnnotation(ValidatesMethod.class);

            if (annotation != null && annotation.value().equals(methodName)) {
                try {
                    if (isValidationMethodValid(method, args)) {
                        method.invoke(this, addErrorsToArgs(errors, args));
                    } else {
                        throw new RuntimeException("Invalid validation method for " + methodName
                                + ". It must take Errors plus service method arguments as parameters.");
                    }
                    break;
                } catch (Exception e) {
                    throw new RuntimeException("Unable to validate service method call.", e);
                }
            }
        }
    }

    /**
     * Check wether the validation method defines correct parameter list.
     * 
     * @param method
     * @param args
     * @return
     */
    private boolean isValidationMethodValid(Method method, Object[] args) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != args.length + 1) {
            return false;
        }

        for (int i = 1; i < parameterTypes.length; i++) {
            Class<?> argumentType = parameterTypes[i];
            if (!(args[i - 1] == null || argumentType.isAssignableFrom(args[i - 1].getClass()))) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    private Object[] addErrorsToArgs(Errors errors, Object[] args) {
        @SuppressWarnings("rawtypes")
        List resultArgs = new ArrayList();
        resultArgs.add(errors);
        resultArgs.addAll(Arrays.asList(args));
        return resultArgs.toArray();
    }

}
