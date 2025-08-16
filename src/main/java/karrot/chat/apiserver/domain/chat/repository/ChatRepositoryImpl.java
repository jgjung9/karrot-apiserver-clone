package karrot.chat.apiserver.domain.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;



@Repository
public class ChatRepositoryImpl implements ChatRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ChatRepositoryImpl(
            @Qualifier("chatEntityManager") EntityManager em,
            @Qualifier("chatQueryFactory") JPAQueryFactory queryFactory
    ) {
        this.em = em;
        this.queryFactory = queryFactory;
    }
}
