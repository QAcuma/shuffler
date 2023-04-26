package ru.acuma.shuffler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import ru.acuma.shuffler.model.domain.TgEvent;

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

    @Bean("redisEventStorage")
    public Map<Long, TgEvent> redisEventStorage(final RedisTemplate<String, Object> redisTemplate) {
        var pagePointers = redisTemplate.<Long, TgEvent>boundHashOps(EVENT_STORAGE);

        return pagePointers.entries();
    }

    @Bean("redisEventSnapshotStorage")
    public Map<Long, TgEvent> redisEventSnapshotStorage(final RedisTemplate<String, Object> redisTemplate) {
        var pagePointers = redisTemplate.<Long, TgEvent>boundHashOps(EVENT_SNAPSHOT_STORAGE);

        return pagePointers.entries();
    }
}
