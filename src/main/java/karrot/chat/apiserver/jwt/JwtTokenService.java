package karrot.chat.apiserver.jwt;

import karrot.chat.apiserver.domain.account.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;

    public TokenDto createToken(Account account) {
        Instant now = Instant.now();

        // Access Token 생성
        JwtClaimsSet accessTokenClaims = JwtClaimsSet.builder()
                .issuer("http://localhost:8080")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .subject(account.getUsername())
                .claim("userId", account.getId())
                .claim("nickname", account.getNickname())
                .claim("scope", "read write")
                .build();

        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(accessTokenClaims)).getTokenValue();

        // Refresh Token 생성
        JwtClaimsSet refreshTokenClaims = JwtClaimsSet.builder()
                .issuer("http://localhost:8080")
                .issuedAt(now)
                .expiresAt(now.plus(24, ChronoUnit.HOURS))
                .subject(account.getUsername())
                .claim("userId", account.getId())
                .claim("type", "refresh")
                .build();

        String refreshToken = jwtEncoder.encode(JwtEncoderParameters.from(refreshTokenClaims)).getTokenValue();

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(1800L) // 30분 (초 단위)
                .build();
    }

    public Long getUserId(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return jwt.getClaim("userId");
    }
}
