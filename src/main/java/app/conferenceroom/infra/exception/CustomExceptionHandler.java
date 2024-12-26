package app.conferenceroom.infra.exception;

import jakarta.servlet.ServletException;
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

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(BindException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.info("handleValidationException Error message: {}", errorMessage);
        ErrorResponse errorResponse = new ErrorResponse(
                "VALIDATION_ERROR", HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServletException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleServletException(ServletException ex) {
        log.info("handleServletException Error message: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                "SERVLET_ERROR", HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConferenceRoomException.class)
    public ResponseEntity<ErrorResponse> handleAppException(ConferenceRoomException ex) {
        log.info("handleAppException Error code: {}", ex.getErrorCode());
        ErrorResponse errorResponse = new ErrorResponse("APPLICATION_ERROR", ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.info("handleHttpMessageNotReadable Error : {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                "MESSAGE_NOT_READABLE", HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage().split(": ")[1]);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Getter @Setter
    public static class ErrorResponse {
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
