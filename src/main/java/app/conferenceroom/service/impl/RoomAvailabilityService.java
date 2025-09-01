package app.conferenceroom.service.impl;

import app.conferenceroom.db.entity.Booking;
import app.conferenceroom.db.entity.Room;
import app.conferenceroom.db.repository.BookingRepository;
import app.conferenceroom.db.repository.RoomRepository;
import app.conferenceroom.db.mapper.RoomToModelMapper;
import app.conferenceroom.service.exception.BookingNotFoundException;
import app.conferenceroom.service.model.BookingRecord;
import app.conferenceroom.service.model.CreateBookingCommand;
import app.conferenceroom.service.model.RoomModel;
import app.conferenceroom.service.model.TimeRange;
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

    /***
     * This function will find all available room sorted with order of best room available criteria.
     * @param bookingCommand booking request
     * @return list of available rooms
     */
    public List<RoomModel> getAllAvailableRooms(CreateBookingCommand bookingCommand) {
        log.info("Search available booking room bookingCommand: {}", bookingCommand);
        maintenanceService.checkForOverlaps(bookingCommand.meetingTime());
        var availableRoomsByCapacity = roomRepository.findRoomByCapacity(bookingCommand.numberOfAttendees());

        Predicate<Room> roomFilter = room -> isRoomAvailable(room, bookingCommand.meetingTime(), bookingRepository.findAll());

        return availableRoomsByCapacity.stream()
                .filter(roomFilter)
                .sorted(Comparator.comparingInt(Room::getCapacity))
                .map(room -> RoomToModelMapper.toRoomModel(room, maintenanceService.getTimings()))
                .toList();

    }

    private boolean isRoomAvailable(Room room, TimeRange timeRange, List<Booking> bookings) {
        log.info("isRoomAvailable, room: {}, timeRangeDTO: {}, bookings: {}", room.getName(), timeRange, bookings);
        return bookings.stream()
                .noneMatch(booking ->
                        booking.getRoom().getRoomId().equals(room.getRoomId()) &&
                                booking.getStartTime().isBefore(timeRange.endTime()) &&
                                booking.getEndTime().isAfter(timeRange.startTime()));
    }

    public List<BookingRecord> getAllBookings(TimeRange timeRange) throws BookingNotFoundException {
        log.info("getAllBookings, timeRangeDTO: {}", timeRange);
        var bookings = bookingRepository.findAllBookings(timeRange.startTime(), timeRange.endTime());
        if (bookings.isEmpty()) {
            throw new BookingNotFoundException("No Booking found between time %s-%s".formatted(
                    timeRange.startTime(), timeRange.endTime()));
        }

        return bookings.stream()
                .map(booking -> new BookingRecord(
                        booking.getBookingReference(),
                        booking.getRoom().getName(),
                        new TimeRange(booking.getStartTime(), booking.getEndTime()),
                        booking.getNumberOfPeople())
                ).toList();
    }

}