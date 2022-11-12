package com.inear.inear.utils;

import lombok.Getter;

@Getter
public enum Status {
    OK("200"),
    BAD_REQUEST("400"),
    NOT_FOUND("404"),
    INTERNAL_SERVER_ERROR("500");

    String statusCode;

    Status(String statusCode) {
        this.statusCode = statusCode;
    }
}
