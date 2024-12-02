package testdatabase;
import com.creditapp.config.DatabaseConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DatabaseConfigTest {

    @InjectMocks
    private DatabaseConfig databaseConfig;

    @Test
    void testDataSourceConfiguration() {
        // Set up test properties
        ReflectionTestUtils.setField(databaseConfig, "dbUrl", "jdbc:postgresql://localhost:5433/banking\n");
        ReflectionTestUtils.setField(databaseConfig, "username", "postgres");
        ReflectionTestUtils.setField(databaseConfig, "password", "28488476");
        ReflectionTestUtils.setField(databaseConfig, "driverClassName", "org.postgresql.Driver");

        // Call the dataSource method
        DataSource dataSource = databaseConfig.dataSource();

        // Assertions
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof HikariDataSource);

        HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
        assertEquals("jdbc:postgresql://localhost:5433/banking", hikariDataSource.getJdbcUrl());
        assertEquals("postgres", hikariDataSource.getUsername());
        assertEquals("org.postgresql.Driver", hikariDataSource.getDriverClassName());

        assertEquals(10, hikariDataSource.getMaximumPoolSize());
        assertEquals(5, hikariDataSource.getMinimumIdle());
        assertEquals(30000, hikariDataSource.getConnectionTimeout());
        assertEquals(600000, hikariDataSource.getIdleTimeout());
        assertEquals(1800000, hikariDataSource.getMaxLifetime());
        assertEquals("BankingHikariPool", hikariDataSource.getPoolName());
    }

    @Test
    void testJdbcTemplateCreation() {
        DataSource mockDataSource = mock(DataSource.class);

        JdbcTemplate jdbcTemplate = databaseConfig.jdbcTemplate(mockDataSource);

        assertNotNull(jdbcTemplate);
        assertEquals(mockDataSource, jdbcTemplate.getDataSource());
    }

    @Test
    void testTransactionManagerCreation() {
        DataSource mockDataSource = mock(DataSource.class);

        DataSourceTransactionManager transactionManager =
                (DataSourceTransactionManager) databaseConfig.transactionManager(mockDataSource);

        assertNotNull(transactionManager);
        assertEquals(mockDataSource, transactionManager.getDataSource());
    }
}