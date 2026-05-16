package com.restaurant.menu_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private T data;
    private boolean success;

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    private String message;
    private LocalDateTime timestamp;
    private HttpStatus status;
}

