package app.conferenceroom.domain.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    NO_ROOM_AVAILABLE("NO_ROOM_AVAILABLE", HttpStatus.NOT_FOUND, "No room available for these booking details"),
    NO_SUCH_ROOM("NO_SUCH_ROOM", HttpStatus.NOT_FOUND, "No room exist with this name or id"),
    UNABLE_TO_BOOK_ROOM("UNABLE_TO_BOOK_ROOM", HttpStatus.PRECONDITION_FAILED, "Room is either booked or room name not correct"),
    NO_BOOKING_FOUND("NO_BOOKING_FOUND", HttpStatus.NOT_FOUND, "No booking for the selected time range"),
    ROOM_UNDER_MAINTENANCE("ROOM_UNDER_MAINTENANCE", HttpStatus.LOCKED, "Select time range overlaps Maintenance TIme");
    final String errorCode;
    final HttpStatus httpStatus;
    final String message;

    ErrorCode(String errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
