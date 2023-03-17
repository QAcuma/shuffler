package ru.acuma.shuffler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import ru.acuma.shuffler.model.domain.TgEvent;

import java.util.Map;

@Configuration
public class RedisConfig {

    public static final String EVENT_STORAGE = "event-storage";

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        return template;
    }

    @Bean
    public Map<Long, TgEvent> eventStorage(final RedisTemplate<String, Object> redisTemplate) {
        var pagePointers = redisTemplate.<Long, TgEvent>boundHashOps(EVENT_STORAGE);

        return pagePointers.entries();
    }
}
