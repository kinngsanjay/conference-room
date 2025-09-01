package app.conferenceroom.api.infra.errors;

import app.conferenceroom.api.infra.errors.enums.ErrorCode;
import app.conferenceroom.api.infra.response.ResponseStatus;

public record ErrorResponseDTO(
        String status,
        String errorCode,
        String erroMessage) {
}
