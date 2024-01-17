package app.conferenceroom.facade.controller;

import app.conferenceroom.facade.dto.MeetingTimeRange;
import app.conferenceroom.facade.dto.RoomAvailabilityResDto;
import app.conferenceroom.facade.service.BookingService;
import app.conferenceroom.facade.dto.BookingDto;
import app.conferenceroom.infra.response.Response;
import app.conferenceroom.infra.response.ResponseStatus;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/conference-bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<Response<String>> bookRoom(@RequestBody @Valid BookingDto booking) {
        bookingService.bookRoom(booking);
        Response<String> response = Response.<String>builder()
                .status(ResponseStatus.SUCCESS)
                .data("Conference Room Booked").build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public List<BookingDto> getBookingsByTime(@RequestBody @Valid MeetingTimeRange timeRange) {
        return bookingService.getBookingsByTime(timeRange);
    }
}
