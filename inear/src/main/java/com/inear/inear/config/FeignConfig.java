package com.inear.inear.config;

import com.inear.inear.exception.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignConfig implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        return new FeignException("feign 에러");
    }
}
