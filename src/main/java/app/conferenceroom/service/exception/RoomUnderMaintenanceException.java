package app.conferenceroom.service.exception;

public class RoomUnderMaintenanceException extends RuntimeException {
    public RoomUnderMaintenanceException(String message) {
        super(message);
    }
}
