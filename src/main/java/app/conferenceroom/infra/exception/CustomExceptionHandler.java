package app.conferenceroom.infra.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(BindException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.info("handleValidationException Error message: {}", errorMessage);
        ErrorResponse errorResponse = new ErrorResponse("error", HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConferenceRoomException.class)
    public ResponseEntity<ErrorResponse> handleAppException(ConferenceRoomException ex) {
        log.info("handleAppException Error code: {}", ex.getErrorCode());
        ErrorResponse errorResponse = new ErrorResponse("error", ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.info("handleHttpMessageNotReadable Error : {}", ex.getMessage());
        ErrorResponse errorMessage = new ErrorResponse("error", "INVALID_INPUT",
                "Incorrect time format. Please use the format 'HH:mm'.");
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @Getter @Setter
    static class ErrorResponse {
        private String status;
        private String errorCode;
        private String errorMessage;

        public ErrorResponse(String status, String errorCode, String errorMessage) {
            this.status = status;
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

    }
}
