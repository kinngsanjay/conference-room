package app.conferenceroom.domain.validator.meetingrange;

import app.conferenceroom.api.dto.TimeRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

@Slf4j
public class TimeRangeValidator implements ConstraintValidator<ValidTimeRange, TimeRange> {
    private boolean addErrorMessage(ConstraintValidatorContext context, String message) {
        log.info("MeetingTimeRangeValidator Error message: {}", message);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        return false;
    }

    @Override
    public boolean isValid(TimeRange timeRange, ConstraintValidatorContext context) {
        if (timeRange == null) {
            addErrorMessage(context, "Time Range cannot be null");
            return false;
        }
        if (timeRange.startTime() == null || timeRange.endTime() == null) {
            addErrorMessage(context, "Start Time or End Time cannot be null");
            return false;
        }
        return validateTimeRange(timeRange, context);
    }

    private boolean validateTimeRange(TimeRange timeRange, ConstraintValidatorContext context) {
        log.info("Validate time range: {}", timeRange);
        if(!isStartTimeValid(timeRange)) {
            return addErrorMessage(context, "Start Time should be after current time");
        }
        if(!isEndTimeValid(timeRange)) {
            return addErrorMessage(context, "End Time cannot be earlier than Start time");
        }
        if(!correctTimeStamp(timeRange)) {
            return addErrorMessage(context, "Entered Time is not suitable input");
        }
        return true;
    }

    private boolean isStartTimeValid(TimeRange timeRange) {
        LocalTime currentDateTime = LocalTime.now();
        return timeRange.startTime().isAfter(currentDateTime);
    }

    public boolean isEndTimeValid(TimeRange timeRange) {
        return timeRange.endTime().isAfter(timeRange.startTime());
    }


    private boolean correctTimeStamp(TimeRange timeRange) {
        return timeRange.startTime().getSecond() == 0 && timeRange.endTime().getSecond() == 0 &&
                (timeRange.startTime().getMinute() == 0 || timeRange.startTime().getMinute() % 15 == 0) &&
                (timeRange.endTime().getMinute() == 0 || timeRange.endTime().getMinute() % 15 == 0);
    }
}
