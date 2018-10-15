package com.hydra.common.httpclient;

import org.apache.http.client.methods.*;

public enum HttpMethod {

    GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS, TRACE;

    static HttpUriRequest createHttpRequest(String uri, HttpMethod method) {
        HttpUriRequest request;
        switch (method) {
        case GET:
            request = new HttpGet(uri);
            break;
        case POST:
            request = new HttpPost(uri);
            break;
        case PUT:
            request = new HttpPut(uri);
            break;
        case DELETE:
            request = new HttpDelete(uri);
            break;
        default:
            throw new IllegalArgumentException("Unsupport http method: " + method);
        }
        return request;
    }

}

