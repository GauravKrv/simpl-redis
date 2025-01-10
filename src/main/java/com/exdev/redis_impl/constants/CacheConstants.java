package com.exdev.redis_impl.constants;

import java.util.Arrays;
import java.util.List;

public class CacheConstants {
    public static final String USER_TIMELINE_CACHE = "user_timeline";
    public static final String USER_MINI_TIMELINE_CACHE = "user_mini_timeline";
    public static final List<String> THIRTY_MINUTES_CACHE_CONFIGS = Arrays.asList();//add your cache key, for initializing cache with 30 mins refresh time-period
    public static final List<String> ONE_HOUR_CACHE_CONFIGS = Arrays.asList();//add your cache key, for initializing cache with one day refresh time-period
    public static final List<String> ONE_DAY_CACHE_CONFIGS = Arrays.asList(USER_TIMELINE_CACHE,USER_MINI_TIMELINE_CACHE);//add your cache key, for initializing cache with one hour refresh time-period
}
