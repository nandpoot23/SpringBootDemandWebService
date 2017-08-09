package com.example.spring.boot.rest.validator;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import com.example.spring.boot.rest.exception.Message;
import com.example.spring.boot.rest.util.BigDecimalUtil;

/**
 * This aspect intercepts service calls and validates the operation parameters.
 * 
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

public class ValidationAspect {

    private Log log = LogFactory.getLog(ValidationAspect.class);

    @SuppressWarnings("rawtypes")
    private List<Validator> validators = new ArrayList<Validator>();
    
    @SuppressWarnings("rawtypes")
    private List<ServiceValidator> serviceValidators = new ArrayList<ServiceValidator>();
    private boolean validateDomainObjectsFirst = false;
    private boolean stopNextLevelValidationOnErrors = false;

    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();

        if (args != null && args.length > 0) {
            final List<Errors> errors = new ArrayList<Errors>();
            // Service operation level validation.
            if (!validateDomainObjectsFirst) {
                validateServiceMethod(pjp, errors);
            }

            // Domain object level valdiation
            if (continueValidation(errors)) {
                for (int i = 0; i < args.length; i++) {
                    validateDomainObject(errors, args[i], getArgumentName(i), getArgAnnotations(pjp, i));
                }
            }

            if (validateDomainObjectsFirst && continueValidation(errors)) {
                validateServiceMethod(pjp, errors);
            }

            processErrors(errors);
        }
        return pjp.proceed(args);
    }

    private boolean continueValidation(List<Errors> errors) {
        return errors.isEmpty() || !stopNextLevelValidationOnErrors;
    }

    /**
     * Process validation errors. Throws ApplicationException if there is any
     * validation errors.
     * 
     * @param errors
     */
    private void processErrors(final List<Errors> errors) {
        if (errors.size() > 0) {
            List<Message> msgs = new ArrayList<Message>();
            String firstCode = null;
            for (Errors objectErrors : errors) {
                for (int i = 0; i < objectErrors.getAllErrors().size(); i++) {
                    ObjectError objError = (ObjectError) objectErrors.getAllErrors().get(i);

                    Object[] errorArgs = objError.getArguments();
                    if (errorArgs != null && errorArgs.length > 0 && errorArgs[0] != null
                            && errorArgs[0].getClass().isArray()) {
                        errorArgs = (Object[]) errorArgs[0];
                    }

                    if (firstCode == null) {
                        firstCode = objError.getCode();
                    }

                    msgs.add(new Message(objError.getCode(), errorArgs, objError.getDefaultMessage()));
                }
            }
            FrameworkValidationError fve = new FrameworkValidationError(firstCode);
            fve.setValidationMessages(msgs);
            throw fve;
        }
    }

    /**
     * Validates at service method level using ServiceValidator.
     * 
     * @param pjp
     * @param errors
     */
    @SuppressWarnings("rawtypes")
    private void validateServiceMethod(ProceedingJoinPoint pjp, final List<Errors> errors) {
        for (ServiceValidator serviceValidator : serviceValidators) {
            if (validatorSupports(pjp.getTarget().getClass(), serviceValidator)) {
                final BindException serviceMethodErrors = new BindException(pjp.getTarget(), pjp.getTarget().getClass()
                        .getName());
                serviceValidator.validate(pjp.getSignature().getName(), pjp.getArgs(), serviceMethodErrors);
                if (serviceMethodErrors.hasErrors()) {
                    errors.add(serviceMethodErrors);
                }
            }
        }
    }

    /**
     * Validates the domain object using associated validator.
     * 
     * @param errors
     * @param value
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    private void validateDomainObject(final List<Errors> errors, final Object value, String fieldOrArgumentName,
            Annotation[] annotations) throws Exception {

        validateAgainstAnnotations(errors, value, fieldOrArgumentName, annotations);

        if (value == null)
            return;

        if (value.getClass().isArray()) {
            if (!(value.getClass().getComponentType().equals(byte.class) || value.getClass().getComponentType()
                    .equals(int.class))) {
                for (final Object argValue : ((Object[]) value)) {
                    validateDomainObject(errors, argValue, fieldOrArgumentName, null);
                }
            }
        } else if (isCollection(value.getClass())) {
            for (final Object argValue : ((Collection) value)) {
                validateDomainObject(errors, argValue, fieldOrArgumentName, null);
            }
        }

        if (isNotSupportedType(value.getClass()))
            return;

        for (final Validator validator : getValidators()) {
            if (validatorSupports(value.getClass(), validator)) {
                validateAndAddErrors(value, validator, errors);
            }
        }
        validateAttributes(value, errors);
    }

    private boolean isNotSupportedType(Class<?> type) {
        return type.isPrimitive() || !type.getName().startsWith("com.example.");
    }

    /**
     * Validate the argument or object attribute against validation annotations.
     * 
     * @param errors
     *            The errors container that maintains all the validation errors.
     * @param value
     *            The value of the operation argument or object attribute.
     * @param fieldOrArgumentName
     *            The name of operaton argument or object attribute.
     * @param annotations
     *            Annotations defined on the operation argument or object
     *            attribute.
     */
    @SuppressWarnings("unchecked")
    private void validateAgainstAnnotations(List<Errors> errors, Object value, String fieldOrArgumentName,
            Annotation[] annotations) {
        if (annotations == null || annotations.length == 0)
            return;

        final BindException validationErrors = new BindException(new Object(), "");
        for (Annotation annotation : annotations) {

            if (annotation instanceof UserGroupsAllowed) {
                continue;
            }

            if (log.isDebugEnabled()) {
                log.debug("Found validation annotation " + annotation + " for " + fieldOrArgumentName);
            }

            ValidatorClass validatorClassAnn = annotation.annotationType().getAnnotation(ValidatorClass.class);
            ErrorCode errorCodeAnn = annotation.annotationType().getAnnotation(ErrorCode.class);

            if (validatorClassAnn != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Validate using " + validatorClassAnn.value() + " with code:" + errorCodeAnn.code()
                            + " and default message:" + errorCodeAnn.defaultMessage());
                }
                @SuppressWarnings("rawtypes")
                GenericValidator validator = newValidatorInstance(validatorClassAnn.value());
                validator.initialize(annotation);
                if (!validator.isValid(value)) {
                    validationErrors.reject(errorCodeAnn.code(), new Object[] {
                            fieldOrArgumentName,
                            (value instanceof BigDecimal ? BigDecimalUtil.bigDecimalToString((BigDecimal) value)
                                    : value), getAnnotationValue(annotation) }, errorCodeAnn.defaultMessage());
                }
            }
        }
        if (validationErrors.hasErrors()) {
            errors.add(validationErrors);
        }
    }

    /**
     * Invokes value() method of the annotation if the method exists and returns
     * the result of the invocation.
     * 
     * @param annotation
     * @return
     */
    private Object getAnnotationValue(Annotation annotation) {
        try {
            Method valueMethod = annotation.getClass().getMethod("value", null);
            return valueMethod.invoke(annotation, null);
        } catch (Exception ignoreIt) {
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    private GenericValidator newValidatorInstance(Class<? extends GenericValidator> validatorClass) {
        try {
            return validatorClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to construct new instance of validator class "
                    + validatorClass.getName() + " using default constructor.");
        }
    }

    private void validateAttributes(final Object arg, final List<Errors> errors) throws Exception {
        final PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(arg.getClass());
        for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getReadMethod() != null) {
                final Object propertyValue = propertyDescriptor.getReadMethod().invoke(arg);
                Annotation[] annotations = getFieldAnnotations(arg.getClass(), propertyDescriptor.getName());
                validateDomainObject(errors, propertyValue, propertyDescriptor.getName(), annotations);
            }
        }
    }

    private Annotation[] getFieldAnnotations(Class<?> clazz, String fieldName) {
        Field field = getDeclaredField(clazz, fieldName);
        Class<?> tmpClazz = clazz;
        while (field == null && !tmpClazz.equals(Object.class)) {
            tmpClazz = tmpClazz.getSuperclass();
            field = getDeclaredField(tmpClazz, fieldName);
        }

        if (field == null) {
            if (Character.isUpperCase(fieldName.charAt(0))) {
                return getFieldAnnotations(clazz, StringUtils.uncapitalize(fieldName));
            }
            return null;
        } else {
            return field.getAnnotations();
        }
    }

    private Field getDeclaredField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException ignoreIt) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private void validateAndAddErrors(final Object arg, @SuppressWarnings("rawtypes") final Validator validator,
            final List<Errors> errors) {
        final BindException objErrors = new BindException(arg, arg.getClass().getName());
        validator.validate(arg, objErrors);
        if (objErrors.hasErrors()) {
            errors.add(objErrors);
        }
    }

    private boolean validatorSupports(Class<?> fieldType, Object validator) {
        if (isFieldTypeGenericArgument(fieldType, validator.getClass().getGenericInterfaces())) {
            return true;
        }
        return isFieldTypeGenericArgument(fieldType, new Type[] { validator.getClass().getGenericSuperclass() });
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean isFieldTypeGenericArgument(Class<?> fieldType, Type[] genericTypes) {
        if (genericTypes != null && genericTypes.length > 0) {
            if (genericTypes[0] instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) genericTypes[0]).getActualTypeArguments();
                if (actualTypeArguments.length > 0) {
                    return ((Class) actualTypeArguments[0]).isAssignableFrom(fieldType);
                }
            }
        }
        return false;
    }

    private boolean isCollection(final Class<?> clazz) {
        return clazz.isAssignableFrom(Collection.class);
    }

    private Annotation[] getArgAnnotations(ProceedingJoinPoint pjp, int argIdx) {
        Class<?> serviceClazz = pjp.getSignature().getDeclaringType();
        if (!serviceClazz.isInterface() && serviceClazz.getInterfaces().length > 0) {
            serviceClazz = serviceClazz.getInterfaces()[0];
        }
        Method[] methods = serviceClazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(pjp.getSignature().getName())) {

                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                if (parameterAnnotations.length < argIdx + 1) {
                    return new Annotation[0];
                } else {
                    return method.getParameterAnnotations()[argIdx];
                }
            }
        }

        return null;
    }

    /**
     * Returns the proper argument name.
     */
    private String getArgumentName(int i) {
        String numberTh = "th";
        if (i == 0) {
            numberTh = "st";
        } else if (i == 1) {
            numberTh = "nd";
        } else if (i == 2) {
            numberTh = "rd";
        }
        return ++i + numberTh + " argument";
    }

    @SuppressWarnings("rawtypes")
    public List<ServiceValidator> getServiceValidators() {
        return serviceValidators;
    }

    public void setServiceValidators(@SuppressWarnings("rawtypes") List<ServiceValidator> serviceValidators) {
        this.serviceValidators = serviceValidators;
    }

    /**
     * @return Returns the validators.
     */
    @SuppressWarnings("rawtypes")
    public List<Validator> getValidators() {
        return validators;
    }

    /**
     * @param validators
     *            The validators to set.
     */
    public void setValidators(@SuppressWarnings("rawtypes") List<Validator> validators) {
        this.validators = validators;
    }

    public boolean isValidateDomainObjectsFirst() {
        return validateDomainObjectsFirst;
    }

    public void setValidateDomainObjectsFirst(boolean validateDomainObjectsFirst) {
        this.validateDomainObjectsFirst = validateDomainObjectsFirst;
    }

    public boolean isStopNextLevelValidationOnErrors() {
        return stopNextLevelValidationOnErrors;
    }

    public void setStopNextLevelValidationOnErrors(boolean stopNextLevelValidationOnErrors) {
        this.stopNextLevelValidationOnErrors = stopNextLevelValidationOnErrors;
    }
}
