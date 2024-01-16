package app.conferenceroom.facade.service;

import app.conferenceroom.facade.dto.MaintenanceTimeRange;
import app.conferenceroom.facade.dto.MeetingTimeRange;
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

    public boolean overlaps(MeetingTimeRange otherTimeRange) {
        for(MaintenanceTimeRange timeRange : timings) {
            if(LocalDateTime.of(LocalDate.now(), timeRange.getStartTime()).isBefore(otherTimeRange.getEndTime()) &&
                    LocalDateTime.of(LocalDate.now(), timeRange.getEndTime()).isAfter(otherTimeRange.getStartTime())) {
                throw new ConferenceRoomException("ROOM_UNDER_MAINTENANCE", "Select time range overlaps Maintenance TIme");
            }
        }
        return false;
    }
}
