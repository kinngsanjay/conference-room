package app.conferenceroom.facade.controller;

import app.conferenceroom.facade.service.BookingService;
import app.conferenceroom.facade.dto.BookingDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book")
    public String bookRoom(@RequestBody @Valid BookingDto booking) {
        if (bookingService.bookRoom(booking)) {
            return "Booking successful!";
        } else {
            return "Room not available for booking.";
        }
    }

    @GetMapping
    public List<BookingDto> getBookingsByTime(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return bookingService.getBookingsByTime(startTime, endTime);
    }
}
