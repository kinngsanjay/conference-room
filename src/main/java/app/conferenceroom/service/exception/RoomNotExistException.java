package app.conferenceroom.service.exception;

public class RoomNotExistException extends RuntimeException {
    public RoomNotExistException(String message) {
        super(message);
    }
}
