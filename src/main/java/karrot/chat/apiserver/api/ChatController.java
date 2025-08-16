package karrot.chat.apiserver.api;

import karrot.chat.apiserver.common.ApiResponse;
import karrot.chat.apiserver.domain.chat.dto.*;
import karrot.chat.apiserver.domain.chat.service.ChatService;
import karrot.chat.apiserver.utils.ApiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    public ApiResponse<List<ChatDto>> chatList(Long userId) {
        return ApiUtil.response(chatService.getChatsByUserId(userId));
    }

    @PostMapping("make/group")
    public ApiResponse<ChatDto> makeGroupChat(@RequestBody MakeGroupChatRequest request) {
        return ApiUtil.response(chatService.makeGroupChat(request.getUserId(), request.getTitle()));
    }

    @PostMapping("make/direct")
    public ApiResponse<ChatDto> makeDirectChat(@RequestBody MakeDirectChatRequest request) {
        return ApiUtil.response(chatService.makeDirectChat(request.getFromUserId(), request.getToUserId()));

    }

    @PostMapping("enter")
    public ApiResponse<ChatDto> enterGroupChat(@RequestBody EnterGroupChatRequest request) {
       return ApiUtil.response(chatService.enterGroupChat(request.getUserId(), request.getChatId()));

    }

    @PostMapping("leave")
    public ApiResponse<ChatDto> leaveGroupChat(@RequestBody LeaveGroupChatRequest request) {
       return ApiUtil.response(chatService.leaveGroupChat(request.getUserId(), request.getChatId()));

    }

    @GetMapping("{chatId}/messages")
    public ApiResponse<List<MessageDto>> getAllMessages(Long userId, @PathVariable("chatId") Long chatId) {
       return ApiUtil.response(chatService.getMessagesByChatId(userId, chatId));
    }
}
