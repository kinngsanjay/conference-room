package app.conferenceroom.api.controller;

import app.conferenceroom.api.dto.*;
import app.conferenceroom.service.exception.BookingNotFoundException;
import app.conferenceroom.service.exception.RoomNotAvailableException;
import app.conferenceroom.api.infra.response.Response;
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

    private TimeRangeDTO getMeetingTimeRange(LocalTime startTime) {
        return new TimeRangeDTO(startTime, startTime.plusMinutes(15));
    }

    private BookingRequestDTO getBookingDto(LocalTime startTime) {
        return new BookingRequestDTO(
                "Inspire", getMeetingTimeRange(startTime), 5);
    }

    @Test
    public void testCreateBooking() throws RoomNotAvailableException {
        LocalTime time = LocalTime.of(21, 0, 0);
        ResponseEntity<Response<BookingReferenceDTO>> response = bookingController.createBooking(getBookingDto(time));
        Assertions.assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetBookingByTime() throws BookingNotFoundException {
        LocalTime time = LocalTime.of(21, 0, 0);
        List<ExistingBookingDTO> bookings = bookingController.search(getMeetingTimeRange(time));
        Assertions.assertNotNull(bookings);
    }

    @Test
    public void testCancelBooking() throws RoomNotAvailableException, BookingNotFoundException {
        LocalTime time = LocalTime.of(15, 0, 0);
        BookingRequestDTO bookingRequestDto = new BookingRequestDTO(
                "Inspire", getMeetingTimeRange(time), 5);
        var bookingResponse = bookingController.createBooking(bookingRequestDto);
        ResponseEntity<Response<BookingCancellationDTO>> response = bookingController.cancelBooking(
                bookingResponse.getBody().getData().bookingReference());
        Assertions.assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
