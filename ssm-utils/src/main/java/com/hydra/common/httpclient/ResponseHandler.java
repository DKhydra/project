package com.hydra.common.httpclient;

import org.apache.http.HttpResponse;

public interface ResponseHandler<T> {

    T handle(HttpResponse response) throws HttpAccessException, CallbackWrapException;

}

