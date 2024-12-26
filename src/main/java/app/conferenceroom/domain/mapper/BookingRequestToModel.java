package app.conferenceroom.domain.mapper;

import app.conferenceroom.api.dto.BookingRequestDto;
import app.conferenceroom.domain.model.BookingModel;
import app.conferenceroom.domain.model.RoomModel;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class BookingRequestToModel implements Function<BookingRequestDto, BookingModel> {
    @Override
    public BookingModel apply(BookingRequestDto bookingRequestDto) {
        var bookingModel = new BookingModel();
        var roomModel = new RoomModel();
        roomModel.setName(bookingRequestDto.roomName());
        bookingModel.setRoomModel(roomModel);
        bookingModel.setRoomDetailsDto(bookingRequestDto.roomDetailsDto());
        return bookingModel;
    }
}
