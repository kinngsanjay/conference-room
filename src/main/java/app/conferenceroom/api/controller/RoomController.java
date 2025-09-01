package app.conferenceroom.api.controller;

import app.conferenceroom.api.dto.BookingRequestDTO;
import app.conferenceroom.api.dto.RoomDTO;
import app.conferenceroom.api.mapper.BookingMapper;
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
            @Valid @RequestBody BookingRequestDTO bookingRequestDto) {
        log.info("RoomController - getAllRooms - STARTED");
        var bookingModel = BookingMapper.toBookingMetadata(bookingRequestDto);
        var availableRooms = roomService.getAvailableRooms(bookingModel, limit)
                .stream()
                .map(RoomMapper::toDto)
                .toList();
        Response<List<RoomDTO>> response = Response.<List<RoomDTO>>builder().status(ResponseStatus.SUCCESS).data(availableRooms).build();
        log.info("RoomController - getAllRooms - ENDED");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Response<RoomDTO>> getRoomByName(@RequestParam String name) {
        log.info("RoomController - getRoomDetailsByName - STARTED");
        var roomDto = RoomMapper.toDto(roomService.getRoomByName(name));
        Response<RoomDTO> response = Response.<RoomDTO>builder().status(ResponseStatus.SUCCESS).data(roomDto).build();
        log.info("RoomController - getRoomDetailsByName - ENDED");
        return ResponseEntity.ok(response);
    }
}
