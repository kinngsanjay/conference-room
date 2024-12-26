package app.conferenceroom.infra.exception;

import app.conferenceroom.domain.enums.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConferenceRoomException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5215848433600151446L;
    private String errorCode;
    private HttpStatus httpStatus;
    private String message;

    public ConferenceRoomException(ErrorCode code) {
        super();
        this.errorCode = code.getErrorCode();
        this.httpStatus = code.getHttpStatus();
        this.message = code.getMessage();
    }
}
