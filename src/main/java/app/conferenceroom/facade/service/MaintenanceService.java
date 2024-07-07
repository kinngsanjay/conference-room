package app.conferenceroom.facade.service;

import app.conferenceroom.facade.dto.MaintenanceTimeRange;
import app.conferenceroom.facade.dto.MeetingTimeRange;
import app.conferenceroom.facade.enums.ErrorCode;
import app.conferenceroom.infra.exception.ConferenceRoomException;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Component
@ConfigurationProperties(prefix = "app.maintenance")
@Slf4j
@Data
public class MaintenanceService {

    private List<MaintenanceTimeRange> timings;

    public void checkForOverlaps(MeetingTimeRange meetingTimeRange) {
        log.info("Checking for overlaps: {}:", meetingTimeRange);

        Predicate<MaintenanceTimeRange> overlapCondition = timeRange ->
                LocalDateTime.of(LocalDate.now(), timeRange.startTime()).isBefore(meetingTimeRange.endTime()) &&
                LocalDateTime.of(LocalDate.now(), timeRange.endTime()).isAfter(meetingTimeRange.startTime());

        timings.stream().filter(overlapCondition).findAny().ifPresent(range -> {
                    log.info("ROOM UNDER MAINTENANCE!!!");
                    throw new ConferenceRoomException(ErrorCode.ROOM_UNDER_MAINTENANCE);
                }
        );
    }
}
