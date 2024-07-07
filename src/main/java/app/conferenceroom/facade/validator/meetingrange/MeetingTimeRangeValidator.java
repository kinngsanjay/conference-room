package app.conferenceroom.facade.validator.meetingrange;

import app.conferenceroom.facade.dto.MeetingTimeRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
public class MeetingTimeRangeValidator implements ConstraintValidator<ValidMeetingTimeRange, MeetingTimeRange> {
    private boolean addErrorMessage(ConstraintValidatorContext context, String message) {
        log.info("MeetingTimeRangeValidator Error message: {}", message);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        return false;
    }

    @Override
    public boolean isValid(MeetingTimeRange meetingTimeRange, ConstraintValidatorContext context) {
        if (meetingTimeRange == null) {
            addErrorMessage(context, "Time Range cannot be null");
            return false;
        }
        return validateTimeRange(meetingTimeRange, context);
    }

    private boolean validateTimeRange(MeetingTimeRange meetingTimeRange, ConstraintValidatorContext context) {
        log.info("Validate time range: {}", meetingTimeRange);
        if(!isTodayDate(meetingTimeRange)) {
            return addErrorMessage(context, "Start and End Date should be of Today's date");
        }
        if(!isStartTimeValid(meetingTimeRange)) {
            return addErrorMessage(context, "Start Time should be after current time");
        }
        if(!isEndTimeValid(meetingTimeRange)) {
            return addErrorMessage(context, "End Time cannot be earlier than Start time");
        }
        if(!correctTimeStamp(meetingTimeRange)) {
            return addErrorMessage(context, "Entered Time is not suitable input");
        }
        return true;
    }

    private boolean isStartTimeValid(MeetingTimeRange meetingTimeRange) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return meetingTimeRange.startTime().isAfter(currentDateTime);
    }

    public boolean isEndTimeValid(MeetingTimeRange meetingTimeRange) {
        return meetingTimeRange.endTime().isAfter(meetingTimeRange.startTime());
    }

    private boolean isTodayDate(MeetingTimeRange meetingTimeRange) {
        LocalDate today = LocalDate.now();
        return meetingTimeRange.startTime().toLocalDate().isEqual(today) &&
                meetingTimeRange.endTime().toLocalDate().isEqual(today);
    }

    private boolean correctTimeStamp(MeetingTimeRange meetingTimeRange) {
        return meetingTimeRange.startTime().getSecond() == 0 && meetingTimeRange.endTime().getSecond() == 0 &&
                (meetingTimeRange.startTime().getMinute() == 0 || meetingTimeRange.startTime().getMinute() % 15 == 0) &&
                (meetingTimeRange.endTime().getMinute() == 0 || meetingTimeRange.endTime().getMinute() % 15 == 0);
    }
}
