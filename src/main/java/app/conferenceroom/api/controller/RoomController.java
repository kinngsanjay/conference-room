package app.conferenceroom.api.controller;

import app.conferenceroom.api.dto.RoomDTO;
import app.conferenceroom.api.dto.SearchRoomRequestDTO;
import app.conferenceroom.api.mapper.RoomMapper;
import app.conferenceroom.service.RoomService;
import app.conferenceroom.api.infra.response.Response;
import app.conferenceroom.api.infra.response.ResponseStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
@Slf4j
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/search")
    public ResponseEntity<Response<List<RoomDTO>>> search(
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @Valid @RequestBody SearchRoomRequestDTO searchRoomRequestDTO) {
        log.info("RoomController - getAllRooms - STARTED");
        var searchRoomsCommand = RoomMapper.toSearchRoomCommad(searchRoomRequestDTO);
        var availableRooms = roomService.execute(searchRoomsCommand, limit)
                .roomModels()
                .stream()
                .map(RoomMapper::toDto)
                .toList();
        Response<List<RoomDTO>> response = Response.<List<RoomDTO>>builder().status(ResponseStatus.SUCCESS).data(availableRooms).build();
        log.info("RoomController - getAllRooms - ENDED");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<Response<RoomDTO>> searchByName(@PathVariable String name) {
        log.info("RoomController - getRoomDetailsByName - STARTED");
        var roomDto = RoomMapper.toDto(roomService.searchRoomByName(name));
        Response<RoomDTO> response = Response.<RoomDTO>builder().status(ResponseStatus.SUCCESS).data(roomDto).build();
        log.info("RoomController - getRoomDetailsByName - ENDED");
        return ResponseEntity.ok(response);
    }
}
