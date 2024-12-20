package app.conferenceroom.facade.validator.meetingrange;

import app.conferenceroom.facade.dto.MeetingTimeRange;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MeetingTimeRangeValidatorTest {

    private Validator validator;

    private MeetingTimeRange getMeetingTimeRange(LocalTime startTime) {
        return new MeetingTimeRange(startTime, startTime.plusMinutes(15));
    }
    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testIsValidWithNullMeetingTimeRange() {
        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(null));
        assertFalse(violations.isEmpty());
        assertEquals("Time Range cannot be null", violations.iterator().next().getMessage());
    }

    @Test
    public void testIsValidWithTimeNotSuitable() {
        LocalTime startTime = LocalTime.of(21, 0, 15);
        LocalTime endTime = LocalTime.of(22, 0, 0);
        MeetingTimeRange meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
        assertEquals("Entered Time is not suitable input", violations.iterator().next().getMessage());

        startTime = LocalTime.of(21, 15, 0);
        endTime = LocalTime.of(22, 15, 15);
        meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
        assertEquals("Entered Time is not suitable input", violations.iterator().next().getMessage());

        startTime = LocalTime.of(21, 16, 0);
        endTime = LocalTime.of(22, 15, 0);
        meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
        assertEquals("Entered Time is not suitable input", violations.iterator().next().getMessage());

        startTime = LocalTime.of(21, 15, 0);
        endTime = LocalTime.of(22, 16, 0);
        meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
        assertEquals("Entered Time is not suitable input", violations.iterator().next().getMessage());
    }

    @Test
    public void testIsValidWithValidMeetingTimeRange() {
        LocalTime now = LocalTime.now().plusMinutes(5);
        MeetingTimeRange meetingTimeRange = new MeetingTimeRange(now, now.plusMinutes(30));

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidateTimeRangeWithInvalidStartTime() {
        LocalTime startTime = LocalTime.now().minusMinutes(10);
        LocalTime endTime = startTime.plusMinutes(30);
        MeetingTimeRange meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
        assertEquals("Start Time should be after current time", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidateTimeRangeWithCorrectTime() {
        LocalTime startTime = LocalTime.of(21, 0, 0);
        LocalTime endTime = LocalTime.of(22, 0, 0);
        MeetingTimeRange meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertTrue(violations.isEmpty());

        startTime = LocalTime.of(21, 15, 0);
        endTime = LocalTime.of(22, 15, 0);
        meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidateTimeRangeWithInvalidEndTime() {
        LocalTime startTime = LocalTime.now().plusMinutes(10);
        LocalTime endTime = startTime.minusMinutes(5);
        MeetingTimeRange meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
        assertEquals("End Time cannot be earlier than Start time", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidateTimeRangeWithInvalidTimestamp() {
        LocalTime startTime = LocalTime.now().withMinute(14).withSecond(30);
        LocalTime endTime = startTime.plusMinutes(45);
        MeetingTimeRange meetingTimeRange = new MeetingTimeRange(startTime.minusHours(1), endTime);

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
        assertEquals("Start Time should be after current time", violations.iterator().next().getMessage());
    }
    @Test
    public void testIsEndTimeValid() {
        LocalTime startTime = LocalTime.now().plusMinutes(10);
        MeetingTimeRange meetingTimeRange = new MeetingTimeRange(startTime, startTime.plusMinutes(30));

        boolean result = new MeetingTimeRangeValidator().isEndTimeValid(meetingTimeRange);
        assertTrue(result);

        meetingTimeRange = new MeetingTimeRange(startTime, startTime.minusMinutes(5));
        result = new MeetingTimeRangeValidator().isEndTimeValid(meetingTimeRange);
        assertFalse(result);
    }

    private static class MeetingTimeRangeWrapper {
        @ValidMeetingTimeRange
        private final MeetingTimeRange meetingTimeRange;

        public MeetingTimeRangeWrapper(MeetingTimeRange meetingTimeRange) {
            this.meetingTimeRange = meetingTimeRange;
        }

        public MeetingTimeRange getMeetingTimeRange() {
            return meetingTimeRange;
        }
    }
}
