package karrot.chat.apiserver.domain.chat.repository;

import karrot.chat.apiserver.domain.chat.entity.Message;

import java.util.List;

public interface MessageRepositoryCustom {

    List<Message> findAllByChatId(Long chatId);
    List<Message> findAllByChatId(Long chatId, long limit);

}
