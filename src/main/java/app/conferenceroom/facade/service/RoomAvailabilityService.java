package app.conferenceroom.facade.service;

import app.conferenceroom.db.entity.Booking;
import app.conferenceroom.db.entity.Room;
import app.conferenceroom.db.repository.BookingRepository;
import app.conferenceroom.db.repository.RoomRepository;
import app.conferenceroom.facade.dto.BookingDto;
import app.conferenceroom.facade.dto.MeetingTimeRange;
import app.conferenceroom.facade.dto.RoomAvailabilityDto;
import app.conferenceroom.facade.enums.ErrorCode;
import app.conferenceroom.infra.exception.ConferenceRoomException;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
@Slf4j
public class RoomAvailabilityService {

    private final MaintenanceService maintenanceService;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    /***
     * This function will find all available room or the best room available based on criteria.
     * @param bookingDto booking request
     * @param bestRoom when true, response will have only one result with the best room available as per the criteria
     * @return list of available rooms
     */
    public List<String> getAvailableRooms(BookingDto bookingDto, boolean bestRoom) {
        log.info("Search available booking room bookingDto: {}, bestRoom: {}", bookingDto, bestRoom);
        maintenanceService.checkForOverlaps(bookingDto.getMeetingDetails().timeRange());
        var availableRoomsByCapacity = roomRepository.findRoomByCapacity(bookingDto.getMeetingDetails().numberOfPeople());

        Predicate<Room> roomFilter = room -> isRoomAvailable(room, bookingDto.getMeetingDetails().timeRange(), bookingRepository.findAll());

        var rooms = availableRoomsByCapacity.stream()
                .filter(roomFilter)
                .sorted(Comparator.comparingInt(Room::getCapacity))
                .toList();

        rooms.stream().findFirst().ifPresent(room -> bookingDto.setRoomId(room.getRoomId()));

        var availableRooms = bestRoom ?
                List.of(rooms.stream().map(Room::getName).findFirst().orElse("")) :
                rooms.stream().map(Room::getName).toList();
        log.info("Available Rooms: {}", availableRooms);

        return availableRooms;
    }

    private String checkRoomAvailableForBooking(BookingDto newBooking) {
        log.info("Check Room available for booking: {}", newBooking);
        maintenanceService.checkForOverlaps(newBooking.getMeetingDetails().timeRange());

        Predicate<Room> hasSufficientCapacity =
                room -> room.getCapacity() >= newBooking.getMeetingDetails().numberOfPeople();

        var room = roomRepository.findById(newBooking.getRoomId())
                .filter(hasSufficientCapacity)
                .orElseThrow(() -> new ConferenceRoomException(ErrorCode.NO_SUCH_ROOM));
        log.info("Available room with capacity: {}", room);

        Predicate<Room> isRoomAvailable = availableRoom -> isRoomAvailable(availableRoom,
                newBooking.getMeetingDetails().timeRange(), bookingRepository.findAll());

        Optional.of(room).filter(isRoomAvailable)
                .orElseThrow(() -> new ConferenceRoomException(ErrorCode.NO_ROOM_AVAILABLE));
        log.info("Available room for booking: {}", room);

        return room.getName();
    }

    public String confirmBooking(BookingDto newBooking) {
        log.info("Confirm Booking: {}", newBooking);
        if(null == newBooking.getRoomId()) {
            var availableRooms = getAvailableRooms(newBooking, true);
            return availableRooms.stream().filter(StringUtils::isNotBlank).findFirst()
                    .orElseThrow(() -> new ConferenceRoomException(ErrorCode.NO_ROOM_AVAILABLE));
        } else {
            return checkRoomAvailableForBooking(newBooking);
        }
    }

    private boolean isRoomAvailable(Room room, MeetingTimeRange timeRange, List<Booking> bookings) {
        log.info("isRoomAvailable, room: {}, timeRange: {}, bookings: {}", room.getName(), timeRange, bookings);
        return bookings.stream()
                .noneMatch(booking ->
                        booking.getRoomId().equals(room.getRoomId()) &&
                                booking.getStartTime().isBefore(timeRange.endTime()) &&
                                booking.getEndTime().isAfter(timeRange.startTime()));
    }

    public List<BookingDto> getAllBookings(MeetingTimeRange timeRange) {
        log.info("getAllBookings, timeRange: {}", timeRange);
        var bookings = bookingRepository.findAllBookings(timeRange.startTime(), timeRange.endTime());
        log.info("bookings: {}", bookings);
        if (bookings.isEmpty()) {
            throw new ConferenceRoomException(ErrorCode.NO_BOOKING_FOUND);
        }

        return bookings.stream()
                .map(booking -> new BookingDto(booking.getRoomId(),
                        new RoomAvailabilityDto(new MeetingTimeRange(booking.getStartTime(), booking.getEndTime()),
                                booking.getNumberOfPeople()))).toList();
    }

}