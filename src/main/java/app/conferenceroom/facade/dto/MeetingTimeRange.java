package app.conferenceroom.facade.dto;

import app.conferenceroom.facade.validator.meetingrange.ValidMeetingTimeRange;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ValidMeetingTimeRange
public class MeetingTimeRange {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime endTime;

}
