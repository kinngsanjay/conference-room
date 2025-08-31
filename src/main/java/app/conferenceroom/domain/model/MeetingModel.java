package app.conferenceroom.domain.model;

import app.conferenceroom.api.dto.TimeRange;
import app.conferenceroom.domain.validator.meetingrange.ValidTimeRange;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MeetingModel(
    TimeRange timeRange,
    int numberOfPeople
) {}
