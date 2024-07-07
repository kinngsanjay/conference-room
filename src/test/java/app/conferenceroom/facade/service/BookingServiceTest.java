package app.conferenceroom.facade.service;

import app.conferenceroom.facade.dto.BookingDto;
import app.conferenceroom.facade.dto.CancelDto;
import app.conferenceroom.facade.dto.MeetingTimeRange;
import app.conferenceroom.facade.dto.RoomAvailabilityDto;
import app.conferenceroom.facade.enums.ErrorCode;
import app.conferenceroom.infra.exception.ConferenceRoomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
public class BookingServiceTest {
    @Autowired
    BookingService bookingService;

    private MeetingTimeRange getMeetingTimeRange(LocalDateTime startTime) {
        return new MeetingTimeRange(startTime, startTime.plusMinutes(15));
    }

    private BookingDto getBookingDto(LocalDateTime startTime) {
        return new BookingDto(
                4L, new RoomAvailabilityDto(getMeetingTimeRange(startTime), 12));
    }

    @Test
    public void testBookRoom() {
        LocalDateTime time = LocalDateTime.now().with(LocalTime.of(14, 0, 0));
        assertEquals(bookingService.bookRoom(getBookingDto(time)), "Conference Room Booked. Room Name: Strive");
    }

    @Test
    public void testGetAllBookings() {
        LocalDateTime time = LocalDateTime.now().with(LocalTime.of(14, 15, 0));
        bookingService.bookRoom(getBookingDto(time));
        assertFalse(bookingService.getBookingsByTime(getMeetingTimeRange(time)).isEmpty());
    }

    @Test
    public void testCancelBooking() {
        LocalDateTime time = LocalDateTime.now().with(LocalTime.of(14, 45, 0));
        bookingService.bookRoom(getBookingDto(time));
        assertEquals(bookingService.cancelBooking(new CancelDto(4L, getMeetingTimeRange(time))),
                "Conference Room Booking Cancelled");
    }

    @Test
    public void testCancelBookingWithNoBookingInTimeRange() {
        ConferenceRoomException exception = assertThrows(ConferenceRoomException.class, () -> {
            bookingService.cancelBooking(new CancelDto(4L,
                    getMeetingTimeRange(LocalDateTime.now().with(LocalTime.of(21, 30, 0)))));
        });

        assertEquals(ErrorCode.NO_BOOKING_FOUND.getErrorCode(), exception.getErrorCode());
    }
}
