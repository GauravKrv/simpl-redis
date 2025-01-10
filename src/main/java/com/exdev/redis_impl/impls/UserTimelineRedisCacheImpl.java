package com.exdev.redis_impl.impls;

import com.exdev.redis_impl.constants.CacheConstants;
import com.exdev.redis_impl.response.UserTimelineResponse;
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
public class UserTimelineRedisCacheImpl implements RedisCacheInterface<UserTimelineResponse> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private BusinessService businessService;

    @Override
    public String getCacheName() {
        return CacheConstants.USER_TIMELINE_CACHE;
    }

    public Object fetchCachedData(String cacheName, String cacheKey) {
        // Construct the full Redis key
        String fullKey = cacheName + "::" + cacheKey;
        log.info("Cache size with key: {}",redisTemplate.opsForValue().size(fullKey));
        // Fetch the cached value
        return redisTemplate.opsForValue().get(fullKey);
    }

    @CacheEvict(value = CacheConstants.USER_TIMELINE_CACHE, key = "#userId")
    public void clearByCacheKey(String userId) {
        log.info("Cleared {} cache!",getCacheName());
    }

    @CacheEvict(value = CacheConstants.USER_TIMELINE_CACHE, allEntries = true)
    public void clearAll() {
    }


    public UserTimelineResponse updateCacheWrapper(String key) {
        String[] keys = key.split("_");
        UserTimelineResponse userTimelineResponse = businessService.buildUserTimeline(keys[0]);
        // Here, we update the cache with the new data
        return updateCache(key,userTimelineResponse); // The return value is automatically cached in Redis
    }

    @CachePut(value = CacheConstants.USER_TIMELINE_CACHE, key = "#key")
    public UserTimelineResponse updateCache(String key, UserTimelineResponse newData) {
        return newData; // The return value is automatically cached in Redis
    }
}

