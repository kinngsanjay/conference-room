package app.conferenceroom.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExistingBookingDTO(
        @JsonProperty("bookingReference")
        String bookingReference,
        @JsonProperty("meetingSummary")
        MeetingDTO meetingDTO
) {
}
