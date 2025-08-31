package app.conferenceroom.domain.service;

import app.conferenceroom.api.dto.TimeRange;
import app.conferenceroom.domain.model.BookingModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    void bookRoom(BookingModel bookingModel);

    List<BookingModel> getBookingsByTime(TimeRange timeRange);

    String cancelBooking(String bookingReference);
}
