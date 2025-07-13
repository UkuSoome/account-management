package com.lhv.account.dto;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class Response {

    private Response() {
    }

    public static <T> ResponseEntity<T> ok(T body) {
        return createResponse(HttpStatus.OK, body);
    }

    public static <T> ResponseEntity<T> ok() {
        return createResponse(HttpStatus.OK, null);
    }

    public static <T> ResponseEntity<T> nok(HttpStatus statusCode, T body) {
        return createResponse(statusCode, body);
    }

    private static <T> ResponseEntity<T> createResponse(HttpStatus statusCode, T body) {
        return new ResponseEntity<>(body, createHeaders(), statusCode);
    }

    private static HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}