package ru.acuma.shuffler.config;

import com.github.benmanes.caffeine.jcache.configuration.CaffeineConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.acuma.shuffler.config.properties.CaffeineProperty;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.util.OptionalLong;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String CAFFEINE_CACHING_PROVIDER = "com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider";
    public static final Long FIFTEEN_MINUTES = 900_000_000_000L;
    public static final String GROUP_AUTH = "group_auth";
    public static final String USER_AUTH = "user_auth";
    public static final String SEASON_ID = "season_id";

    @Bean
    public CachingProvider caffeineCachingProvider() {
        return Caching.getCachingProvider(CAFFEINE_CACHING_PROVIDER);
    }

    @Bean
    public JCacheCacheManager cacheManager(final CachingProvider cachingProvider) {
        var cacheManager = cachingProvider.getCacheManager();
        cacheManager.createCache(USER_AUTH, caffeineConfiguration());
        cacheManager.createCache(GROUP_AUTH, caffeineConfiguration());
        cacheManager.createCache(SEASON_ID, caffeineConfiguration());

        return new JCacheCacheManager(cacheManager);
    }

    private CaffeineConfiguration<String, CaffeineProperty> caffeineConfiguration() {
        CaffeineConfiguration<String, CaffeineProperty> caffeineConfiguration = new CaffeineConfiguration<>();
        caffeineConfiguration.setMaximumSize(OptionalLong.of(100L));
        caffeineConfiguration.setExpireAfterWrite(OptionalLong.of(FIFTEEN_MINUTES));
        caffeineConfiguration.setExpireAfterAccess(OptionalLong.of(FIFTEEN_MINUTES));

        return caffeineConfiguration;
    }
}
