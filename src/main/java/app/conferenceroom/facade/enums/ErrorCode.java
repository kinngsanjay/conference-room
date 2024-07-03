package app.conferenceroom.facade.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NO_ROOM_AVAILABLE("NO_ROOM_AVAILABLE", "No room available for these booking details"),
    NO_SUCH_ROOM("NO_SUCH_ROOM", "No room exist with this name or id"),
    UNABLE_TO_BOOK_ROOM("UNABLE_TO_BOOK_ROOM", "Unable to book the room"),
    NO_BOOKING_FOUND("NO_BOOKING_FOUND", "No booking for the selected time range"),
    ROOM_UNDER_MAINTENANCE("ROOM_UNDER_MAINTENANCE", "Select time range overlaps Maintenance TIme");
    final String errorCode;
    final String message;

    ErrorCode(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
