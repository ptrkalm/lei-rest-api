package com.ptrkalm.cdq.api.responses;

import lombok.Getter;

@Getter
public class RestResponsePaged extends RestResponse {
    private final long totalResults;
    private final long pageNumber;
    private final long pageSize;

    public RestResponsePaged(int status, Object result, long totalResults, long pageNumber, long pageSize) {
        super(status, result);
        this.totalResults = totalResults;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
}
