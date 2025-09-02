package app.conferenceroom.validator.meetingrange;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TimeRangeValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTimeRange {
    String message() default "Invalid meeting time range";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
