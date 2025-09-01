package app.conferenceroom.api.mapper;

import app.conferenceroom.api.dto.BookingRequestDTO;
import app.conferenceroom.api.dto.ExistingBookingDTO;
import app.conferenceroom.api.dto.MeetingDTO;
import app.conferenceroom.api.dto.TimeRangeDTO;
import app.conferenceroom.service.model.CreateBookingCommand;
import app.conferenceroom.service.model.BookingRecord;
import app.conferenceroom.service.model.TimeRange;

public class BookingMapper {

    public static CreateBookingCommand toBookingMetadata(BookingRequestDTO bookingRequestDto) {
        var timeRangeDTO = bookingRequestDto.timeRangeDTO();
        return new CreateBookingCommand(
                bookingRequestDto.roomName(),
                new TimeRange(
                        timeRangeDTO.startTime(),
                        timeRangeDTO.endTime()),
                bookingRequestDto.numberOfAttendees());
    }

    public static ExistingBookingDTO toResponseDto(BookingRecord bookingRecord) {
        var meetingTime = bookingRecord.meetingTime();
        var timeRangeDTO = new TimeRangeDTO(meetingTime.startTime(), meetingTime.endTime());

        var meetingDTO = new MeetingDTO(
                bookingRecord.roomName(),
                timeRangeDTO,
                bookingRecord.numberOfAttendees());

        return new ExistingBookingDTO(
                bookingRecord.bookingReference(),
                meetingDTO
        );
    }
}
