package app.conferenceroom.domain.service;

import app.conferenceroom.api.dto.TimeRange;
import app.conferenceroom.domain.enums.ErrorCode;
import app.conferenceroom.infra.exception.ConferenceRoomException;
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
                    throw new ConferenceRoomException(ErrorCode.ROOM_UNDER_MAINTENANCE);
                }
        );
    }
}
