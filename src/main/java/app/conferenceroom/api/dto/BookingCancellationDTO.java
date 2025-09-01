package app.conferenceroom.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookingCancellationDTO(
        @JsonProperty("status")
        boolean status) {
}
