package com.exdev.redis_impl.interfaces;


public interface RedisCacheInterface<T> {
    String getCacheName();
    Object fetchCachedData(String cacheName, String cacheKey);
    void clearByCacheKey(String cacheKey);
    void clearAll();
    T updateCacheWrapper(String cacheKey) throws Exception;
    T updateCache(String cacheKey, T newData) throws Exception ;
}
