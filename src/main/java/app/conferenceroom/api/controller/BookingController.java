package app.conferenceroom.api.controller;

import app.conferenceroom.api.dto.*;
import app.conferenceroom.api.mapper.BookingMapper;
import app.conferenceroom.service.exception.BookingNotFoundException;
import app.conferenceroom.service.exception.RoomNotAvailableException;
import app.conferenceroom.service.model.CancelBookingCommand;
import app.conferenceroom.service.model.SearchBookingsCommand;
import app.conferenceroom.service.model.TimeRange;
import app.conferenceroom.service.BookingService;
import app.conferenceroom.api.infra.response.Response;
import app.conferenceroom.validator.meetingrange.ValidTimeRange;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static app.conferenceroom.api.mapper.BookingMapper.toBookingMetadata;
import static app.conferenceroom.api.infra.response.ResponseStatus.*;

@RestController
@RequestMapping("/api/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Response<BookingReferenceDTO>> createBooking(@RequestBody @Valid BookingRequestDTO booking)
            throws RoomNotAvailableException {
        log.info("BookingController - bookRoom - STARTED");
        var roomBookingModel = toBookingMetadata(booking);
        var bookingReference = new BookingReferenceDTO(bookingService.execute(roomBookingModel).reference());
        Response<BookingReferenceDTO> response = Response.<BookingReferenceDTO>builder()
                .status(SUCCESS)
                .data(bookingReference).build();
        log.info("BookingController - bookRoom - ENDED");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public List<ExistingBookingDTO> search(@RequestBody @ValidTimeRange TimeRangeDTO timeRangeDTO)
            throws BookingNotFoundException {
        log.info("BookingController - getBookingsByTime: {}", timeRangeDTO);
        return bookingService.execute(
                        new SearchBookingsCommand(new TimeRange(
                                timeRangeDTO.startTime(), timeRangeDTO.endTime()))
                )
                .existingBookings()
                .stream().map(BookingMapper::toResponseDto).toList();
    }

    @DeleteMapping("/{bookingReference}")
    public ResponseEntity<Response<BookingCancellationDTO>> cancelBooking(@PathVariable String bookingReference)
            throws BookingNotFoundException {
        log.info("BookingController - cancelBooking - STARTED");
        Response<BookingCancellationDTO> response = Response.<BookingCancellationDTO>builder()
                .status(SUCCESS)
                .data(new BookingCancellationDTO(bookingService.execute(
                        new CancelBookingCommand(bookingReference)).status())).build();
        log.info("BookingController - cancelBooking - ENDED");
        return ResponseEntity.ok(response);
    }

}
