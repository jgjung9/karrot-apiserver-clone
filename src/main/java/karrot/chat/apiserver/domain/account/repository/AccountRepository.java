package karrot.chat.apiserver.domain.account.repository;

import karrot.chat.apiserver.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {

    Optional<Account> findByUsername(String username);
}
