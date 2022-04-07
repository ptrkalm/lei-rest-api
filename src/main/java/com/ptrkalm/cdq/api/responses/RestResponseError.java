package com.ptrkalm.cdq.api.responses;

import lombok.Getter;

@Getter
public class RestResponseError extends RestResponse {
    private final String errorMassage;

    public RestResponseError(int status, Object result, String errorMassage) {
        super(status, result);
        this.errorMassage = errorMassage;
    }
}
