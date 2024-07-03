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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomAvailabilityService {

    private final MaintenanceService maintenanceService;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public List<String> getAvailableRooms(RoomAvailabilityDto availabilityDto) {
        maintenanceService.checkForOverlaps(availabilityDto.timeRange());
        List<Room> availableRoomsByCapacity = roomRepository.findRoomByCapacity(availabilityDto.numberOfPeople());
        List<Booking> bookings = bookingRepository.findAll();
        return availableRoomsByCapacity.stream()
                .filter(room -> isRoomAvailable(room, availabilityDto.timeRange(), bookings))
                .map(Room::getName)
                .toList();
    }

    public boolean isRoomAvailableForBooking(BookingDto newBooking) {
        maintenanceService.checkForOverlaps(newBooking.timeRange());
        return roomRepository.findById(newBooking.roomId())
                .filter(room -> room.getCapacity() >= newBooking.numberOfPeople())
                .map(room -> isRoomAvailable(room, newBooking.timeRange(), bookingRepository.findAll()))
                .orElseThrow(() -> new ConferenceRoomException(ErrorCode.NO_SUCH_ROOM));
    }

    private boolean isRoomAvailable(Room room, MeetingTimeRange timeRange, List<Booking> bookings) {
        return bookings.stream()
                .noneMatch(booking ->
                        booking.getRoomId().equals(room.getRoomId()) &&
                                booking.getStartTime().isBefore(timeRange.endTime()) &&
                                booking.getEndTime().isAfter(timeRange.startTime()));
    }

    public List<BookingDto> getAllBookings(MeetingTimeRange timeRange) {
        List<Booking> bookings = bookingRepository.findAllBookings(timeRange.startTime(),timeRange.endTime());
        if(bookings.isEmpty()) {
            throw new ConferenceRoomException(ErrorCode.NO_BOOKING_FOUND);
        }
        return bookings.stream()
                .map(booking -> new BookingDto(booking.getRoomId(),
                        new MeetingTimeRange(booking.getStartTime(), booking.getEndTime()),
                        booking.getNumberOfPeople())).toList();
    }
}