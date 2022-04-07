package com.ptrkalm.cdq.api.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class RestResponse {
    private int status;
    private Object result;
}
