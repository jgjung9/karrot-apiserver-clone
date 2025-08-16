package karrot.chat.apiserver.domain.chat.service;

import karrot.chat.apiserver.common.Result;
import karrot.chat.apiserver.domain.chat.dto.ChatDto;
import karrot.chat.apiserver.domain.chat.dto.MessageDto;
import karrot.chat.apiserver.domain.chat.entity.Chat;
import karrot.chat.apiserver.domain.chat.entity.ChatType;
import karrot.chat.apiserver.domain.chat.entity.Message;
import karrot.chat.apiserver.domain.chat.entity.UserChat;
import karrot.chat.apiserver.domain.chat.repository.ChatRepository;
import karrot.chat.apiserver.domain.chat.repository.MessageRepository;
import karrot.chat.apiserver.domain.chat.repository.UserChatRepository;
import karrot.chat.apiserver.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional("chatTransactionManager")
class ChatServiceTest {

    @Autowired
    ChatService chatService;
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    UserChatRepository userChatRepository;

    private Long USER_ID_1 = 1L;
    private Long USER_ID_2 = 2L;
    private Long USER_ID_3 = 3L;
    private Long USER_ID_4 = 4L;
    private Long DIRECT_CHAT_ID;
    private Long GROUP_CHAT_ID;

    @BeforeEach
    void setUp() {
        Chat chat1 = chatRepository.save(Chat.createDirectChat());
        DIRECT_CHAT_ID = chat1.getId();
        userChatRepository.save(UserChat.create(chat1, USER_ID_1, false, ""));
        userChatRepository.save(UserChat.create(chat1, USER_ID_2, false, ""));

        Chat chat2 = chatRepository.save(Chat.createGroupChat("title", null));
        GROUP_CHAT_ID = chat2.getId();
        userChatRepository.save(UserChat.create(chat2, USER_ID_1, false, ""));
        userChatRepository.save(UserChat.create(chat2, USER_ID_2, false, ""));
        userChatRepository.save(UserChat.create(chat2, USER_ID_3, false, ""));
        messageRepository.save(Message.createMessage(chat2, USER_ID_1, "user1 message"));
        messageRepository.save(Message.createMessage(chat2, USER_ID_2, "user2 message"));
        messageRepository.save(Message.createMessage(chat2, USER_ID_3, "user3 message"));
    }

    @Test
    @DisplayName("유저 개인 메시지 방 생성")
    void makeDirectChat() {
        Result<ChatDto> result = chatService.makeDirectChat(USER_ID_1, USER_ID_3);
        Chat chat = chatRepository.findById(result.getData().getChatId()).get();

        assertThat(userChatRepository.findByChatId(chat.getId()).size()).isEqualTo(2);
        assertThat(userChatRepository.findByChatIdAndUserId(chat.getId(), USER_ID_1).isPresent()).isTrue();
        assertThat(userChatRepository.findByChatIdAndUserId(chat.getId(), USER_ID_3).isPresent()).isTrue();
        assertThat(chat.getType()).isEqualTo(ChatType.DIRECT);
    }
    
    @Test
    @DisplayName("이미 개인 메시지방이 존재할 경우 생성되지 않는다.")
    void shouldNotMakeDMAlreadyExist() throws Exception {
        Result<ChatDto> result = chatService.makeDirectChat(USER_ID_1, USER_ID_2);

        assertThat(ErrorCode.ofCode(result.getErrorCode())).isEqualTo(ErrorCode.CHAT_ALREADY_EXISTS_DIRECT);
    }

    @Test
    @DisplayName("유저 그룹 메시지 방 생성")
    void makeGroupChat() throws Exception {
        // given
        String testTitle = "test group chat";
        Result<ChatDto> result = chatService.makeGroupChat(USER_ID_1, testTitle);

        // when
        Chat chat = chatRepository.findById(result.getData().getChatId()).get();

        // then
        assertThat(chat.getType()).isEqualTo(ChatType.GROUP);
        assertThat(chat.getTitle()).isEqualTo(testTitle);
    }

