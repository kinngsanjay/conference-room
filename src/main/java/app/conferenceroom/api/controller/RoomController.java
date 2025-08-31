package app.conferenceroom.api.controller;

import app.conferenceroom.api.dto.BookingRequestDto;
import app.conferenceroom.api.mapper.BookingMapper;
import app.conferenceroom.api.mapper.RoomMapper;
import app.conferenceroom.domain.service.RoomService;
import app.conferenceroom.api.dto.RoomDto;
import app.conferenceroom.infra.response.Response;
import app.conferenceroom.infra.response.ResponseStatus;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conference-room")
@Slf4j
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;
    private final BookingMapper bookingMapper;

    public RoomController(RoomService roomService, RoomMapper roomMapper, BookingMapper bookingMapper) {
        this.roomService = roomService;
        this.roomMapper = roomMapper;
        this.bookingMapper = bookingMapper;
    }

    @PostMapping("/search")
    public ResponseEntity<Response<List<RoomDto>>> search(@RequestParam(value = "bestRoom", required = false, defaultValue = "false") boolean bestRoom, @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("RoomController - getAllRooms - STARTED");
        var bookingModel = bookingMapper.toModel(bookingRequestDto);
        var availableRooms = roomService.getAvailableRooms(bookingModel, bestRoom)
                .stream()
                .map(roomMapper::toDto)
                .toList();
        Response<List<RoomDto>> response = Response.<List<RoomDto>>builder().status(ResponseStatus.SUCCESS).data(availableRooms).build();
        log.info("RoomController - getAllRooms - ENDED");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Response<RoomDto>> getRoomDetailsByName(@RequestParam String name) {
        log.info("RoomController - getRoomDetailsByName - STARTED");
        var roomDto = roomMapper.toDto(roomService.getRoomByName(name));
        Response<RoomDto> response = Response.<RoomDto>builder().status(ResponseStatus.SUCCESS).data(roomDto).build();
        log.info("RoomController - getRoomDetailsByName - ENDED");
        return ResponseEntity.ok(response);
    }
}
