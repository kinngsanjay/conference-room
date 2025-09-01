package app.conferenceroom.domain.service;

import app.conferenceroom.service.BookingService;
import app.conferenceroom.service.exception.BookingNotFoundException;
import app.conferenceroom.service.exception.RoomNotAvailableException;
import app.conferenceroom.service.model.CancelBookingCommand;
import app.conferenceroom.service.model.CreateBookingCommand;
import app.conferenceroom.service.model.SearchBookingsCommand;
import app.conferenceroom.service.model.TimeRange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingServiceTest {
    @Autowired
    BookingService bookingService;

    private TimeRange getMeetingTimeRange(LocalTime startTime) {
        return new TimeRange(startTime, startTime.plusMinutes(15));
    }

    private CreateBookingCommand getBooking(LocalTime startTime) {
        return new CreateBookingCommand("Strive", getMeetingTimeRange(startTime), 12);
    }

    @Test
    public void testBookRoom() throws RoomNotAvailableException {
        LocalTime time = LocalTime.of(14, 0, 0);
        var bookingModel = getBooking(time);
        assertNotNull(bookingService.execute(bookingModel));
    }

    @Test
    public void testBookRoomWithNoAvailability() {
        LocalTime time = LocalTime.of(14, 0, 0);
        var bookingModel = new CreateBookingCommand( "Strive", getMeetingTimeRange(time), 15);
        RoomNotAvailableException exception = assertThrows(RoomNotAvailableException.class, () -> {
            bookingService.execute(bookingModel);
        });
    }

    @Test
    public void testGetAllBookings() throws RoomNotAvailableException, BookingNotFoundException {
        LocalTime time = LocalTime.of(14, 15, 0);
        bookingService.execute(getBooking(time));
        assertFalse(bookingService.execute(
                new SearchBookingsCommand(getMeetingTimeRange(time)))
                .existingBookings()
                .isEmpty());
    }

    @Test
    public void testCancelBooking() throws RoomNotAvailableException, BookingNotFoundException {
        LocalTime time = LocalTime.of(14, 45, 0);
        var bookingModel = getBooking(time);
        var reference = bookingService.execute(bookingModel).reference();
        assertEquals(bookingService.execute(new CancelBookingCommand(reference)).status(), true);
    }

    @Test
    public void testCancelBookingWithNoBookingInTimeRange() {
        BookingNotFoundException exception = assertThrows(BookingNotFoundException.class, () -> {
            bookingService.execute(new CancelBookingCommand("2L"));
        });
    }
}
