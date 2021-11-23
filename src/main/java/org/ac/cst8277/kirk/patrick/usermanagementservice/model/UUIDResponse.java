package org.ac.cst8277.kirk.patrick.usermanagementservice.model;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public class UUIDResponse {
    private HttpStatus httpStatus;
    private String message;
    private List<UUID> data;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public List<UUID> getData() {
        return data;
    }
}
