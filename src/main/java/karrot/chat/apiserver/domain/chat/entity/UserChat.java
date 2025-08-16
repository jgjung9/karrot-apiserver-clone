package karrot.chat.apiserver.domain.chat.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(
        uniqueConstraints = @UniqueConstraint(name = "uk_user_chat", columnNames = {"chat_id", "user_id"}),
        indexes = @Index(name = "idx_user_chat_user_id", columnList = "user_id")
)
public class UserChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @Column(nullable = false)
    private Long userId;

    private Boolean mute;

    @Column(name = "display_idx")
    private String displayIdx;

    public static UserChat create(Chat chat, Long userId, Boolean mute, String displayIdx) {
        UserChat userChat = new UserChat();
        userChat.chat = chat;
        userChat.userId = userId;
        userChat.mute = mute;
        userChat.displayIdx = displayIdx;
        chat.getUserChats().add(userChat);
        return userChat;
    }
}
