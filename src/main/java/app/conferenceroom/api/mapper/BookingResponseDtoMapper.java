package app.conferenceroom.api.mapper;

import app.conferenceroom.api.dto.BookingResponseDto;
import app.conferenceroom.domain.model.BookingModel;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class BookingResponseDtoMapper implements Function<BookingModel, BookingResponseDto> {
    @Override
    public BookingResponseDto apply(BookingModel bookingModel) {
        return new BookingResponseDto(bookingModel.getBookingReference(),
                bookingModel.getRoomModel().getName(),
                bookingModel.getRoomDetailsDto());
    }
}
