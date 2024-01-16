package app.conferenceroom.facade.validator.meetingrange;

import app.conferenceroom.facade.dto.MeetingTimeRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MeetingTimeRangeValidator implements ConstraintValidator<ValidMeetingTimeRange, MeetingTimeRange> {

    @Override
    public void initialize(ValidMeetingTimeRange constraintAnnotation) {
    }

    private void addErrorMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
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
        if(!isTodayDate(meetingTimeRange)) {
            addErrorMessage(context, "Start and End Date should be of Today's date");
            return false;
        }
        if(!isStartTimeValid(meetingTimeRange)) {
            addErrorMessage(context, "Start Time should be after current time");
            return false;
        }
        if(!isEndTimeValid(meetingTimeRange)) {
            addErrorMessage(context, "End Time cannot be earlier than Start time");
            return false;
        }
        if(!correctTimeStamp(meetingTimeRange)) {
            addErrorMessage(context, "Entered Time is not suitable input");
            return false;
        }
        return true;
    }

    private boolean isStartTimeValid(MeetingTimeRange meetingTimeRange) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return meetingTimeRange.getStartTime().isAfter(currentDateTime);
    }

    public boolean isEndTimeValid(MeetingTimeRange meetingTimeRange) {
        return meetingTimeRange.getEndTime().isAfter(meetingTimeRange.getStartTime());
    }

    private boolean isTodayDate(MeetingTimeRange meetingTimeRange) {
        LocalDate today = LocalDate.now();
        return meetingTimeRange.getStartTime().toLocalDate().isEqual(today) &&
                meetingTimeRange.getEndTime().toLocalDate().isEqual(today);
    }

    private boolean correctTimeStamp(MeetingTimeRange meetingTimeRange) {
        return meetingTimeRange.getStartTime().getSecond() == 0 && meetingTimeRange.getEndTime().getSecond() == 0 &&
                (meetingTimeRange.getStartTime().getMinute() == 0 || meetingTimeRange.getStartTime().getMinute() % 15 == 0) &&
                (meetingTimeRange.getEndTime().getMinute() == 0 || meetingTimeRange.getEndTime().getMinute() % 15 == 0);
    }
}
