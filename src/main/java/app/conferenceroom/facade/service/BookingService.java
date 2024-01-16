package app.conferenceroom.facade.service;

import app.conferenceroom.db.entity.Booking;
import app.conferenceroom.db.repository.BookingRepository;
import app.conferenceroom.facade.dto.BookingDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingService {

    private BookingRepository bookingRepository;
    private RoomAvailabilityService roomAvailabilityService;

    @Transactional
    public boolean bookRoom(BookingDto bookingDto) {
        if (roomAvailabilityService.isRoomAvailableForBooking(bookingDto)) {
            Booking booking = new Booking();
            BeanUtils.copyProperties(bookingDto, booking);
            BeanUtils.copyProperties(bookingDto.getTimeRange(), booking);
            bookingRepository.save(booking);
            return true;
        }
        return false;
    }

    public List<BookingDto> getBookingsByTime(LocalDateTime startTime, LocalDateTime endTime) {
        List<BookingDto> result = new ArrayList<>();
        // Implement logic to retrieve bookings within the specified time range
        return result;
    }
}
