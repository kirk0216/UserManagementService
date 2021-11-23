package org.ac.cst8277.kirk.patrick.usermanagementservice.model;

import org.springframework.http.HttpStatus;

public class Response {
    private HttpStatus httpStatus;
    private String message;
    private Object data;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}