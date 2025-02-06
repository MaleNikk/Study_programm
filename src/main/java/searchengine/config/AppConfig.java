package searchengine.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Bean
    public JdbcTemplate jdbcTemplate() {
        final String driverClassName = "org.postgresql.Driver";
        final String jdbcUrl = "jdbc:postgresql://localhost:5432/storage";
        final String username = "word";
        final String password = "word";
        final DataSource dataSource = DataSourceBuilder
                .create().url(jdbcUrl).username(username).password(password).driverClassName(driverClassName).build();
        return new JdbcTemplate(dataSource);
    }
}
