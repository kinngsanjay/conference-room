package app.conferenceroom.service.impl;

import app.conferenceroom.db.entity.Booking;
import app.conferenceroom.db.entity.Room;
import app.conferenceroom.db.repository.BookingRepository;
import app.conferenceroom.service.BookingService;
import app.conferenceroom.service.exception.BookingNotFoundException;
import app.conferenceroom.service.exception.RoomNotAvailableException;
import app.conferenceroom.service.exception.RoomNotBookedException;
import app.conferenceroom.service.model.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    private RoomAvailabilityService roomAvailabilityService;

    @Override
    @Transactional
    public BookingCreationResult execute(CreateBookingCommand bookingCommand) throws RoomNotAvailableException {
        log.info("bookRoom started: {}", bookingCommand);
        var availableRooms = roomAvailabilityService.searchAvailableRooms(
                bookingCommand.meetingTime(), bookingCommand.numberOfAttendees());
        log.info("Available Rooms: {}", availableRooms);
        if(availableRooms.isEmpty()) {
            throw new RoomNotAvailableException("Room %s is not available at time %s".formatted(
                    bookingCommand.roomName(), bookingCommand.meetingTime()
            ));
        }

        RoomModel selectedRoom = Optional.ofNullable(bookingCommand.roomName())
                .filter(StringUtils::hasText)
                .map(roomName -> availableRooms.stream()
                        .filter(room -> room.name().equalsIgnoreCase(roomName))
                        .findFirst()
                        .orElseThrow(() -> new RoomNotBookedException("Room not booked"))
                )
                .orElseGet(() -> availableRooms.get(0));

        var bookingReference = "BR-"+System.currentTimeMillis();
        var booking = mapToBookingEntity(bookingReference, bookingCommand, selectedRoom);
        bookingRepository.save(booking);
        log.info("Conference Room Booked. Booking Reference: {}", booking.getBookingReference());
        return new BookingCreationResult(bookingReference);
    }

    @Override
    public SearchBookingsResult execute(SearchBookingsCommand command) throws BookingNotFoundException {
        log.info("getBookingsByTime, timeRange: {}", command.timeRange());
        return new SearchBookingsResult(roomAvailabilityService.getAllBookings(command.timeRange()));
    }

    @Override
    public BookingCancellationResult execute(CancelBookingCommand command) throws BookingNotFoundException {
        log.info("Cancelling room started for bookingReference: {}", command.reference());
        var booking = bookingRepository.findByBookingReference(command.reference())
                .orElseThrow(() -> new BookingNotFoundException("No Booking found for the reference %s"
                        .formatted(command.reference())));;
        bookingRepository.deleteById(booking.getBookingId());
        return new BookingCancellationResult(true);
    }

    private Booking mapToBookingEntity(String bookingReference,
                                       CreateBookingCommand bookingCommand,
                                       RoomModel roomModel) {
        var booking = new Booking();
        var room = new Room();
        room.setRoomId(roomModel.roomId());
        booking.setRoom(room);
        booking.setBookingReference(bookingReference);
        booking.setStartTime(bookingCommand.meetingTime().startTime());
        booking.setEndTime(bookingCommand.meetingTime().endTime());
        booking.setNumberOfPeople(bookingCommand.numberOfAttendees());
        booking.setMeetingDate(LocalDate.now());
        return booking;
    }
}
