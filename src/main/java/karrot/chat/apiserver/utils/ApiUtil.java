package karrot.chat.apiserver.utils;

import karrot.chat.apiserver.common.ApiResponse;
import karrot.chat.apiserver.common.Result;
import org.springframework.util.StringUtils;

public class ApiUtil {

    public static <T> ApiResponse<T> response(Result<T> result) {
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }

        if (StringUtils.hasText(result.getErrorCode())) {
            return ApiResponse.badRequest(result.getErrorMessage(), result.getErrorCode());
        } else {
            return ApiResponse.internalServerError("서버에 문자가 발생했습니다. 잠시 후 다시 시도해 주세요");
        }
    }

    public static <T> ApiResponse<T> responseWithErrorMessage(Result<T> result, String message) {
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }

        if (StringUtils.hasText(result.getErrorCode())) {
            return ApiResponse.badRequest(message, result.getErrorCode());
        } else {
            return ApiResponse.internalServerError(message);
        }
    }
}
