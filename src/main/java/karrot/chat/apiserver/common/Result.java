package karrot.chat.apiserver.common;

import karrot.chat.apiserver.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<T> {
    private final boolean success;
    private final T data;
    private final String errorCode;
    private final String errorMessage;

    // 성공 응답 생성
    public static <T> Result<T> success(T data) {
        return new Result<>(true, data, null, null);
    }

    public static <T> Result<T> success() {
        return new Result<>(true, null, null, null);
    }

    // 실패 응답 생성
    public static <T> Result<T> failure(String errorCode, String errorMessage) {
        return new Result<>(false, null, errorCode, errorMessage);
    }

    public static <T> Result<T> failure(ErrorCode errorCode) {
        return new Result<>(false, null, errorCode.getCode(), errorCode.getMessage());
    }

    public boolean isFailure() {
        return !success;
    }
}
