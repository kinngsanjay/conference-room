package app.conferenceroom.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookingReferenceDTO(
        @JsonProperty("bookingReference")
        String bookingReference
) {
}
