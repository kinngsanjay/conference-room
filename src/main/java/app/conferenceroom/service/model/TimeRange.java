package app.conferenceroom.service.model;

import java.time.LocalTime;


public record TimeRange(
        LocalTime startTime,
        LocalTime endTime) {
}
