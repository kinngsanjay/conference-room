package app.conferenceroom.facade.service;

import app.conferenceroom.db.entity.Booking;
import app.conferenceroom.db.repository.BookingRepository;
import app.conferenceroom.facade.dto.BookingDto;
import app.conferenceroom.facade.dto.MeetingTimeRange;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookingService {

    private BookingRepository bookingRepository;
    private RoomAvailabilityService roomAvailabilityService;

    @Transactional
    public void bookRoom(BookingDto bookingDto) {
        roomAvailabilityService.isRoomAvailableForBooking(bookingDto);
        Booking booking = new Booking();
        BeanUtils.copyProperties(bookingDto, booking);
        BeanUtils.copyProperties(bookingDto.getTimeRange(), booking);
        bookingRepository.save(booking);
    }

    public List<BookingDto> getBookingsByTime(MeetingTimeRange timeRange) {
        List<BookingDto> result = roomAvailabilityService.getAllBookings(timeRange);
        return result;
    }
}
