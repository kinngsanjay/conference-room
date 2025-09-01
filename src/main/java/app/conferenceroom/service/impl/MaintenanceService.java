package app.conferenceroom.service.impl;

import app.conferenceroom.service.exception.RoomUnderMaintenanceException;
import app.conferenceroom.service.model.TimeRange;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
@ConfigurationProperties(prefix = "app.maintenance")
@Slf4j
@Data
public class MaintenanceService {

    private List<TimeRange> timings;

    public void checkForOverlaps(TimeRange meetingTimeRange) {
        log.info("Checking for overlaps: {}:", meetingTimeRange);

        Predicate<TimeRange> overlapCondition = timeRange ->
                timeRange.startTime().isBefore(meetingTimeRange.endTime()) &&
                        timeRange.endTime().isAfter(meetingTimeRange.startTime());

        timings.stream().filter(overlapCondition).findAny().ifPresent(range -> {
                    log.info("ROOM UNDER MAINTENANCE!!!");
                    throw new RoomUnderMaintenanceException("Room will be under maintenance for range %s-%s"
                            .formatted(meetingTimeRange.startTime(), meetingTimeRange.endTime()));
                }
        );
    }
}
