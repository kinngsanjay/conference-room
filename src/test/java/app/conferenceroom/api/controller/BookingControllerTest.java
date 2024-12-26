package app.conferenceroom.api.controller;

import app.conferenceroom.api.dto.BookingRequestDto;
import app.conferenceroom.api.dto.BookingResponseDto;
import app.conferenceroom.api.dto.MeetingTimeRange;
import app.conferenceroom.api.dto.RoomDetailsDto;
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

    private BookingRequestDto getBookingDto(LocalTime startTime) {
        return new BookingRequestDto(
                "Inspire", new RoomDetailsDto(getMeetingTimeRange(startTime), 5));
    }

    @Test
    public void testBookingRoom() {
        LocalTime time = LocalTime.of(21, 0, 0);
        ResponseEntity<Response<BookingResponseDto>> response = bookingController.bookingRoom(getBookingDto(time));
        Assertions.assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetBookingByTime() {
        LocalTime time = LocalTime.of(21, 0, 0);
        List<BookingResponseDto> bookings = bookingController.getBookingsByTime(getMeetingTimeRange(time));
        Assertions.assertNotNull(bookings);
    }

    @Test
    public void testCancelBooking() {
        LocalTime time = LocalTime.of(15, 0, 0);
        BookingRequestDto bookingRequestDto = new BookingRequestDto(
                "Inspire", new RoomDetailsDto(getMeetingTimeRange(time), 5));
        var bookingResponse = bookingController.bookingRoom(bookingRequestDto);
        ResponseEntity<Response<String>> response = bookingController.cancelBooking(
                bookingResponse.getBody().getData().bookingReference());
        Assertions.assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
