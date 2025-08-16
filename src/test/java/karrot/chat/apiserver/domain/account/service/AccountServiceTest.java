package karrot.chat.apiserver.domain.account.service;

import karrot.chat.apiserver.common.Result;
import karrot.chat.apiserver.domain.account.entity.Account;
import karrot.chat.apiserver.domain.account.repository.AccountRepository;
import karrot.chat.apiserver.exception.ErrorCode;
import karrot.chat.apiserver.jwt.TokenDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class AccountServiceTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String USERNAME = "testid";
    private final String PASSWORD = "password123";
    private final String NICKNAME = "nickname123";


    @BeforeEach
    void setUp() {
        Account account = Account.createAccount(
                USERNAME,
                passwordEncoder.encode(PASSWORD),
                NICKNAME
        );
        accountRepository.save(account);
    }

    @Test
    @DisplayName("회원가입")
    void join() {
        String username = "testid123";
        String password = "password123";
        String nickname = "nickname123";
        accountService.join(username, password, nickname);

        Account found = accountRepository.findByUsername(username).get();

        assertThat(found.getUsername()).isEqualTo(username);
        assertThat(found.getNickname()).isEqualTo(nickname);
    }

    @Test
    @DisplayName("이미 존재하는 회원 예외 발생")
    void cannotJoinByExistsId() throws Exception {
        Result<Void> joinResult = accountService.join(USERNAME, PASSWORD, NICKNAME);

        assertThat(joinResult.isSuccess()).isFalse();
        assertThat(ErrorCode.ofCode(joinResult.getErrorCode()))
                .isEqualTo(ErrorCode.USER_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        assertThat(accountService.login(USERNAME, PASSWORD).isSuccess()).isTrue();
    }

    @Test
    @DisplayName("로그인 아이디 없음")
    void cannotLoginByInvalidId() throws Exception {
        Result<TokenDto> loginResult = accountService.login(USERNAME + "12", PASSWORD);
        assertThat(loginResult.isSuccess()).isFalse();
        assertThat(ErrorCode.ofCode(loginResult.getErrorCode()))
                .isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("로그인 비밀번호 틀림 예외 발생")
    void cannotLoginByInvalidPassword() throws Exception {
        Result<TokenDto> loginResult = accountService.login(USERNAME, PASSWORD + "invalid");
        assertThat(loginResult.isSuccess()).isFalse();
        assertThat(ErrorCode.ofCode(loginResult.getErrorCode()))
                .isEqualTo(ErrorCode.USER_INVALID_PASSWORD);
    }
}