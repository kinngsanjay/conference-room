package app.conferenceroom.domain.validator.meetingrange;

import app.conferenceroom.api.dto.TimeRangeDTO;
import app.conferenceroom.validator.meetingrange.TimeRangeValidator;
import app.conferenceroom.validator.meetingrange.ValidTimeRange;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
public class TimeRangeValidatorTest {

    private Validator validator;

    private TimeRangeDTO getMeetingTimeRange(LocalTime startTime) {
        return new TimeRangeDTO(startTime, startTime.plusMinutes(15));
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
        assertEquals("Time Range should be provided", violations.iterator().next().getMessage());
    }

    @Test
    public void testIsValidWithNullStartOrEndTime() {
        LocalTime startTime = null;
        LocalTime endTime = LocalTime.of(22, 0, 0);
        TimeRangeDTO timeRangeDTO = new TimeRangeDTO(startTime, endTime);

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(timeRangeDTO));
        assertFalse(violations.isEmpty());
        assertEquals("Start Time or End Time should be provided", violations.iterator().next().getMessage());

        startTime = LocalTime.of(21, 0, 15);
        endTime = null;
        timeRangeDTO = new TimeRangeDTO(startTime, endTime);

        violations = validator.validate(new MeetingTimeRangeWrapper(timeRangeDTO));
        assertFalse(violations.isEmpty());
        assertEquals("Start Time or End Time should be provided", violations.iterator().next().getMessage());
    }

    @Test
    public void testIsValidWithTimeNotSuitable() {
        LocalTime mockNow = LocalTime.of(20, 0);

        try (MockedStatic<LocalTime> mockedLocalTime = mockStatic(LocalTime.class, CALLS_REAL_METHODS)) {

            mockedLocalTime.when(LocalTime::now).thenReturn(mockNow);
            LocalTime startTime = LocalTime.of(21, 0, 15);
            LocalTime endTime = LocalTime.of(22, 0, 0);
            TimeRangeDTO timeRangeDTO = new TimeRangeDTO(startTime, endTime);

            Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(timeRangeDTO));
            assertFalse(violations.isEmpty());
            assertEquals("Entered Time is not suitable input", violations.iterator().next().getMessage());

            startTime = LocalTime.of(21, 15, 0);
            endTime = LocalTime.of(22, 15, 15);
            timeRangeDTO = new TimeRangeDTO(startTime, endTime);

            violations = validator.validate(new MeetingTimeRangeWrapper(timeRangeDTO));
            assertFalse(violations.isEmpty());
            assertEquals("Entered Time is not suitable input", violations.iterator().next().getMessage());

            startTime = LocalTime.of(21, 16, 0);
            endTime = LocalTime.of(22, 15, 0);
            timeRangeDTO = new TimeRangeDTO(startTime, endTime);

            violations = validator.validate(new MeetingTimeRangeWrapper(timeRangeDTO));
            assertFalse(violations.isEmpty());
            assertEquals("Entered Time is not suitable input", violations.iterator().next().getMessage());

            startTime = LocalTime.of(21, 15, 0);
            endTime = LocalTime.of(22, 16, 0);
            timeRangeDTO = new TimeRangeDTO(startTime, endTime);

            violations = validator.validate(new MeetingTimeRangeWrapper(timeRangeDTO));
            assertFalse(violations.isEmpty());
            assertEquals("Entered Time is not suitable input", violations.iterator().next().getMessage());
        }
    }

    @Test
    public void testIsValidWithValidMeetingTimeRange() {
        LocalTime now = LocalTime.now().plusMinutes(5);
        TimeRangeDTO timeRangeDTO = new TimeRangeDTO(now, now.plusMinutes(30));

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(timeRangeDTO));
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidateTimeRangeWithInvalidStartTime() {
        LocalTime startTime = LocalTime.now().minusMinutes(10);
        LocalTime endTime = startTime.plusMinutes(30);
        TimeRangeDTO timeRangeDTO = new TimeRangeDTO(startTime, endTime);

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(timeRangeDTO));
        assertFalse(violations.isEmpty());
        assertEquals("Start Time should be after current time", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidateTimeRangeWithCorrectTime() {
        LocalTime mockNow = LocalTime.of(20, 0);

        try (MockedStatic<LocalTime> mockedLocalTime = mockStatic(LocalTime.class, CALLS_REAL_METHODS)) {

            mockedLocalTime.when(LocalTime::now).thenReturn(mockNow);
            LocalTime startTime = LocalTime.of(21, 0, 0);
            LocalTime endTime = LocalTime.of(22, 0, 0);
            TimeRangeDTO timeRangeDTO = new TimeRangeDTO(startTime, endTime);

            Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(
                    new MeetingTimeRangeWrapper(timeRangeDTO));
            assertTrue(violations.isEmpty());

            startTime = LocalTime.of(21, 15, 0);
            endTime = LocalTime.of(22, 15, 0);
            timeRangeDTO = new TimeRangeDTO(startTime, endTime);

            violations = validator.validate(new MeetingTimeRangeWrapper(timeRangeDTO));
            assertTrue(violations.isEmpty());

        }
    }

    @Test
    public void testValidateTimeRangeWithInvalidEndTime() {
        LocalTime startTime = LocalTime.now().plusMinutes(10);
        LocalTime endTime = startTime.minusMinutes(5);
        TimeRangeDTO timeRangeDTO = new TimeRangeDTO(startTime, endTime);

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(timeRangeDTO));
        assertFalse(violations.isEmpty());
        assertEquals("End Time cannot be earlier than Start time", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidateTimeRangeWithInvalidTimestamp() {
        LocalTime startTime = LocalTime.now().withMinute(14).withSecond(30);
        LocalTime endTime = startTime.plusMinutes(45);
        TimeRangeDTO timeRangeDTO = new TimeRangeDTO(startTime.minusHours(1), endTime);

        Set<ConstraintViolation<MeetingTimeRangeWrapper>> violations = validator.validate(new MeetingTimeRangeWrapper(timeRangeDTO));
        assertFalse(violations.isEmpty());
        assertEquals("Start Time should be after current time", violations.iterator().next().getMessage());
    }
    @Test
    public void testIsEndTimeValid() {
        LocalTime startTime = LocalTime.now().plusMinutes(10);
        TimeRangeDTO timeRangeDTO = new TimeRangeDTO(startTime, startTime.plusMinutes(30));

        boolean result = new TimeRangeValidator().isEndTimeValid(timeRangeDTO);
        assertTrue(result);

        timeRangeDTO = new TimeRangeDTO(startTime, startTime.minusMinutes(5));
        result = new TimeRangeValidator().isEndTimeValid(timeRangeDTO);
        assertFalse(result);
    }

    private static class MeetingTimeRangeWrapper {
        @ValidTimeRange
        private final TimeRangeDTO timeRangeDTO;

        public MeetingTimeRangeWrapper(TimeRangeDTO timeRangeDTO) {
            this.timeRangeDTO = timeRangeDTO;
        }

        public TimeRangeDTO getMeetingTimeRange() {
            return timeRangeDTO;
        }
    }
}
