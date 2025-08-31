package app.conferenceroom.domain.service;

import app.conferenceroom.db.entity.Booking;
import app.conferenceroom.db.entity.Room;
import app.conferenceroom.db.repository.BookingRepository;
import app.conferenceroom.api.dto.TimeRange;
import app.conferenceroom.domain.enums.ErrorCode;
import app.conferenceroom.domain.model.BookingModel;
import app.conferenceroom.domain.model.RoomModel;
import app.conferenceroom.infra.exception.ConferenceRoomException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    private RoomAvailabilityService roomAvailabilityService;

    private Booking mapToBookingEntity(BookingModel bookingModel) {
        var booking = new Booking();
        var room = new Room();
        room.setRoomId(bookingModel.getRoomModel().getRoomId());
        booking.setRoom(room);
        booking.setBookingReference(bookingModel.getBookingReference());
        booking.setStartTime(bookingModel.getMeetingModel().timeRange().startTime());
        booking.setEndTime(bookingModel.getMeetingModel().timeRange().endTime());
        booking.setNumberOfPeople(bookingModel.getMeetingModel().numberOfPeople());
        booking.setMeetingDate(LocalDate.now());
        return booking;
    }

    @Override
    @Transactional
    public void bookRoom(BookingModel bookingModel) {
        log.info("bookRoom started: {}", bookingModel);
        var availableRooms = roomAvailabilityService.getAllAvailableRooms(bookingModel);
        log.info("Available Rooms: {}", availableRooms);
        if(availableRooms.isEmpty()) {
            throw new ConferenceRoomException(ErrorCode.NO_ROOM_AVAILABLE);
        }

        RoomModel selectedRoom = Optional.ofNullable(bookingModel.getRoomModel().getName())
                .filter(StringUtils::hasText)
                .map(roomName -> availableRooms.stream()
                        .filter(room -> room.getName().equalsIgnoreCase(roomName))
                        .findFirst()
                        .orElseThrow(() -> new ConferenceRoomException(ErrorCode.UNABLE_TO_BOOK_ROOM))
                )
                .orElseGet(() -> availableRooms.get(0));

        bookingModel.setBookingReference("BR-"+System.currentTimeMillis());
        bookingModel.setRoomModel(selectedRoom);
        var booking = mapToBookingEntity(bookingModel);
        bookingRepository.save(booking);
        log.info("Conference Room Booked. Booking Reference: {}", booking.getBookingReference());
    }

    @Override
    public List<BookingModel> getBookingsByTime(TimeRange timeRange) {
        log.info("getBookingsByTime, timeRange: {}", timeRange);
        return roomAvailabilityService.getAllBookings(timeRange);
    }

    @Override
    public String cancelBooking(String bookingReference) {
        log.info("Cancelling room started for bookingReference: {}", bookingReference);
        var booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new ConferenceRoomException(ErrorCode.NO_BOOKING_FOUND));;
        bookingRepository.deleteById(booking.getBookingId());
        return "Conference Room Booking Cancelled";
    }
}
