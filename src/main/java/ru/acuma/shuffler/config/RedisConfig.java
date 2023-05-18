package ru.acuma.shuffler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import ru.acuma.shuffler.model.domain.TEvent;

import java.util.Map;

@Configuration
public class RedisConfig {

    public static final String EVENT_STORAGE = "event-storage";
    public static final String EVENT_SNAPSHOT_STORAGE = "event-snapshot-storage";

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        return template;
    }

    @Bean("redisEventSnapshotStorage")
    public Map<Long, TEvent> redisEventSnapshotStorage(final RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.<Long, TEvent>boundHashOps(EVENT_SNAPSHOT_STORAGE).entries();
    }
}
