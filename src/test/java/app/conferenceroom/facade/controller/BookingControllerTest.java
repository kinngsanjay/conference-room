package app.conferenceroom.facade.controller;

import app.conferenceroom.facade.dto.BookingDto;
import app.conferenceroom.facade.dto.MeetingTimeRange;
import app.conferenceroom.facade.dto.RoomAvailabilityDto;
import app.conferenceroom.infra.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookingControllerTest {
    @Autowired
    BookingController bookingController;

    private MeetingTimeRange getMeetingTimeRange(LocalTime startTime) {
        return new MeetingTimeRange(startTime, startTime.plusMinutes(15));
    }

    private BookingDto getBookingDto(LocalTime startTime) {
        return new BookingDto(
                3L, new RoomAvailabilityDto(getMeetingTimeRange(startTime), 5));
    }

    @Test
    public void testBookRoom() {
        LocalTime time = LocalTime.of(21, 0, 0);
        ResponseEntity<Response<String>> response = bookingController.bookRoom(getBookingDto(time));
        Assertions.assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetBookingByTime() {
        LocalTime time = LocalTime.of(21, 0, 0);
        List<BookingDto> bookings = bookingController.getBookingsByTime(getMeetingTimeRange(time));
        Assertions.assertNotNull(bookings);
    }

    @Test
    public void testCancelBooking() {
        LocalTime time = LocalTime.of(15, 0, 0);
        BookingDto bookingDto = new BookingDto(
                3L, new RoomAvailabilityDto(getMeetingTimeRange(time), 5));
        bookingController.bookRoom(bookingDto);

        ResponseEntity<Response<String>> response = bookingController.cancelBooking(3L);
        Assertions.assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
