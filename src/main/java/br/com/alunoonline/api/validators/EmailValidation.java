package br.com.alunoonline.api.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EmailValidator.class)
public @interface EmailValidation {

    String message() default "Email precisa ser @fuji.com. Valor digitado foi: ";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload() default {};
}
