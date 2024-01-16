package app.conferenceroom.facade.controller;

import app.conferenceroom.facade.dto.RoomAvailabilityDto;
import app.conferenceroom.facade.dto.RoomAvailabilityResDto;
import app.conferenceroom.facade.service.RoomService;
import app.conferenceroom.facade.dto.RoomDto;
import app.conferenceroom.infra.response.Response;
import app.conferenceroom.infra.response.ResponseStatus;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PostMapping
    public ResponseEntity<Response<RoomAvailabilityResDto>> getAllRooms(
            @Valid @RequestBody RoomAvailabilityDto availabilityDto) {
        Response<RoomAvailabilityResDto> response = Response.<RoomAvailabilityResDto>builder()
                .status(ResponseStatus.SUCCESS)
                .data(roomService.getAvailableRooms(availabilityDto)).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{name}")
    public RoomDto getRoomByName(@PathVariable String name) {
        return roomService.getRoomByName(name);
    }
}
