package com.exdev.redis_impl.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectResponse {
    private Object responseData;
    private int statusCode;
    private String message;
    private String status;
    private String userMessage;
}
