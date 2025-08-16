package karrot.chat.apiserver.domain.chat.service;

import karrot.chat.apiserver.common.Result;
import karrot.chat.apiserver.domain.chat.dto.ChatDto;
import karrot.chat.apiserver.domain.chat.dto.MessageDto;
import karrot.chat.apiserver.domain.chat.entity.Chat;
import karrot.chat.apiserver.domain.chat.entity.ChatType;
import karrot.chat.apiserver.domain.chat.entity.UserChat;
import karrot.chat.apiserver.domain.chat.repository.ChatRepository;
import karrot.chat.apiserver.domain.chat.repository.UserChatRepository;
import karrot.chat.apiserver.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(value = "chatTransactionManager", readOnly = true)
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserChatRepository userChatRepository;

    public Result<List<ChatDto>> getChatsByUserId(Long userId) {
        log.debug("getChatsByUserId userId={}", userId);

        List<UserChat> userChats = userChatRepository.findByUserId(userId);
        log.debug("userChats={}", userChats);
        List<ChatDto> data = new ArrayList<>();
        userChats.stream()
                .forEach(userChat -> data.add(ChatDto.builder().
                        chatId(userChat.getChat().getId())
                        .title(userChat.getChat().getTitle())
                        .chatType(userChat.getChat().getType().name())
                        .build()));
        return Result.success(data);
    }

    public Result<List<MessageDto>> getMessagesByChatId(Long userId, Long chatId) {
        log.debug("getMessagesByChatId userId={}, chatId={}", userId, chatId);

        List<MessageDto> data = new ArrayList<>();
        Optional<Chat> foundChat = chatRepository.findById(chatId);
        if (foundChat.isEmpty()) {
            return Result.failure(ErrorCode.CHAT_NOT_FOUND);
        }

        boolean isMember = foundChat.get().getUserChats().stream()
                .anyMatch(userChat -> userChat.getUserId().equals(userId));
        if (!isMember) {
            return Result.failure(ErrorCode.CHAT_NOT_MEMBER);
        }

        foundChat.get().getMessages().stream()
                .forEach(message -> data.add(MessageDto.builder()
                        .chatId(chatId)
                        .senderId(message.getUserId())
                        .text(message.getText())
                        .sentAt(message.getCreatedAt())
                        .build()));
        return Result.success(data);
    }

    @Transactional
    public Result<ChatDto> makeGroupChat(Long userId, String title) {
        log.debug("makeGroupChat userId={}, title={}", userId, title);

        Chat saved = chatRepository.save(Chat.createGroupChat(title, null));
        userChatRepository.save(UserChat.create(saved, userId, false, ""));
        return Result.success(ChatDto.builder()
                .chatId(saved.getId())
                .title(saved.getTitle())
                .chatType(saved.getType().name())
                .build());
    }

    @Transactional
    public Result<ChatDto> makeDirectChat(Long fromUserId, Long toUserId) {
        log.debug("makeDirectChat fromUserId={}, toUserId={}", fromUserId, toUserId);

        Set<Long> fromChatIdSet = new HashSet<>(userChatRepository.findByUserId(fromUserId).stream()
                .map(uc -> uc.getChat().getId())
                .toList());
        Set<Long> toChatIdSet = new HashSet<>(userChatRepository.findByUserId(toUserId).stream()
                .map(uc -> uc.getChat().getId())
                .toList());
        List<Long> commonChatIds = fromChatIdSet.stream()
                .filter(chatId -> toChatIdSet.contains(chatId))
                .toList();

        boolean existsDM = chatRepository.findByIdIn(commonChatIds).stream()
                .anyMatch(chat -> chat.getType().equals(ChatType.DIRECT));
        if (existsDM) {
            return Result.failure(ErrorCode.CHAT_ALREADY_EXISTS_DIRECT);
        }

        Chat saved = chatRepository.save(Chat.createDirectChat());
        userChatRepository.save(UserChat.create(saved, fromUserId, false, ""));
        userChatRepository.save(UserChat.create(saved, toUserId, false, ""));
        return Result.success(ChatDto.builder()
                .chatId(saved.getId())
                .title(saved.getTitle())
                .chatType(saved.getType().name())
                .build());
    }

    @Transactional
    public Result<ChatDto> enterGroupChat(Long userId, Long chatId) {
        log.debug("enterGroupChat chatId={}, userId={}", chatId, userId);

        Optional<Chat> found = chatRepository.findById(chatId);
        if (found.isEmpty()) {
            return Result.failure(ErrorCode.CHAT_NOT_FOUND);
        }

        Chat chat = found.get();
        boolean alreadyMember = chat.getUserChats().stream()
                .anyMatch(userChat -> userChat.getUserId().equals(userId));
        if (alreadyMember) {
            return Result.failure(ErrorCode.CHAT_ALREADY_GROUP_MEMBER);
        }

        userChatRepository.save(UserChat.create(chat, userId, false, ""));
        return Result.success(ChatDto.builder()
                .chatId(chat.getId())
                .title(chat.getTitle())
                .chatType(chat.getType().name())
                .build());
    }

    @Transactional
    public Result<ChatDto> leaveGroupChat(Long userId, Long chatId) {
        log.debug("leaveGroupChat chatId={}, userId={}", chatId, userId);

        Optional<Chat> foundChat = chatRepository.findById(chatId);
        if (foundChat.isEmpty()) {
            return Result.failure(ErrorCode.CHAT_NOT_FOUND);
        }
        Chat chat = foundChat.get();
        if (chat.getType().equals(ChatType.DIRECT)) {
            return Result.failure(ErrorCode.CHAT_TYPE_DIRECT);
        }

        Optional<UserChat> foundUserChat = userChatRepository.findByChatIdAndUserId(chatId, userId);
        if (foundUserChat.isEmpty()) {
            return Result.failure(ErrorCode.CHAT_NOT_MEMBER);
        }
        userChatRepository.delete(foundUserChat.get());
        return Result.success(ChatDto.builder()
                .chatId(chat.getId())
                .build());
    }
}
