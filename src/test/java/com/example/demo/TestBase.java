package com.example.demo;

import com.example.demo.model.BaseEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.threeten.extra.MutableClock;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SuppressWarnings("PMD.ExcessiveImports")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TestBase.CustomClockConfiguration.class, initializers = PostgresInitializer.class)
public abstract class TestBase {

    static final String TABLE_NAME_CATEGORIES = "demo.categories";
    static final String TABLE_NAME_NEWS = "demo.news";
    static final String TABLE_NAME_USERS = "demo.users";
    static final String TABLE_NAME_COMMENTS = "demo.comments";

    static final LocalDateTime BEFORE_MILLENNIUM = LocalDateTime.of(1999, Month.DECEMBER, 31, 23, 59, 59);

    @Autowired
    protected Clock clock;

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected MutableClock mutableClock;

    @LocalServerPort
    protected int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    static Instant getTestInstant() {
        return BEFORE_MILLENNIUM.toInstant(ZoneOffset.UTC);
    }

    @BeforeEach
    void clearDataBase() {
        jdbcTemplate.update("truncate table demo.categories, demo.users, demo.news, demo.comments");
    }

    @AfterEach
    void resetClock() {
        mutableClock.setInstant(getTestInstant());
    }

    protected int getEntriesCount(final String tableName) {
        final String countRowsSql = String.format("SELECT COUNT(*) FROM %s", tableName);
        final Integer count = jdbcTemplate.queryForObject(countRowsSql, Integer.class);
        return count != null ? count : 0;
    }

    protected <T> void assertThatNullableFieldsAreNotPrimitive(final Class<T> entityClass) {
        Arrays.stream(entityClass.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Column.class) &&
                field.getAnnotation(Column.class).nullable()
            )
            .forEach(field -> assertThat(field.getType().isPrimitive())
                .withFailMessage(String.format("In %s field %s is primitive", entityClass.getName(), field.getName()))
                .isFalse());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected <T extends BaseEntity> void assertThatEntityIsCorrect(@Nonnull final Set<T> entities,
                                                                    @Nonnull final JpaRepository<T, Long> repository) {
        assertThat(entities)
            .as("The size of the collection must be greater than or equal to two")
            .hasSizeGreaterThanOrEqualTo(2);

        final List<T> saved = repository.saveAll(entities);
        assertThat(saved)
            .hasSameSizeAs(entities);

        final List<T> result = repository.findAll();
        assertThat(result)
            .hasSameSizeAs(entities);
        result.forEach(c -> assertThatNoException()
            .as("Метод toString не должен генерировать ошибок")
            .isThrownBy(c::toString)); // toString
    }

    @TestConfiguration
    static class CustomClockConfiguration {

        @Bean
        public MutableClock mutableClock() {
            return MutableClock.of(getTestInstant(), ZoneOffset.UTC);
        }

        @Bean
        @Primary
        public Clock fixedClock(@Nonnull final MutableClock mutableClock) {
            return mutableClock;
        }
    }

}
