package karrot.chat.apiserver.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MakeGroupChatRequest {

    private Long userId;
    private String title;
}
