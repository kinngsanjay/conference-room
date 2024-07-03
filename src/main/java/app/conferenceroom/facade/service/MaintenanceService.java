package app.conferenceroom.facade.service;

import app.conferenceroom.facade.dto.MaintenanceTimeRange;
import app.conferenceroom.facade.dto.MeetingTimeRange;
import app.conferenceroom.facade.enums.ErrorCode;
import app.conferenceroom.infra.exception.ConferenceRoomException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "maintenance")
@Data
public class MaintenanceService {

    private List<MaintenanceTimeRange> timings;

    public void checkForOverlaps(MeetingTimeRange otherTimeRange) {
        for(MaintenanceTimeRange timeRange : timings) {
            if(LocalDateTime.of(LocalDate.now(), timeRange.startTime()).isBefore(otherTimeRange.endTime()) &&
                    LocalDateTime.of(LocalDate.now(), timeRange.endTime()).isAfter(otherTimeRange.startTime())) {
                throw new ConferenceRoomException(ErrorCode.ROOM_UNDER_MAINTENANCE);
            }
        }
    }
}
