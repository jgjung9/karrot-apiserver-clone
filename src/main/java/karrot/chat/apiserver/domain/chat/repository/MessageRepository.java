package karrot.chat.apiserver.domain.chat.repository;

import karrot.chat.apiserver.domain.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryCustom {
}
