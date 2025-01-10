package com.exdev.redis_impl.provider;

import com.exdev.redis_impl.interfaces.RedisCacheInterface;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class RedisCacheImplProvider {
    public static Map<String, RedisCacheInterface> serviceMap = new HashMap<>();

    @Autowired
    private List<? extends RedisCacheInterface> redisCacheImpls;

    public static RedisCacheInterface findByCacheName(String cacheName) {
        RedisCacheInterface service = serviceMap.getOrDefault(cacheName, null);
        if (Objects.isNull(service)) {
            throw new RuntimeException("Invalid cacheName " + cacheName+", for RedisCacheImpl Provider");
        }
        return service;
    }

    @PostConstruct
    private void constructServiceProviderMap() {
        redisCacheImpls.forEach(service -> serviceMap.put(service.getCacheName(), service));
    }
}