    @Test
    @DisplayName("유저 그룹 메시지 방 입장")
    void enterGroupChat() throws Exception {
        Result<ChatDto> result = chatService.enterGroupChat(USER_ID_4, GROUP_CHAT_ID);
        assertThat(result.getData().getChatId()).isEqualTo(GROUP_CHAT_ID);
        assertThat(userChatRepository.findByChatIdAndUserId(GROUP_CHAT_ID, USER_ID_4).isPresent()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 그룹 채팅에 입장 할 수 없다")
    void shouldNotEnterIfNotExistsChat() throws Exception {
        Result<ChatDto> result = chatService.enterGroupChat(USER_ID_1, 10000L);
        assertThat(ErrorCode.ofCode(result.getErrorCode())).isEqualTo(ErrorCode.CHAT_NOT_FOUND);
    }

    @Test
    @DisplayName("이미 입장해 있는 그룹 채팅에는 입장할 수 없다")
    void shouldNotEnterIfAlreadyMember() throws Exception {
        Result<ChatDto> result = chatService.enterGroupChat(USER_ID_1, GROUP_CHAT_ID);
        assertThat(ErrorCode.ofCode(result.getErrorCode())).isEqualTo(ErrorCode.CHAT_ALREADY_GROUP_MEMBER);
    }

    @Test
    @DisplayName("유저의 채팅방 목록 가져오기")
    void getChatsByUserId() throws Exception {
        Result<List<ChatDto>> result = chatService.getChatsByUserId(USER_ID_1);

        assertThat(result.getData().size()).isEqualTo(2);
        for (ChatDto dto : result.getData()) {
            Chat chat = chatRepository.findById(dto.getChatId()).get();
            assertThat(chat.getId()).isEqualTo(dto.getChatId());
            assertThat(chat.getTitle()).isEqualTo(dto.getTitle());
            assertThat(chat.getType().name()).isEqualTo(dto.getChatType());
        }
    }

    @Test
    @DisplayName("그룹 채팅방 나가기")
    void leaveGroupChat() throws Exception {
        Result<ChatDto> result = chatService.leaveGroupChat(USER_ID_1, GROUP_CHAT_ID);
        List<UserChat> userChats = userChatRepository.findByChatId(GROUP_CHAT_ID);
        assertThat(result.getData().getChatId()).isEqualTo(GROUP_CHAT_ID);
        assertThat(userChats.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("존재 하지 않는 채팅방에서 나갈 수 없다")
    void shouldNotLeaveIfNotExist() throws Exception {
        Result<ChatDto> result = chatService.leaveGroupChat(USER_ID_1, 100000L);
        assertThat(ErrorCode.ofCode(result.getErrorCode())).isEqualTo(ErrorCode.CHAT_NOT_FOUND);
    }

    @Test
    @DisplayName("개인 채팅방을 나갈 수 없다.")
    void shouldNotLeaveDirectChat() throws Exception {
        Result<ChatDto> result = chatService.leaveGroupChat(USER_ID_1, DIRECT_CHAT_ID);
        assertThat(ErrorCode.ofCode(result.getErrorCode())).isEqualTo(ErrorCode.CHAT_TYPE_DIRECT);
    }

    @Test
    @DisplayName("속해 있지 않은 채팅방에서 나갈 수 없다")
    void shouldNotLeaveIfNotMember() throws Exception {
        Result<ChatDto> result = chatService.leaveGroupChat(USER_ID_4, GROUP_CHAT_ID);
        assertThat(ErrorCode.ofCode(result.getErrorCode())).isEqualTo(ErrorCode.CHAT_NOT_GROUP_MEMBER);
    }

    @Test
    @DisplayName("메시지 목록 가져오기")
    void getAllMessagesByChatId() throws Exception {
        Result<List<MessageDto>> result = chatService.getMessagesByChatId(GROUP_CHAT_ID);
        assertThat(result.getData().size()).isEqualTo(3);
        result.getData().stream().forEach(m -> System.out.println(m));
    }
}