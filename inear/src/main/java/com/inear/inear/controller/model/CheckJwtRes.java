package com.inear.inear.controller.model;

import lombok.Data;

@Data
public class CheckJwtRes {

    public CheckJwtRes(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    private boolean isSuccess;
}
