package app.conferenceroom.api.controller;

import app.conferenceroom.api.dto.BookingRequestDto;
import app.conferenceroom.api.mapper.BookingRequestToModel;
import app.conferenceroom.api.mapper.RoomDtoMapper;
import app.conferenceroom.domain.service.RoomService;
import app.conferenceroom.api.dto.RoomDto;
import app.conferenceroom.infra.response.Response;
import app.conferenceroom.infra.response.ResponseStatus;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conference-room")
@Slf4j
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomDtoMapper roomDtoMapper;
    @Autowired
    private BookingRequestToModel bookingRequestToModel;

    @PostMapping
    public ResponseEntity<Response<List<RoomDto>>> getAllRooms(
            @RequestParam(value = "bestRoom", required = false, defaultValue = "false") boolean bestRoom,
            @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("RoomController - getAllRooms - STARTED");
        var bookingModel = bookingRequestToModel.apply(bookingRequestDto);
        var availableRooms = roomService.getAvailableRooms(bookingModel, bestRoom).stream()
                .map(roomDtoMapper).toList();
        Response<List<RoomDto>> response = Response.<List<RoomDto>>builder()
                .status(ResponseStatus.SUCCESS)
                .data(availableRooms).build();
        log.info("RoomController - getAllRooms - ENDED");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Response<RoomDto>> getRoomDetailsByName(@RequestParam String name) {
        log.info("RoomController - getRoomDetailsByName - STARTED");
        var roomDto = roomDtoMapper.apply(roomService.getRoomByName(name));
        Response<RoomDto> response = Response.<RoomDto>builder()
                .status(ResponseStatus.SUCCESS).data(roomDto).build();
        log.info("RoomController - getRoomDetailsByName - ENDED");
        return ResponseEntity.ok(response);
    }
}
