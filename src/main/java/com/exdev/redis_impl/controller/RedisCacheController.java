package com.exdev.redis_impl.controller;

import com.exdev.redis_impl.response.ProjectResponse;
import com.exdev.redis_impl.request.RedisCacheRefreshRequest;
import com.exdev.redis_impl.service.GenericRedisCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cache")
@Slf4j
public class RedisCacheController {
    @Autowired
    private GenericRedisCacheService genericRedisCacheService;

    //generic request for evicting any cache
    @DeleteMapping("/request/evictCache")
    public ProjectResponse evictRedisCache(@RequestBody RedisCacheRefreshRequest redisCacheRefreshRequest) {
        genericRedisCacheService.evictCache(redisCacheRefreshRequest);
        return ProjectResponse.builder().status(HttpStatus.OK.name()).build();
    }
    //for internal use
    @DeleteMapping("/clearAll")
    public ResponseEntity<String> clearAll(@RequestParam(value = "cacheName",required = false) String cacheName) {
        genericRedisCacheService.clearAll(cacheName);
        return ResponseEntity.ok("Cache cleared for "+cacheName);
    }

    //for internal use
    @GetMapping("/{cacheName}/{cacheKey}")
    public ResponseEntity<?> fetchCachedData(@PathVariable("cacheName") String cacheName,@PathVariable("cacheKey") String cacheKey) {
        return ResponseEntity.ok(genericRedisCacheService.fetchCachedData(cacheName,cacheKey));
    }
    //for internal use
    @PutMapping("/updateCache/{cacheName}/{cacheKey}")
    public ResponseEntity<Object> updateCache(@PathVariable("cacheName") String cacheName,@PathVariable("cacheKey") String cacheKey)  {
        return ResponseEntity.ok(genericRedisCacheService.updateCache(cacheName,cacheKey));
    }
}
