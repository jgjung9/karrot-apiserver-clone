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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "karrot.chat.apiserver.domain.account.repository",
        entityManagerFactoryRef = "accountEntityManager",
        transactionManagerRef = "accountTransactionManager"
)
public class AccountDataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.account")
    public DataSourceProperties accountDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource accountDataSource(
            @Qualifier("accountDataSourceProperties") DataSourceProperties properties
    ) {
        return properties
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean accountEntityManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("accountDataSource") DataSource dataSource
    ) {
        return builder.dataSource(dataSource)
                .packages("karrot.chat.apiserver.domain.account.entity")
                .persistenceUnit("account")
                .build();
    }

    @Bean
    @Primary
    PlatformTransactionManager accountTransactionManager(
            @Qualifier("accountEntityManager") EntityManagerFactory emf
    ) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    @Primary
    public JPAQueryFactory accountQueryFactory(
            @Qualifier("accountEntityManager") EntityManager accountEntityManager
    ) {
        return new JPAQueryFactory(accountEntityManager);
    }
}