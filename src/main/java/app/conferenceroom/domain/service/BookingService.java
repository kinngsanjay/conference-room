package app.conferenceroom.domain.service;

import app.conferenceroom.api.dto.MeetingTimeRange;
import app.conferenceroom.domain.model.BookingModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    void bookRoom(BookingModel bookingModel);

    List<BookingModel> getBookingsByTime(MeetingTimeRange timeRange);

    String cancelBooking(String bookingReference);
}
