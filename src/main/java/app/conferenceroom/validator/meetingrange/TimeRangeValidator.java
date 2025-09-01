package app.conferenceroom.validator.meetingrange;

import app.conferenceroom.api.dto.TimeRangeDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

@Slf4j
public class TimeRangeValidator implements ConstraintValidator<ValidTimeRange, TimeRangeDTO> {
    private boolean addErrorMessage(ConstraintValidatorContext context, String message) {
        log.info("MeetingTimeRangeValidator Error message: {}", message);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        return false;
    }

    @Override
    public boolean isValid(TimeRangeDTO timeRangeDTO, ConstraintValidatorContext context) {
        if (timeRangeDTO == null) {
            addErrorMessage(context, "Time Range cannot be null");
            return false;
        }
        if (timeRangeDTO.startTime() == null || timeRangeDTO.endTime() == null) {
            addErrorMessage(context, "Start Time or End Time cannot be null");
            return false;
        }
        return validateTimeRange(timeRangeDTO, context);
    }

    private boolean validateTimeRange(TimeRangeDTO timeRangeDTO, ConstraintValidatorContext context) {
        log.info("Validate time range: {}", timeRangeDTO);
        if(!isStartTimeValid(timeRangeDTO)) {
            return addErrorMessage(context, "Start Time should be after current time");
        }
        if(!isEndTimeValid(timeRangeDTO)) {
            return addErrorMessage(context, "End Time cannot be earlier than Start time");
        }
        if(!correctTimeStamp(timeRangeDTO)) {
            return addErrorMessage(context, "Entered Time is not suitable input");
        }
        return true;
    }

    private boolean isStartTimeValid(TimeRangeDTO timeRangeDTO) {
        LocalTime currentDateTime = LocalTime.now();
        return timeRangeDTO.startTime().isAfter(currentDateTime);
    }

    public boolean isEndTimeValid(TimeRangeDTO timeRangeDTO) {
        return timeRangeDTO.endTime().isAfter(timeRangeDTO.startTime());
    }


    private boolean correctTimeStamp(TimeRangeDTO timeRangeDTO) {
        return timeRangeDTO.startTime().getSecond() == 0 && timeRangeDTO.endTime().getSecond() == 0 &&
                (timeRangeDTO.startTime().getMinute() == 0 || timeRangeDTO.startTime().getMinute() % 15 == 0) &&
                (timeRangeDTO.endTime().getMinute() == 0 || timeRangeDTO.endTime().getMinute() % 15 == 0);
    }
}
