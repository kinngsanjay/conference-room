package app.conferenceroom.api.controller;

import app.conferenceroom.api.dto.BookingResponseDto;
import app.conferenceroom.api.dto.MeetingTimeRange;
import app.conferenceroom.api.mapper.BookingRequestToModel;
import app.conferenceroom.api.mapper.BookingResponseDtoMapper;
import app.conferenceroom.domain.service.BookingService;
import app.conferenceroom.api.dto.BookingRequestDto;
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
    @Autowired
    private BookingResponseDtoMapper bookingResponseDtoMapper;
    @Autowired
    private BookingRequestToModel bookingRequestToModel;

    @PostMapping
    public ResponseEntity<Response<BookingResponseDto>> bookingRoom(@RequestBody @Valid BookingRequestDto booking) {
        log.info("BookingController - bookRoom - STARTED");
        var roomBookingModel = bookingRequestToModel.apply(booking);
        bookingService.bookRoom(roomBookingModel);
        var bookingResponse = bookingResponseDtoMapper.apply(roomBookingModel);
        Response<BookingResponseDto> response = Response.<BookingResponseDto>builder()
                .status(ResponseStatus.SUCCESS)
                .data(bookingResponse).build();
        log.info("BookingController - bookRoom - ENDED");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public List<BookingResponseDto> getBookingsByTime(@RequestBody @Valid MeetingTimeRange timeRange) {
        log.info("BookingController - getBookingsByTime: {}", timeRange);
        return bookingService.getBookingsByTime(timeRange)
                .stream().map(bookingResponseDtoMapper).toList();
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
