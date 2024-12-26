package app.conferenceroom.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record RoomDto (
    String name,
    int capacity,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<MaintenanceTimeRange> maintenanceTimings
) {}
