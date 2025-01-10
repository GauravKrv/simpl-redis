package com.exdev.redis_impl.service;

import com.exdev.redis_impl.response.UserMiniTimelineResponse;
import com.exdev.redis_impl.response.UserTimelineResponse;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {
    public UserMiniTimelineResponse generateUserMiniTimelineResponse(String userId) {
        return UserMiniTimelineResponse.builder().build();
    }

    public UserTimelineResponse buildUserTimeline(String userId) {
        return UserTimelineResponse.builder().build();
    }
}
