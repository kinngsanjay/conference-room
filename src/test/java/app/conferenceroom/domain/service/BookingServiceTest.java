package app.conferenceroom.domain.service;

import app.conferenceroom.api.dto.MeetingTimeRange;
import app.conferenceroom.api.dto.RoomDetailsDto;
import app.conferenceroom.domain.enums.ErrorCode;
import app.conferenceroom.domain.model.BookingModel;
import app.conferenceroom.domain.model.RoomModel;
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

    private BookingModel getBooking(LocalTime startTime) {
        return new BookingModel("",
                new RoomModel(4L, "Strive"), new RoomDetailsDto(getMeetingTimeRange(startTime), 12));
    }

    @Test
    public void testBookRoom() {
        LocalTime time = LocalTime.of(14, 0, 0);
        var bookingModel = getBooking(time);
        bookingService.bookRoom(bookingModel);
        assertNotNull(bookingModel.getBookingReference());
    }

    @Test
    public void testGetAllBookings() {
        LocalTime time = LocalTime.of(14, 15, 0);
        bookingService.bookRoom(getBooking(time));
        assertFalse(bookingService.getBookingsByTime(getMeetingTimeRange(time)).isEmpty());
    }

    @Test
    public void testCancelBooking() {
        LocalTime time = LocalTime.of(14, 45, 0);
        var bookingModel = getBooking(time);
        bookingService.bookRoom(bookingModel);
        assertEquals(bookingService.cancelBooking(bookingModel.getBookingReference()), "Conference Room Booking Cancelled");
    }

    @Test
    public void testCancelBookingWithNoBookingInTimeRange() {
        ConferenceRoomException exception = assertThrows(ConferenceRoomException.class, () -> {
            bookingService.cancelBooking("2L");
        });

        assertEquals(ErrorCode.NO_BOOKING_FOUND.getErrorCode(), exception.getErrorCode());
    }
}
