package app.conferenceroom.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RoomDTO(
        @JsonProperty("name")
        String name,
        @JsonProperty("capacity")
        int capacity,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("maintenanceTimings")
        List<TimeRangeDTO> maintenanceTimings
) {}
