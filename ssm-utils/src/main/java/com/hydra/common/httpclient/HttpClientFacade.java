package com.hydra.common.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpClientFacade {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientFacade.class);

    private final HttpClient httpClient;

    public HttpClientFacade(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private HttpResponse executeRequest(HttpUriRequest request) throws HttpAccessException {
        Long start = System.currentTimeMillis();
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);

            logger.info("HTTP request: " + request);
            logger.info("HTTP status code: " + response.getStatusLine().getStatusCode());
            logger.info("HTTP reason: " + response.getStatusLine().getReasonPhrase());
        } catch (Exception ex) {
            throw new HttpAccessException(ex);
        } finally {
            Long end = System.currentTimeMillis();
            logger.info("url:" + request.getURI().toString() + ", cost time:" + (end - start) / 1000 + "s");
        }
        return response;
    }

    private <T> T handleResponse(HttpResponse response, ResponseHandler<T> callback)
            throws HttpAccessException, CallbackWrapException {
        T result = null;
        try {
            if (callback != null) {
                result = callback.handle(response);
            }
        } finally {
            try {
                EntityUtils.consume(response.getEntity());
            } catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return result;
    }

    public <T> T access(String uri, HttpMethod method, final HttpEntity entity, ResponseHandler<T> respHandler)
            throws HttpAccessException, CallbackWrapException {
        return access(uri, method, entity == null ? null : new RequestHandler() {
            public void handle(HttpUriRequest request) {
                if (request instanceof HttpEntityEnclosingRequest) {
                    ((HttpEntityEnclosingRequest) request).setEntity(entity);
                }
            }
        }, respHandler);
    }

    public <T> T access(String uri, HttpMethod method, RequestHandler reqHandler,
            ResponseHandler<T> respHandler) throws HttpAccessException, CallbackWrapException {
        HttpUriRequest request = HttpMethod.createHttpRequest(uri, method);
        if (reqHandler != null) {
            reqHandler.handle(request);
        }
        HttpResponse response = executeRequest(request);
        return handleResponse(response, respHandler);
    }

    public <T> T accessViaGet(String uri, ResponseHandler<T> respHandler) throws HttpAccessException,
            CallbackWrapException {
        return access(uri, HttpMethod.GET, (RequestHandler) null, respHandler);
    }

    public <T> T accessViaPost(String uri, HttpEntity entity, ResponseHandler<T> respHandler)
            throws HttpAccessException, CallbackWrapException {
        return access(uri, HttpMethod.POST, entity, respHandler);
    }

}

