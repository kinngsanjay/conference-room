package app.conferenceroom.facade.service;

import app.conferenceroom.facade.dto.BookingDto;
import app.conferenceroom.facade.dto.MeetingTimeRange;
import app.conferenceroom.facade.dto.RoomAvailabilityDto;
import app.conferenceroom.facade.enums.ErrorCode;
import app.conferenceroom.infra.exception.ConferenceRoomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingServiceTest {
    @Autowired
    BookingService bookingService;

    private MeetingTimeRange getMeetingTimeRange(LocalTime startTime) {
        return new MeetingTimeRange(startTime, startTime.plusMinutes(15));
    }

    private BookingDto getBookingDto(LocalTime startTime) {
        return new BookingDto(
                4L, new RoomAvailabilityDto(getMeetingTimeRange(startTime), 12));
    }

    @Test
    public void testBookRoom() {
        LocalTime time = LocalTime.of(14, 0, 0);
        assertEquals(bookingService.bookRoom(getBookingDto(time)), "Conference Room Booked. Room Name: Strive");
    }

    @Test
    public void testGetAllBookings() {
        LocalTime time = LocalTime.of(14, 15, 0);
        bookingService.bookRoom(getBookingDto(time));
        assertFalse(bookingService.getBookingsByTime(getMeetingTimeRange(time)).isEmpty());
    }

    @Test
    public void testCancelBooking() {
        LocalTime time = LocalTime.of(14, 45, 0);
        bookingService.bookRoom(getBookingDto(time));
        assertEquals(bookingService.cancelBooking(2L), "Conference Room Booking Cancelled");
    }

    @Test
    public void testCancelBookingWithNoBookingInTimeRange() {
        ConferenceRoomException exception = assertThrows(ConferenceRoomException.class, () -> {
            bookingService.cancelBooking(2L);
        });

        assertEquals(ErrorCode.NO_BOOKING_FOUND.getErrorCode(), exception.getErrorCode());
    }
}
