package app.conferenceroom.api.infra.errors.exception;

import app.conferenceroom.api.infra.errors.ErrorResponseDTO;
import jakarta.servlet.ServletException;
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
    public ResponseEntity<ErrorResponseDTO> handleValidationException(BindException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.info("handleValidationException Error message: {}", errorMessage);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "VALIDATION_ERROR", HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServletException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleServletException(ServletException ex) {
        log.info("handleServletException Error message: {}", ex.getMessage());
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "SERVLET_ERROR", HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.info("handleHttpMessageNotReadable Error : {}", ex.getMessage());
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "MESSAGE_NOT_READABLE", HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage().split(": ")[1]);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
