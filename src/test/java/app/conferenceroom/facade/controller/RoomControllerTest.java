package app.conferenceroom.facade.controller;

import app.conferenceroom.facade.dto.*;
import app.conferenceroom.infra.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
public class RoomControllerTest {
    @Autowired
    RoomController roomController;

    private MeetingTimeRange getMeetingTimeRange(LocalDateTime startTime) {
        return new MeetingTimeRange(startTime, startTime.plusMinutes(15));
    }

    private BookingDto getBookingDto(LocalDateTime startTime) {
        return new BookingDto(
                3L, new RoomAvailabilityDto(getMeetingTimeRange(startTime), 5));
    }

    @Test
    public void testGetAllRooms() {
        LocalDateTime time = LocalDateTime.now().with(LocalTime.of(22, 0, 0));
        ResponseEntity<Response<RoomAvailabilityResDto>> response = roomController.getAllRooms(true, getBookingDto(time));
        Assertions.assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetRoomByName() {
        ResponseEntity<Response<RoomDto>> response = roomController.getRoomByName("Inspire");
        Assertions.assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
