package karrot.chat.apiserver.domain.account.service;

import karrot.chat.apiserver.common.Result;
import karrot.chat.apiserver.domain.account.entity.Account;
import karrot.chat.apiserver.domain.account.repository.AccountRepository;
import karrot.chat.apiserver.exception.ErrorCode;
import karrot.chat.apiserver.jwt.JwtTokenService;
import karrot.chat.apiserver.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    @Transactional
    public Result<Void> join(String username, String password, String nickname) {
        if (accountRepository.findByUsername(username).isPresent()) {
            return Result.failure(ErrorCode.USER_ALREADY_EXISTS);
        }
        Account account = Account.createAccount(
                username,
                passwordEncoder.encode(password),
                nickname
        );
        accountRepository.save(account);
        return Result.success();
    }

    public Result<TokenDto> login(String username, String password) {
        Optional<Account> found = accountRepository.findByUsername(username);
        if (found.isEmpty()) {
            return Result.failure(ErrorCode.USER_NOT_FOUND);
        }

        Account account = found.get();
        if (!passwordEncoder.matches(password, account.getPassword())) {
            return Result.failure(ErrorCode.USER_INVALID_PASSWORD);
        }
        return Result.success(jwtTokenService.createToken(account));
    }

}
