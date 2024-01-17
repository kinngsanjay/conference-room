package app.conferenceroom.infra.exception;

import app.conferenceroom.facade.enums.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConferenceRoomException extends RuntimeException {
    private static final long serialVersionUID = -5215848433600151446L;
    private String errorCode;
    private String message;

    private void init(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
    public ConferenceRoomException(ErrorCode code) {
        super();
        init(code.getErrorCode(), code.getMessage());
    }
}
