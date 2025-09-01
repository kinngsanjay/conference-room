package app.conferenceroom.service.exception;

public class RoomNotBookedException extends RuntimeException {
    public RoomNotBookedException(String message) {
        super(message);
    }
}
