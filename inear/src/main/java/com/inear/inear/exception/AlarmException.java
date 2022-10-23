package com.inear.inear.exception;

import javax.management.RuntimeErrorException;

public class AlarmException extends RuntimeException {
    public AlarmException(String message) {
        super(message);
    }

    public AlarmException(String message,Throwable cause) {
        super(message,cause);
    }
}
