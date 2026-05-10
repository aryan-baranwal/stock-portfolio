package com.portfolio.alert.exception;

public class AlertNotFoundException extends RuntimeException {

    public AlertNotFoundException(Long id) {
        super("Alert not found with id: " + id);
    }

    public AlertNotFoundException(String message) {
        super(message);
    }
}