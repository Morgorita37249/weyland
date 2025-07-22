package com.weyland.bishop.exception;

import lombok.Getter;
import java.time.Instant;

public class WeylandErrorResponse {
    @Getter
    private Instant timestamp;
    @Getter
    private int status;
    @Getter
    private String error;
    @Getter
    private String message;
    @Getter
    private String path;

    public WeylandErrorResponse(int status, String error, String message, String path){
        this.timestamp = Instant.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
