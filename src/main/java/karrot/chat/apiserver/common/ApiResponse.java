package karrot.chat.apiserver.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import karrot.chat.apiserver.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final int status;
    private final String message;
    private final T data;
    private final String error;
    private final LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> success(T data) {
        return success("성공", data);
    }

    public static ApiResponse<Void> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> error(int status, String message, String errorCode) {
        return new ApiResponse<>(status, message, null, errorCode, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return error(status, message, null);
    }

    public static <T> ApiResponse<T> badRequest(String message, String errorCode) {
        return error(400, message, errorCode);
    }
    public static <T> ApiResponse<T> badRequest(String message) {
        return badRequest(message, null);
    }

    public static <T> ApiResponse<T> notFound(String message, String errorCode) {
        return error(404, message, errorCode);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return notFound(message, null);
    }

    public static <T> ApiResponse<T> internalServerError(String message, String errorCode) {
        return error(500, message, errorCode);
    }

    public static <T> ApiResponse<T> internalServerError(String message) {
        return internalServerError(message, null);
    }

}
