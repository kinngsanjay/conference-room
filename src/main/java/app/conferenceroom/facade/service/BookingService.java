package app.conferenceroom.facade.service;

import app.conferenceroom.db.entity.Booking;
import app.conferenceroom.db.repository.BookingRepository;
import app.conferenceroom.facade.dto.BookingDto;
import app.conferenceroom.facade.dto.MeetingTimeRange;
import app.conferenceroom.facade.enums.ErrorCode;
import app.conferenceroom.infra.exception.ConferenceRoomException;
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
        if(!roomAvailabilityService.isRoomAvailableForBooking(bookingDto)) {
            throw new ConferenceRoomException(ErrorCode.NO_ROOM_AVAILABLE);
        }
        Booking booking = new Booking();
        BeanUtils.copyProperties(bookingDto, booking);
        BeanUtils.copyProperties(bookingDto.timeRange(), booking);
        bookingRepository.save(booking);
    }

    public List<BookingDto> getBookingsByTime(MeetingTimeRange timeRange) {
        return roomAvailabilityService.getAllBookings(timeRange);
    }
}
