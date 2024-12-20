package app.conferenceroom.facade.controller;

import app.conferenceroom.facade.dto.MeetingTimeRange;
import app.conferenceroom.facade.service.BookingService;
import app.conferenceroom.facade.dto.BookingDto;
import app.conferenceroom.infra.response.Response;
import app.conferenceroom.infra.response.ResponseStatus;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conference-bookings")
@Slf4j
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Response<String>> bookRoom(@RequestBody @Valid BookingDto booking) {
        log.info("BookingController - bookRoom - STARTED");
        Response<String> response = Response.<String>builder()
                .status(ResponseStatus.SUCCESS)
                .data(bookingService.bookRoom(booking)).build();
        log.info("BookingController - bookRoom - ENDED");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public List<BookingDto> getBookingsByTime(@RequestBody @Valid MeetingTimeRange timeRange) {
        log.info("BookingController - getBookingsByTime: {}", timeRange);
        return bookingService.getBookingsByTime(timeRange);
    }

    @DeleteMapping("/cancel/{bookingId}")
    public ResponseEntity<Response<String>> cancelBooking(@PathVariable Long bookingId) {
        log.info("BookingController - cancelBooking - STARTED");
        Response<String> response = Response.<String>builder()
                .status(ResponseStatus.SUCCESS)
                .data(bookingService.cancelBooking(bookingId)).build();
        log.info("BookingController - cancelBooking - ENDED");
        return ResponseEntity.ok(response);
    }
}
