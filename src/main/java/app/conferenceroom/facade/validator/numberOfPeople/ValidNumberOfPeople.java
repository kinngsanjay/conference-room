package app.conferenceroom.facade.validator.numberOfPeople;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NumberOfPeopleValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNumberOfPeople {
    String message() default "Invalid number of people";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
