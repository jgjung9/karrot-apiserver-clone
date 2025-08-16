package karrot.chat.apiserver.domain.account.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class JoinRequest {

    private String username;
    private String password;
    private String nickname;
}
