package app.conferenceroom.domain.mapper;

import app.conferenceroom.api.dto.RoomDto;
import app.conferenceroom.domain.model.RoomModel;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RoomDtoMapper implements Function<RoomModel, RoomDto> {
    @Override
    public RoomDto apply(RoomModel roomModel) {
        return new RoomDto(roomModel.getName(),
                roomModel.getCapacity(),
                roomModel.getMaintenanceTimings());
    }
}
