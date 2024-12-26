package app.conferenceroom.api.controller;

import app.conferenceroom.api.dto.BookingRequestDto;
import app.conferenceroom.api.dto.MeetingTimeRange;
import app.conferenceroom.api.dto.RoomDetailsDto;
import app.conferenceroom.api.dto.RoomDto;
import app.conferenceroom.infra.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RoomControllerTest {
    @Autowired
    RoomController roomController;

    private MeetingTimeRange getMeetingTimeRange(LocalTime startTime) {
        return new MeetingTimeRange(startTime, startTime.plusMinutes(15));
    }

    private BookingRequestDto getBookingDto(LocalTime startTime) {
        return new BookingRequestDto(
                "Inspire", new RoomDetailsDto(getMeetingTimeRange(startTime), 5));
    }

    @Test
    public void testGetAllRooms() {
        LocalTime time = LocalTime.of(22, 0, 0);
        ResponseEntity<Response<List<RoomDto>>> response = roomController.getAllRooms(true, getBookingDto(time));
        Assertions.assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetRoomDetailsByName() {
        ResponseEntity<Response<RoomDto>> response = roomController.getRoomDetailsByName("Inspire");
        Assertions.assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
