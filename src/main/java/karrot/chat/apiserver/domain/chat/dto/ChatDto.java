package karrot.chat.apiserver.domain.chat.dto;

import lombok.Builder;
import lombok.Data;

// 채팅방 정보
@Data
@Builder
public class ChatDto {
    private Long chatId;
    private String title;
    private String chatType;
}
