package com.inear.inear.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Message {

    private  String status;
    private  String message;
    private  Object data;

}
