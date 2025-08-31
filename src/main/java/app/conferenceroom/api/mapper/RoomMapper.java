package app.conferenceroom.api.mapper;

import app.conferenceroom.api.dto.RoomDto;
import app.conferenceroom.domain.model.RoomModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomDto toDto(RoomModel roomModel);
}
