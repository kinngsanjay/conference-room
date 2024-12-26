package app.conferenceroom.domain.validator.meetingrange;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MeetingTimeRangeValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMeetingTimeRange {
    String message() default "Invalid meeting time range";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
