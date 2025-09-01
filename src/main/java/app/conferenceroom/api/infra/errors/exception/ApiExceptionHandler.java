package app.conferenceroom.api.infra.errors.exception;

import app.conferenceroom.api.infra.errors.ErrorResponseDTO;
import app.conferenceroom.api.infra.errors.enums.ErrorCode;
import app.conferenceroom.service.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static app.conferenceroom.api.infra.response.ResponseStatus.ERROR;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RoomNotAvailableException.class)
    public ErrorResponseDTO handleRoomNotAvailableException(RoomNotAvailableException exception){
        log.error("Room booking failed.", exception);
        ErrorCode errorcode = ErrorCode.NO_ROOM_AVAILABLE;
        return new ErrorResponseDTO(ERROR.toValue(), errorcode.getErrorCode(), errorcode.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BookingNotFoundException.class)
    public ErrorResponseDTO handleBookingNotFoundException(BookingNotFoundException exception){
        log.error("Booking not found.", exception);
        ErrorCode errorcode = ErrorCode.NO_BOOKING_FOUND;
        return new ErrorResponseDTO(ERROR.toValue(), errorcode.getErrorCode(), errorcode.getMessage());
    }

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(RoomNotBookedException.class)
    public ErrorResponseDTO handleRoomNotBookedException(RoomNotBookedException exception){
        log.error("Unable to book room.", exception);
        ErrorCode errorcode = ErrorCode.UNABLE_TO_BOOK_ROOM;
        return new ErrorResponseDTO(ERROR.toValue(), errorcode.getErrorCode(), errorcode.getMessage());
    }

    @ResponseStatus(HttpStatus.LOCKED)
    @ExceptionHandler(RoomUnderMaintenanceException.class)
    public ErrorResponseDTO handleRoomUnderMaintenanceException(RoomUnderMaintenanceException exception){
        log.error("Room is under maintenance.", exception);
        ErrorCode errorcode = ErrorCode.ROOM_UNDER_MAINTENANCE;
        return new ErrorResponseDTO(ERROR.toValue(), errorcode.getErrorCode(), errorcode.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ExceptionHandler(RoomNotExistException.class)
    public ErrorResponseDTO handleRoomNotExistException(RoomNotExistException exception){
        log.error("Room does not exist.", exception);
        ErrorCode errorcode = ErrorCode.NO_SUCH_ROOM;
        return new ErrorResponseDTO(ERROR.toValue(), errorcode.getErrorCode(), errorcode.getMessage());
    }
}
