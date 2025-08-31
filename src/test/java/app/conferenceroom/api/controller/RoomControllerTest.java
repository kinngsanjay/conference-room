package app.conferenceroom.api.controller;

import app.conferenceroom.api.dto.*;
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

    private TimeRange getMeetingTimeRange(LocalTime startTime) {
        return new TimeRange(startTime, startTime.plusMinutes(15));
    }

    private BookingRequestDto getBookingDto(LocalTime startTime) {
        return new BookingRequestDto(
                "Inspire", new MeetingRequestDto(getMeetingTimeRange(startTime), 5));
    }

    @Test
    public void testSearch() {
        LocalTime time = LocalTime.of(22, 0, 0);
        ResponseEntity<Response<List<RoomDto>>> response = roomController.search(true, getBookingDto(time));
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
