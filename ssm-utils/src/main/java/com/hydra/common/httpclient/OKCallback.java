package com.hydra.common.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

public abstract class OKCallback<T> implements ResponseHandler<T> {

    protected abstract T onOKStatus(HttpResponse response) throws HttpAccessException, CallbackWrapException;

    public final T handle(HttpResponse response) throws HttpAccessException, CallbackWrapException {
        int status = response.getStatusLine().getStatusCode();
        if (status != HttpStatus.SC_OK) {
            throw new HttpAccessException(status);
        }
        return onOKStatus(response);
    }

}

