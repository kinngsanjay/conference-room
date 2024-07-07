package app.conferenceroom.facade.controller;

import app.conferenceroom.facade.dto.BookingDto;
import app.conferenceroom.facade.dto.RoomAvailabilityResDto;
import app.conferenceroom.facade.service.RoomService;
import app.conferenceroom.facade.dto.RoomDto;
import app.conferenceroom.infra.response.Response;
import app.conferenceroom.infra.response.ResponseStatus;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conference-room")
@Slf4j
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PostMapping
    public ResponseEntity<Response<RoomAvailabilityResDto>> getAllRooms(
            @RequestParam(value = "bestRoom", required = false, defaultValue = "false") boolean bestRoom,
            @Valid @RequestBody BookingDto bookingDto) {
        log.info("RoomController - getAllRooms - STARTED");
        Response<RoomAvailabilityResDto> response = Response.<RoomAvailabilityResDto>builder()
                .status(ResponseStatus.SUCCESS)
                .data(roomService.getAvailableRooms(bookingDto, bestRoom)).build();
        log.info("RoomController - getAllRooms - ENDED");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Response<RoomDto>> getRoomByName(@RequestParam String name) {
        log.info("RoomController - getRoomByName - STARTED");
        Response<RoomDto> response = Response.<RoomDto>builder()
                .status(ResponseStatus.SUCCESS).data(roomService.getRoomByName(name)).build();
        log.info("RoomController - getRoomByName - ENDED");
        return ResponseEntity.ok(response);
    }
}
