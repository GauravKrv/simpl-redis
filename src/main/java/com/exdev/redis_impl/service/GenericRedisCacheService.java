package com.exdev.redis_impl.service;

import com.exdev.redis_impl.request.RedisCacheRefreshRequest;
import com.exdev.redis_impl.interfaces.RedisCacheInterface;
import com.exdev.redis_impl.provider.RedisCacheImplProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
public class GenericRedisCacheService {

    public void evictCache(RedisCacheRefreshRequest redisCacheRefreshRequest) {
        RedisCacheInterface redisCacheImpl = RedisCacheImplProvider.findByCacheName(redisCacheRefreshRequest.getCacheName());
        redisCacheImpl.clearByCacheKey(redisCacheRefreshRequest.getCacheKey());
    }
    public void evictCache(String cacheName, String cacheKey) {
        if (!ObjectUtils.isEmpty(cacheKey)){
            RedisCacheRefreshRequest redisCacheRefreshRequest = RedisCacheRefreshRequest.builder().cacheName(cacheName).cacheKey(cacheKey).build();
            evictCache(redisCacheRefreshRequest);
        }
    }

    public Object fetchCachedData(String cacheName, String cacheKey) {
        RedisCacheInterface redisCacheImpl = RedisCacheImplProvider.findByCacheName(cacheName);
        // Construct the full Redis key
        return redisCacheImpl.fetchCachedData(cacheName,cacheKey);
    }

    public void clearAll(String cacheName) {
        RedisCacheInterface redisCacheImpl = RedisCacheImplProvider.findByCacheName(cacheName);
        redisCacheImpl.clearAll();
    }

    public Object updateCache(String cacheName,String cacheKey){
        try{
            RedisCacheInterface redisCacheImpl = RedisCacheImplProvider.findByCacheName(cacheName);
            return redisCacheImpl.updateCacheWrapper(cacheKey);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }
}
