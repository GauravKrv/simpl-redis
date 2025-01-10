package com.exdev.redis_impl.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RedisCacheRefreshRequest {
    private String cacheKey;
    private String cacheName;
}
