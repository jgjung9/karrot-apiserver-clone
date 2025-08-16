package karrot.chat.apiserver.domain.chat.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @Nullable
    private String title;

    @Nullable
    private Long thumbnailId;

    @Enumerated(value = EnumType.STRING)
    private ChatType type;

    @ToString.Exclude
    @OneToMany(mappedBy = "chat")
    private List<Message> messages = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "chat")
    private List<UserChat> userChats = new ArrayList<>();

    public static Chat createGroupChat(String title, Long thumbnailId) {
        Chat chat = new Chat();
        chat.title = title;
        chat.thumbnailId = thumbnailId;
        chat.type = ChatType.GROUP;
        return chat;
    }

    public static Chat createDirectChat() {
        Chat chat = new Chat();
        chat.type = ChatType.DIRECT;
        return chat;
    }

    public void changeTitleAndThumbnail(String title, Long thumbnailId) {
        this.title = title;
        this.thumbnailId = thumbnailId;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeThumbnail(Long thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public void addUserChat(UserChat userChat) {
        this.userChats.add(userChat);
    }
}
