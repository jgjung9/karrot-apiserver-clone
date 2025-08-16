package karrot.chat.apiserver.exception;

import lombok.Getter;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("A001", "사용자를 찾을 수 없습니다"),
    USER_ALREADY_EXISTS("A002", "이미 존재하는 사용자 입니다"),
    USER_INVALID_PASSWORD("A003", "패스워드가 잘못되었습니다"),

    CHAT_NOT_FOUND("C001", "채팅방이 존재하지 않습니다"),
    CHAT_ALREADY_EXISTS_DIRECT("C002", "이미 개인 채팅방이 존재합니다"),
    CHAT_ALREADY_GROUP_MEMBER("C003", "이미 해당 그룹에 존재합니다."),
    CHAT_NOT_GROUP_MEMBER("C004", "그룹 채팅방의 멤버가 아닙니다"),
    CHAT_TYPE_DIRECT("C005", "개인 채팅방은 나갈 수 없다");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorCode ofCode(String code) {
        return Arrays.stream(ErrorCode.values())
                .filter(errorCode -> errorCode.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("ErrorCode Not Found: " + code));
    }
}
