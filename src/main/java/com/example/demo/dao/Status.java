package com.example.demo.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Status {
    String status;
    String message;
    String isRetry;

    public Status(String status, String message, String isRetry) {
        this.status = status;
        this.message = message;
        this.isRetry = isRetry;
    }
}
