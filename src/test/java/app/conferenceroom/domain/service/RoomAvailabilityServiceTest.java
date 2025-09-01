package app.conferenceroom.domain.service;

import app.conferenceroom.service.impl.RoomAvailabilityService;
import app.conferenceroom.service.exception.BookingNotFoundException;
import app.conferenceroom.service.exception.RoomUnderMaintenanceException;
import app.conferenceroom.service.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoomAvailabilityServiceTest {
    @Autowired
    RoomAvailabilityService roomAvailabilityService;

    private TimeRange getMeetingTimeRange() {
        LocalTime time = LocalTime.of(14, 0, 0);
        return new TimeRange(time, time.plusHours(1));
    }

    @Test
    public void testAvailabilityWithoutRoom() {
        LocalTime startTime = LocalTime.of(10, 0, 0);
        SearchRoomsCommand searchRoomsCommand = new SearchRoomsCommand(
                new TimeRange(startTime, startTime.plusHours(1)), 5);

        List<RoomModel> rooms = roomAvailabilityService.searchAvailableRooms(
                new TimeRange(startTime, startTime.plusHours(1)), 5);
        assertNotNull(rooms);
        assertFalse(rooms.isEmpty());
        assertEquals(2, rooms.size());
        assertEquals("Beauty", rooms.get(0).name());
    }

    @Test
    public void testAvailabilityWithEndTimeBeforeBookingStartTime() {
        LocalTime startTime = LocalTime.of(10, 0, 0);
        SearchRoomsCommand searchRoomsCommand = new SearchRoomsCommand(
                new TimeRange(startTime, startTime.minusHours(1)), 5);

        List<RoomModel> rooms = roomAvailabilityService.searchAvailableRooms(
                new TimeRange(startTime, startTime.minusHours(1)), 5);

        assertEquals("Beauty", rooms.get(0).name());
    }

    @Test
    public void testGetAllBookings() throws BookingNotFoundException {
        LocalTime time = LocalTime.of(10, 0, 0);
        TimeRange timeRange = new TimeRange(time.minusHours(1), time.plusHours(1));

        List<BookingRecord> bookings = roomAvailabilityService.getAllBookings(timeRange);

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
        assertEquals(1, bookings.size());
        assertEquals("Inspire", bookings.get(0).roomName());
    }

    @Test()
    public void testGetAllBookingsNoBookingsFound() {
        LocalTime time = LocalTime.of(13, 0, 0);
        TimeRange timeRange = new TimeRange(time.plusHours(2), time.plusHours(4));

        assertThrows(BookingNotFoundException.class, () -> {
            roomAvailabilityService.getAllBookings(timeRange);
        });

    }

    @Test()
    public void testNoRoomsFoundDueToCapacity() {
        assertEquals(roomAvailabilityService.searchAvailableRooms(
                getMeetingTimeRange(), 22).size(), 0);
    }

    @Test()
    public void testWhenRoomIsUnderMaintenance() {
        LocalTime time = LocalTime.of(9, 0, 0);

        assertThrows(RoomUnderMaintenanceException.class, () -> {
            roomAvailabilityService.searchAvailableRooms(
                    new TimeRange(time, time.plusMinutes(15)), 22);
        });
    }
}
