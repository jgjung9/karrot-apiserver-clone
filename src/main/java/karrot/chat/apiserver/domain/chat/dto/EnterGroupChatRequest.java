package karrot.chat.apiserver.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnterGroupChatRequest {
    private Long userId;
    private Long chatId;
}
