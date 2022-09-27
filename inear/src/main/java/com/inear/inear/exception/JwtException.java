package com.inear.inear.exception;

import javax.management.RuntimeErrorException;

public class JwtException extends RuntimeErrorException {

    public JwtException(Error e) {
        super(e);
    }

    public JwtException(Error e, String message) {
        super(e, message);
    }
}
