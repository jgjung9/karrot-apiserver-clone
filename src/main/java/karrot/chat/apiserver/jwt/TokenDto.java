package karrot.chat.apiserver.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
}
