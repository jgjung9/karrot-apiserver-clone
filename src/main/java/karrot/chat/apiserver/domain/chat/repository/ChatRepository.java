package karrot.chat.apiserver.domain.chat.repository;

import karrot.chat.apiserver.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {

    List<Chat> findByIdIn(List<Long> ids);
}
