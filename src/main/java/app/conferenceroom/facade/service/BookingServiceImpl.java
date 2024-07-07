package app.conferenceroom.facade.service;

import app.conferenceroom.db.entity.Booking;
import app.conferenceroom.db.repository.BookingRepository;
import app.conferenceroom.facade.dto.BookingDto;
import app.conferenceroom.facade.dto.CancelDto;
import app.conferenceroom.facade.dto.MeetingTimeRange;
import app.conferenceroom.facade.enums.ErrorCode;
import app.conferenceroom.infra.exception.ConferenceRoomException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    private RoomAvailabilityService roomAvailabilityService;

    @Override
    @Transactional
    public String bookRoom(BookingDto bookingDto) {
        log.info("bookRoom started: {}", bookingDto);
        var roomName = roomAvailabilityService.confirmBooking(bookingDto);
        var booking = new Booking();
        BeanUtils.copyProperties(bookingDto, booking);
        BeanUtils.copyProperties(bookingDto.getMeetingDetails(), booking);
        BeanUtils.copyProperties(bookingDto.getMeetingDetails().timeRange(), booking);
        log.info("bookRoom ended: {}", booking);
        bookingRepository.save(booking);
        return String.format("Conference Room Booked. Room Name: %s", roomName);
    }

    @Override
    public List<BookingDto> getBookingsByTime(MeetingTimeRange timeRange) {
        log.info("getBookingsByTime, timeRange: {}", timeRange);
        return roomAvailabilityService.getAllBookings(timeRange);
    }

    @Override
    public String cancelBooking(CancelDto cancelDto) {
        log.info("cancelDto started, cancelDto: {}", cancelDto);
        Optional<Booking> booking = bookingRepository.findByRoomIdAndStartTimeAndEndTime(cancelDto.roomId(),
                cancelDto.timeRange().startTime(), cancelDto.timeRange().endTime());
        log.info("Result booking: {}", booking);
        if(booking.isPresent()) {
            bookingRepository.deleteById(booking.get().getBookingId());
        } else {
            throw new ConferenceRoomException(ErrorCode.NO_BOOKING_FOUND);
        }
        return "Conference Room Booking Cancelled";
    }
}
