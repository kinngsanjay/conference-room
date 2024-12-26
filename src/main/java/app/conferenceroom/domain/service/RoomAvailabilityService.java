package app.conferenceroom.domain.service;

import app.conferenceroom.db.entity.Booking;
import app.conferenceroom.db.entity.Room;
import app.conferenceroom.db.repository.BookingRepository;
import app.conferenceroom.db.repository.RoomRepository;
import app.conferenceroom.api.dto.MeetingTimeRange;
import app.conferenceroom.api.dto.RoomDetailsDto;
import app.conferenceroom.domain.enums.ErrorCode;
import app.conferenceroom.domain.mapper.RoomToModelMapper;
import app.conferenceroom.domain.model.BookingModel;
import app.conferenceroom.domain.model.RoomModel;
import app.conferenceroom.infra.exception.ConferenceRoomException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
@Slf4j
public class RoomAvailabilityService {

    private final MaintenanceService maintenanceService;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final RoomToModelMapper roomToModelMapper;

    /***
     * This function will find all available room sorted with order of best room available criteria.
     * @param bookingModel booking request
     * @return list of available rooms
     */
    public List<RoomModel> getAllAvailableRooms(BookingModel bookingModel) {
        log.info("Search available booking room bookingModel: {}", bookingModel);
        maintenanceService.checkForOverlaps(bookingModel.getRoomDetailsDto().timeRange());
        var availableRoomsByCapacity = roomRepository.findRoomByCapacity(bookingModel.getRoomDetailsDto().numberOfPeople());

        Predicate<Room> roomFilter = room -> isRoomAvailable(room, bookingModel.getRoomDetailsDto().timeRange(), bookingRepository.findAll());

        return availableRoomsByCapacity.stream()
                .filter(roomFilter)
                .sorted(Comparator.comparingInt(Room::getCapacity))
                .map(roomToModelMapper)
                .toList();

    }

    private boolean isRoomAvailable(Room room, MeetingTimeRange timeRange, List<Booking> bookings) {
        log.info("isRoomAvailable, room: {}, timeRange: {}, bookings: {}", room.getName(), timeRange, bookings);
        return bookings.stream()
                .noneMatch(booking ->
                        booking.getRoom().getRoomId().equals(room.getRoomId()) &&
                                booking.getStartTime().isBefore(timeRange.endTime()) &&
                                booking.getEndTime().isAfter(timeRange.startTime()));
    }

    public List<BookingModel> getAllBookings(MeetingTimeRange timeRange) {
        log.info("getAllBookings, timeRange: {}", timeRange);
        var bookings = bookingRepository.findAllBookings(timeRange.startTime(), timeRange.endTime());
        if (bookings.isEmpty()) {
            throw new ConferenceRoomException(ErrorCode.NO_BOOKING_FOUND);
        }

        return bookings.stream()
                .map(booking -> new BookingModel(
                        booking.getBookingReference(),
                        new RoomModel(booking.getRoom().getRoomId(), booking.getRoom().getName()),
                        new RoomDetailsDto(new MeetingTimeRange(booking.getStartTime(), booking.getEndTime()),
                        booking.getNumberOfPeople())
                )).toList();
    }

}