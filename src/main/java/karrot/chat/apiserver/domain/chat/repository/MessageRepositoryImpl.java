package karrot.chat.apiserver.domain.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import karrot.chat.apiserver.domain.chat.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static karrot.chat.apiserver.domain.chat.entity.QMessage.message;

@Repository
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public MessageRepositoryImpl(
            @Qualifier("chatEntityManager") EntityManager em,
            @Qualifier("chatQueryFactory") JPAQueryFactory queryFactory
    ) {
        this.em = em;
        this.queryFactory = queryFactory;
    }

    public List<Message> findAllByChatId(Long chatId) {
        return findAllByChatId(chatId, 300);
    }

    public List<Message> findAllByChatId(Long chatId, long limit) {
        return queryFactory
                .selectFrom(message)
                .where(message.chat.id.eq(chatId))
                .orderBy(message.createdAt.desc())
                .limit(limit)
                .fetch();
    }
}
