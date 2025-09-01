package app.conferenceroom.service.model;

import java.util.List;

public record SearchBookingsResult(
        List<BookingRecord> existingBookings
) {
}
