package karrot.chat.apiserver.domain.account.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String nickname;


    // 생성 메서드
    public static Account createAccount(String username, String password, String nickname) {
        Account account = new Account();
        account.username = username;
        account.password = password;
        account.nickname = nickname;
        return account;
    }
}
