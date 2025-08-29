package br.edu.fiec.MapeamentoDeSaude.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "br.edu.fiec.MapeamentoDeSaude.features.search.repositories", // Pacote dos seus repositories
        entityManagerFactoryRef = "externalEntityManagerFactory",
        transactionManagerRef = "externalTransactionManager"
)
public class DataSourceConfig {

    @Bean
    @Primary // Marca este como o datasource principal, se houver
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "externalDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.external")
    public DataSource externalDataSource() {
        return DataSourceBuilder.create().build();
    }
}