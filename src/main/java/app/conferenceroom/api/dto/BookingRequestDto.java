package app.conferenceroom.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record BookingRequestDto(
        @JsonProperty("roomName")
        String roomName,
        @JsonProperty("meetingRequest")
        @NotNull(message = "meeting request must have value")
        @Valid
        MeetingRequestDto meetingRequestDto
) {}
