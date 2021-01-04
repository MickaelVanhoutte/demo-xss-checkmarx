package fr.lm.xsssanitizer.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to add on form resources. String and Collection of String will be escaped to prevent XSS injections
 * @author Mickael Vanhoutte
 */
@Target({ TYPE, ANNOTATION_TYPE, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = SanitizeValidator.class)
@Documented
public @interface Sanitize {

    String message() default "Input data should be escaped";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
