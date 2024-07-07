package app.conferenceroom.facade.validator.meetingrange;

import app.conferenceroom.facade.dto.MeetingTimeRange;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
public class MeetingTimeRangeValidatorTest {

    private Validator validator;

    private MeetingTimeRange getMeetingTimeRange(LocalDateTime startTime) {
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
        LocalDateTime startTime = LocalDateTime.now().with(LocalTime.of(21, 0, 15));
        LocalDateTime endTime = LocalDateTime.now().with(LocalTime.of(22, 0, 0));
        MeetingTimeRange meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
        assertEquals("Entered Time is not suitable input", violations.iterator().next().getMessage());

        startTime = LocalDateTime.now().with(LocalTime.of(21, 15, 0));
        endTime = LocalDateTime.now().with(LocalTime.of(22, 15, 15));
        meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
        assertEquals("Entered Time is not suitable input", violations.iterator().next().getMessage());

        startTime = LocalDateTime.now().with(LocalTime.of(21, 16, 0));
        endTime = LocalDateTime.now().with(LocalTime.of(22, 15, 0));
        meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
        assertEquals("Entered Time is not suitable input", violations.iterator().next().getMessage());

        startTime = LocalDateTime.now().with(LocalTime.of(21, 15, 0));
        endTime = LocalDateTime.now().with(LocalTime.of(22, 16, 0));
        meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
        assertEquals("Entered Time is not suitable input", violations.iterator().next().getMessage());
    }

    @Test
    public void testIsValidWithValidMeetingTimeRange() {
        LocalDateTime now = LocalDateTime.now().plusMinutes(5);
        MeetingTimeRange meetingTimeRange = new MeetingTimeRange(now, now.plusMinutes(30));

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidateTimeRangeWithInvalidDate() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusMinutes(30);
        MeetingTimeRange meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
        assertEquals("Start and End Date should be of Today's date", violations.iterator().next().getMessage());

        startTime = LocalDateTime.now();
        endTime = startTime.plusDays(1);
        meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
        assertEquals("Start and End Date should be of Today's date", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidateTimeRangeWithInvalidStartTime() {
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(10);
        LocalDateTime endTime = startTime.plusMinutes(30);
        MeetingTimeRange meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
        assertEquals("Start Time should be after current time", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidateTimeRangeWithCorrectTime() {
        LocalDateTime startTime = LocalDateTime.now().with(LocalTime.of(21, 0, 0));
        LocalDateTime endTime = LocalDateTime.now().with(LocalTime.of(22, 0, 0));
        MeetingTimeRange meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertTrue(violations.isEmpty());

        startTime = LocalDateTime.now().with(LocalTime.of(21, 15, 0));
        endTime = LocalDateTime.now().with(LocalTime.of(22, 15, 0));
        meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidateTimeRangeWithInvalidEndTime() {
        LocalDateTime startTime = LocalDateTime.now().plusMinutes(10);
        LocalDateTime endTime = startTime.minusMinutes(5);
        MeetingTimeRange meetingTimeRange = new MeetingTimeRange(startTime, endTime);

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
        assertEquals("End Time cannot be earlier than Start time", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidateTimeRangeWithInvalidTimestamp() {
        LocalDateTime startTime = LocalDateTime.now().withMinute(14).withSecond(30);
        LocalDateTime endTime = startTime.plusMinutes(45);
        MeetingTimeRange meetingTimeRange = new MeetingTimeRange(startTime.minusHours(1), endTime);

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(meetingTimeRange));
        assertFalse(violations.isEmpty());
        assertEquals("Start Time should be after current time", violations.iterator().next().getMessage());
    }
    @Test
    public void testIsEndTimeValid() {
        LocalDateTime startTime = LocalDateTime.now().plusMinutes(10);
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
