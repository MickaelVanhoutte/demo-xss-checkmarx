package fr.lm.xsssanitizer.validation;

import org.apache.commons.text.StringEscapeUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Sanitize incoming forms by replacing Strings with their html escaped counterpart
 * @author Mickael Vanhoutte
 */
public class SanitizeValidator implements ConstraintValidator<Sanitize, Object> {

    @Override
    public void initialize(Sanitize constraintAnnotation) {

    }

    /**
     * Always return true, but replace strings (including collections of strings) with an escaped value
     *
     * @param form
     *     the request body input
     * @param constraintValidatorContext
     *     the context
     *
     * @return true when sanitized
     */
    @Override
    public boolean isValid(Object form, ConstraintValidatorContext constraintValidatorContext) {
        try {
            for (Field field : form.getClass().getDeclaredFields()) {
                if (isTypeString(field)) {
                    this.sanitizeString(field, form);
                } else if (isTypeCollectionOfString(field)) {
                    this.sanitizeCollectionString(field, form);
                }
            }
            return true;
        } catch (IllegalAccessException e) {
            return true;
        }
    }

    private void sanitizeString(Field field, Object form) throws IllegalAccessException {
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);
        String value = (String) field.get(form);
        if (value != null) {
            String escapedValue = StringEscapeUtils.escapeHtml4(value);
            field.set(form, escapedValue);
        }
        field.setAccessible(isAccessible);
    }

    private void sanitizeCollectionString(Field field, Object form) throws IllegalAccessException {
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);
        Object value = field.get(form);
        if (value != null) {
            Collection<String> sanitizedCollection = this.tryInstanciate(value);
            for (String cValue : (Collection<String>) value) {
                String cEscapedValue = StringEscapeUtils.escapeHtml4(cValue);
                sanitizedCollection.add(cEscapedValue);
            }
            field.set(form, sanitizedCollection);
        }
        field.setAccessible(isAccessible);
    }

    private Collection<String> tryInstanciate(Object value) {
        try {
            return (Collection<String>) value.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return new ArrayList<>();
        }
    }

    private boolean isTypeString(final Field field) {
        return String.class.isAssignableFrom(field.getType());
    }

    private boolean isTypeCollectionOfString(final Field field) {

        Type type = field.getGenericType();
        return type instanceof ParameterizedType && Collection.class.isAssignableFrom(field.getType()) && String.class.isAssignableFrom(
            (Class) ((ParameterizedType) type).getActualTypeArguments()[0]);
    }
}
