package com.exdev.redis_impl.impls;

import com.exdev.redis_impl.constants.CacheConstants;
import com.exdev.redis_impl.response.UserMiniTimelineResponse;
import com.exdev.redis_impl.interfaces.RedisCacheInterface;
import com.exdev.redis_impl.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class UserMiniTimelineRedisCacheImpl implements RedisCacheInterface<UserMiniTimelineResponse> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private BusinessService businessService;

    @Override
    public String getCacheName() {
        return CacheConstants.USER_MINI_TIMELINE_CACHE;
    }

    public Object fetchCachedData(String cacheName, String cacheKey) {
        // Construct the full Redis key
        String fullKey = cacheName + "::" + cacheKey;
        log.info("Cache size with key: {}",redisTemplate.opsForValue().size(fullKey));
        // Fetch the cached value
        return redisTemplate.opsForValue().get(fullKey);
    }

    @CacheEvict(value = CacheConstants.USER_MINI_TIMELINE_CACHE, key = "#key")
    public void clearByCacheKey(String key) {
        log.info("Cleared {} cache!",getCacheName());
    }

    @CacheEvict(value = CacheConstants.USER_MINI_TIMELINE_CACHE, allEntries = true)
    public void clearAll() {
    }


    public UserMiniTimelineResponse updateCacheWrapper(String key) {
        UserMiniTimelineResponse userMiniTimelineResponse = businessService.generateUserMiniTimelineResponse(key);
        // Here, we update the cache with the new data
        return updateCache(key,userMiniTimelineResponse); // The return value is automatically cached in Redis
    }

    @CachePut(value = CacheConstants.USER_MINI_TIMELINE_CACHE, key = "#key")
    public UserMiniTimelineResponse updateCache(String key, UserMiniTimelineResponse newData) {
        return newData; // The return value is automatically cached in Redis
    }
}

