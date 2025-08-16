package karrot.chat.apiserver.auth;

import karrot.chat.apiserver.common.ApiResponse;
import karrot.chat.apiserver.domain.account.dto.JoinRequest;
import karrot.chat.apiserver.domain.account.dto.LoginRequest;
import karrot.chat.apiserver.domain.account.service.AccountService;
import karrot.chat.apiserver.jwt.TokenDto;
import karrot.chat.apiserver.utils.ApiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AccountService accountService;

    @PostMapping("join")
    @ResponseBody
    public ApiResponse<Void> join(@RequestBody JoinRequest request) {
        return ApiUtil.response(accountService.join(request.getUsername(), request.getPassword(), request.getNickname()));
    }

    @PostMapping("login")
    @ResponseBody
    public ApiResponse<TokenDto> login(@RequestBody LoginRequest request) {
        return ApiUtil.responseWithErrorMessage(
                accountService.login(request.getUsername(), request.getPassword()),
                "아이디 또는 비밀번호가 잘못되었습니다");
    }
}
