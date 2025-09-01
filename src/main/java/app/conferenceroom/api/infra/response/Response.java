package app.conferenceroom.api.infra.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private ResponseStatus status;
    private T data;

    public boolean success() {
        return Objects.nonNull(status) && status == ResponseStatus.SUCCESS;
    }
    public boolean hasData() {
        return Objects.nonNull(data);
    }
}
