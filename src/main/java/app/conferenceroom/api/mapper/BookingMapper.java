package app.conferenceroom.api.mapper;

import app.conferenceroom.api.dto.BookingRequestDto;
import app.conferenceroom.api.dto.BookingResponseDto;
import app.conferenceroom.domain.model.BookingModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "roomName", target = "roomModel.name")
    @Mapping(source = "meetingRequestDto", target = "meetingModel")
    BookingModel toModel(BookingRequestDto dto);

    @Mapping(source = "roomModel.name", target = "roomName")
    @Mapping(source = "meetingModel", target = "meetingResponseDto")
    BookingResponseDto toResponseDto(BookingModel model);

}
