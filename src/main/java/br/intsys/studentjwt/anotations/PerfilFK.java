package br.intsys.studentjwt.anotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;

import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

import br.intsys.studentjwt.validator.PerfilFKValidator;

@Constraint(validatedBy = PerfilFKValidator.class)
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface PerfilFK {
    String message() default "Perfil invalido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default { };
}