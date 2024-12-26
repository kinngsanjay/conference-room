package app.conferenceroom.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record BookingRequestDto(
        String roomName,
        @JsonProperty("meetingDetails")
        @NotNull(message = "roomAvailabilityDto must not be null")
        @Valid
        RoomDetailsDto roomDetailsDto
) {}
