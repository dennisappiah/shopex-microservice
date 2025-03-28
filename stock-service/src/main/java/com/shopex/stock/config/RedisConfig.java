package com.shopex.stock.config;

import com.shopex.stock.dto.PriceUpdate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, PriceUpdate> reactivePriceUpdateTemplate(
            ReactiveRedisConnectionFactory factory) {

        Jackson2JsonRedisSerializer<PriceUpdate> serializer =
                new Jackson2JsonRedisSerializer<>(PriceUpdate.class);

        RedisSerializationContext<String, PriceUpdate> context =
                RedisSerializationContext.<String, PriceUpdate>newSerializationContext()
                        .key(new StringRedisSerializer())
                        .value(serializer)
                        .hashKey(new StringRedisSerializer())
                        .hashValue(serializer)
                        .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

}