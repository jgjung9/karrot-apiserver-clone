package karrot.chat.apiserver.domain.chat.repository;

import karrot.chat.apiserver.domain.chat.entity.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserChatRepository extends JpaRepository<UserChat, Long>, UserChatRepositoryCustom {
    List<UserChat> findByUserId(Long userId);
    List<UserChat> findByChatId(Long chatId);

    Optional<UserChat> findByChatIdAndUserId(Long chatId, Long userId);

}
