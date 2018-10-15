package com.hydra.common.httpclient;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;

public abstract class TextCallback<T> extends OKCallback<T> {

    private final Charset charset;

    public TextCallback() {
        this(Consts.UTF_8);
    }

    public TextCallback(Charset charset) {
        this.charset = charset;
    }

    protected abstract T handleResponse(String response) throws HttpAccessException, CallbackWrapException;

    @Override
    protected final T onOKStatus(HttpResponse response) throws HttpAccessException, CallbackWrapException {
        String text;
        try {
            text = EntityUtils.toString(response.getEntity(), charset);
        } catch (Exception ex) {
            throw new HttpAccessException("Error occur on getting response text!", ex);
        }
        return handleResponse(text);
    }
}

