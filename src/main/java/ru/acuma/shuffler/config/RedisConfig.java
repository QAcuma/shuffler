package ru.acuma.shuffler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import ru.acuma.shuffler.model.entity.TgEvent;

import java.util.Map;

@Configuration
public class RedisConfig {

    public static final String EVENT_STORAGE = "event-storage";

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());

        return template;
    }

    @Bean
    public Map<Long, TgEvent> eventStorage(final RedisTemplate<String, Object> redisTemplate) {
        var pagePointers = redisTemplate.<Long, TgEvent>boundHashOps(EVENT_STORAGE);

        return pagePointers.entries();
    }



}
