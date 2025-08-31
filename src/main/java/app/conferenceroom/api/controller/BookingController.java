package app.conferenceroom.api.controller;

import app.conferenceroom.api.dto.BookingResponseDto;
import app.conferenceroom.api.dto.TimeRange;
import app.conferenceroom.api.mapper.BookingMapper;
import app.conferenceroom.domain.service.BookingService;
import app.conferenceroom.api.dto.BookingRequestDto;
import app.conferenceroom.infra.response.Response;
import app.conferenceroom.infra.response.ResponseStatus;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conference-bookings")
@Slf4j
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    public BookingController(BookingService bookingService,
                             BookingMapper bookingMapper) {
        this.bookingService = bookingService;
        this.bookingMapper = bookingMapper;
    }

    @PostMapping
    public ResponseEntity<Response<BookingResponseDto>> bookingRoom(@RequestBody @Valid BookingRequestDto booking) {
        log.info("BookingController - bookRoom - STARTED");
        var roomBookingModel = bookingMapper.toModel(booking);
        bookingService.bookRoom(roomBookingModel);
        var bookingResponse = bookingMapper.toResponseDto(roomBookingModel);
        Response<BookingResponseDto> response = Response.<BookingResponseDto>builder()
                .status(ResponseStatus.SUCCESS)
                .data(bookingResponse).build();
        log.info("BookingController - bookRoom - ENDED");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public List<BookingResponseDto> getBookingsByTime(@RequestBody @Valid TimeRange timeRange) {
        log.info("BookingController - getBookingsByTime: {}", timeRange);
        return bookingService.getBookingsByTime(timeRange)
                .stream().map(bookingMapper::toResponseDto).toList();
    }

    @DeleteMapping("/{bookingReference}")
    public ResponseEntity<Response<String>> cancelBooking(@PathVariable String bookingReference) {
        log.info("BookingController - cancelBooking - STARTED");
        Response<String> response = Response.<String>builder()
                .status(ResponseStatus.SUCCESS)
                .data(bookingService.cancelBooking(bookingReference)).build();
        log.info("BookingController - cancelBooking - ENDED");
        return ResponseEntity.ok(response);
    }
}
