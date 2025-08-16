package karrot.chat.apiserver.domain.chat.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

// 메시지 정보
@Data
@Builder
public class MessageDto {
    private Long chatId;
    private Long senderId;
    private String text;
    private LocalDateTime sentAt;
}
