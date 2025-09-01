package app.conferenceroom.api.controller;

import app.conferenceroom.api.dto.*;
import app.conferenceroom.api.infra.response.Response;
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

    private TimeRangeDTO getMeetingTimeRange(LocalTime startTime) {
        return new TimeRangeDTO(startTime, startTime.plusMinutes(15));
    }

    private SearchRoomRequestDTO getSearchRoomRequestDTO(LocalTime startTime) {
        return new SearchRoomRequestDTO(getMeetingTimeRange(startTime), 5);
    }

    @Test
    public void testSearch() {
        LocalTime time = LocalTime.of(22, 0, 0);
        ResponseEntity<Response<List<RoomDTO>>> response = roomController.search(1, getSearchRoomRequestDTO(time));
        Assertions.assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSearchByName() {
        ResponseEntity<Response<RoomDTO>> response = roomController.searchByName("Inspire");
        Assertions.assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
