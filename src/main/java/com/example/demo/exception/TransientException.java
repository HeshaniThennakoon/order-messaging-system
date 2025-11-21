package com.example.demo.exception;

public class TransientException extends RuntimeException {
    public TransientException(String msg) {
        super(msg);
    }
}
