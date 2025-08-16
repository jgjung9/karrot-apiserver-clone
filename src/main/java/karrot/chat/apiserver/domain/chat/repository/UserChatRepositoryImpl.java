package karrot.chat.apiserver.domain.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import karrot.chat.apiserver.domain.chat.entity.UserChat;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

import static karrot.chat.apiserver.domain.chat.entity.QUserChat.userChat;


@Repository
public class UserChatRepositoryImpl implements UserChatRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public UserChatRepositoryImpl(
            @Qualifier("chatEntityManager") EntityManager em,
            @Qualifier("chatQueryFactory") JPAQueryFactory queryFactory
    ) {
        this.em = em;
        this.queryFactory = queryFactory;
    }
}
