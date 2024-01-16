package app.conferenceroom.facade.service;

import app.conferenceroom.db.entity.Booking;
import app.conferenceroom.db.entity.Room;
import app.conferenceroom.db.repository.BookingRepository;
import app.conferenceroom.db.repository.RoomRepository;
import app.conferenceroom.facade.dto.BookingDto;
import app.conferenceroom.facade.dto.MeetingTimeRange;
import app.conferenceroom.facade.dto.RoomAvailabilityDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RoomAvailabilityService {

    private MaintenanceService maintenanceService;
    private RoomRepository roomRepository;
    private BookingRepository bookingRepository;

    public List<String> getAvailableRooms(RoomAvailabilityDto availabilityDto) {
        List<String> availableRooms = new ArrayList<>();
        if(!maintenanceService.overlaps(availabilityDto.getTimeRange())) {
            List<Room> availableRoomsByCapacity = roomRepository.findRoomByCapacity(availabilityDto.getNumberOfPeople());
            List<Booking> bookings = bookingRepository.findAll();

            for (Room room : availableRoomsByCapacity) {
                if (isRoomAvailable(room, availabilityDto.getTimeRange(), bookings)) {
                    availableRooms.add(room.getName());
                }
            }
        }
        return availableRooms;
    }

    public boolean isRoomAvailableForBooking(BookingDto newBooking) {
        if(maintenanceService.overlaps(newBooking.getTimeRange())) {
            return false;
        }
        Room room = roomRepository.getReferenceById(newBooking.getRoomId());
        if(room.getCapacity() >= newBooking.getNumberOfPeople()) {
            return isRoomAvailable(room, newBooking.getTimeRange(), bookingRepository.findAll());
        } else
            return false;
    }

    private boolean isRoomAvailable(Room room, MeetingTimeRange timeRange, List<Booking> bookings) {
        return
                bookings.stream()
                        .noneMatch(booking ->
                                        booking.getRoomId().equals(room.getRoomId()) &&
                                        booking.getStartTime().isBefore(timeRange.getEndTime()) &&
                                        booking.getEndTime().isAfter(timeRange.getStartTime()));
    }
}