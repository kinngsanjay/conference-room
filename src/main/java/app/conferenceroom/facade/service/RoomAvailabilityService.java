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
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomAvailabilityService {

    private MaintenanceService maintenanceService;
    private RoomRepository roomRepository;
    private BookingRepository bookingRepository;

    public List<String> getAvailableRooms(RoomAvailabilityDto availabilityDto) {
        List<String> availableRooms = new ArrayList<>();
        maintenanceService.checkForOverlaps(availabilityDto.getTimeRange());
        List<Room> availableRoomsByCapacity = roomRepository.findRoomByCapacity(availabilityDto.getNumberOfPeople());
        List<Booking> bookings = bookingRepository.findAll();
        for (Room room : availableRoomsByCapacity) {
            if (isRoomAvailable(room, availabilityDto.getTimeRange(), bookings)) {
                availableRooms.add(room.getName());
            }
        }
        return availableRooms;
    }

    public boolean isRoomAvailableForBooking(BookingDto newBooking) {
        maintenanceService.checkForOverlaps(newBooking.getTimeRange());
        Optional<Room> optionalRoom = roomRepository.findById(newBooking.getRoomId());
        if (optionalRoom.isPresent()) {
            try {
                if (optionalRoom.get().getCapacity() >= newBooking.getNumberOfPeople()) {
                    return isRoomAvailable(optionalRoom.get(), newBooking.getTimeRange(), bookingRepository.findAll());
                }
            } catch (Exception ex) {
                throw new ConferenceRoomException(ErrorCode.UNABLE_TO_BOOK_ROOM);
            }
        }
        throw new ConferenceRoomException(ErrorCode.NO_SUCH_ROOM);
    }

    private boolean isRoomAvailable(Room room, MeetingTimeRange timeRange, List<Booking> bookings) {
        return bookings.stream()
                .noneMatch(booking ->
                        booking.getRoomId().equals(room.getRoomId()) &&
                                booking.getStartTime().isBefore(timeRange.getEndTime()) &&
                                booking.getEndTime().isAfter(timeRange.getStartTime()));
    }

    public List<BookingDto> getAllBookings(MeetingTimeRange timeRange) {
        List<Booking> bookings = bookingRepository.findAllBookings(timeRange.getStartTime(),timeRange.getEndTime());
        if(bookings.isEmpty()) {
            throw new ConferenceRoomException(ErrorCode.NO_BOOKING_FOUND);
        }
        List<BookingDto> bookingDtos = new ArrayList<>();
        for(Booking booking : bookings) {
            BookingDto bookingDto = new BookingDto();
            MeetingTimeRange meetingTimeRange = new MeetingTimeRange();
            BeanUtils.copyProperties(booking, bookingDto);
            BeanUtils.copyProperties(booking, meetingTimeRange);
            bookingDto.setTimeRange(meetingTimeRange);
            bookingDtos.add(bookingDto);
        }
        return bookingDtos;
    }
}