package com.lhv.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(description = "Error response details")
public class ErrorResponse {

    @Schema(description = "Timestamp of the error", example = "2025-07-14T16:00:00")
    private LocalDateTime timestamp;
    @Schema(description = "HTTP status code", example = "400")
    private int status;
    @Schema(description = "Error reason phrase", example = "Bad Request")
    private String error;
    @Schema(description = "Detailed error message", example = "Account not found/Invalid field")
    private String message;
    @Schema(description = "Request path that caused the error", example = "/api/accounts/123")
    private String path;

    public ErrorResponse(HttpStatus status, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
    }
}