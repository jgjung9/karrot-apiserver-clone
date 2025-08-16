package karrot.chat.apiserver.domain.chat.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    private Long userId;

    private String text;

    private LocalDateTime createdAt;

    public static Message createMessage(Chat chat, Long userId, String text) {
        Message message = new Message();
        message.chat = chat;
        message.userId = userId;
        message.text = text;
        message.createdAt = LocalDateTime.now();
        chat.getMessages().add(message);
        return message;
    }
}
