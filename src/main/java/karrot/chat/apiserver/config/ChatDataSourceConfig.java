package karrot.chat.apiserver.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "karrot.chat.apiserver.domain.chat.repository",
        entityManagerFactoryRef = "chatEntityManager",
        transactionManagerRef = "chatTransactionManager"
)
public class ChatDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.chat")
    public DataSourceProperties chatDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource chatDataSource() {
        return chatDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean chatEntityManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("chatDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("karrot.chat.apiserver.domain.chat.entity")
                .persistenceUnit("chat")
                .build();
    }

    @Bean
    public PlatformTransactionManager chatTransactionManager(
            @Qualifier("chatEntityManager") EntityManagerFactory emf
    ) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public JPAQueryFactory chatQueryFactory(
            @Qualifier("chatEntityManager") EntityManager chatEntityManager
    ) {
        return new JPAQueryFactory(chatEntityManager);
    }
}