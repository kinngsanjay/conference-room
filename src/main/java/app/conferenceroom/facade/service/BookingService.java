package app.conferenceroom.facade.service;

import app.conferenceroom.facade.dto.BookingDto;
import app.conferenceroom.facade.dto.MeetingTimeRange;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    String bookRoom(BookingDto bookingDto);

    List<BookingDto> getBookingsByTime(MeetingTimeRange timeRange);

    String cancelBooking(Long bookingId);
}
