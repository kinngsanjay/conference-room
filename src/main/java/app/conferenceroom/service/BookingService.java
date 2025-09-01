package app.conferenceroom.service;

import app.conferenceroom.service.exception.BookingNotFoundException;
import app.conferenceroom.service.exception.RoomNotAvailableException;
import app.conferenceroom.service.model.*;
import org.springframework.stereotype.Service;

@Service
public interface BookingService {
    BookingCreationResult execute(CreateBookingCommand command) throws RoomNotAvailableException;

    SearchBookingsResult execute(SearchBookingsCommand command) throws BookingNotFoundException;

    BookingCancellationResult execute(CancelBookingCommand commad) throws BookingNotFoundException;
}
