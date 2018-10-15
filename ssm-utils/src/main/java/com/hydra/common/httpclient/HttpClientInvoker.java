package com.hydra.common.httpclient;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rv.common.xauth.XauthHttpKeys.HeaderKeys;
import com.rv.common.xauth.XauthSignUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;

import javax.net.ssl.SSLContext;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HttpClientInvoker {

    // private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    // private static final String HEAD_ACCEPT_LANGUAGE = "Accept-Language";

	private static String accessId = "xb2b";
	private static String accessKey = "322d628bd7994720a4a488be9b757933";
	
	public void setAccessId(String accessId) {
		HttpClientInvoker.accessId = accessId;
	}
	public void setAccessKey(String accessKey) {
		HttpClientInvoker.accessKey = accessKey;
	}
	
    private static HttpClientFacade hc;

    private static int poolSize = 50;
    private static int socketTimeout = 600000;// 5000;
    private static int connectTimeout = 600000;// 5000;
    private static int connectionRequestTimout = 600000;// 5000;

    public HttpClientInvoker() {
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setConnectionRequestTimout(int connectionRequestTimout) {
        this.connectionRequestTimout = connectionRequestTimout;
    }

    static {
        SSLContext sslContext = null;
		try {
			sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustAnySignedStrategy()).build();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext,
                new AllowAllHostnameVerifier());

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectionRequestTimout)
                .setStaleConnectionCheckEnabled(true).build();

//        CredentialsProvider provider = new BasicCredentialsProvider();
//        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(SecurityCredentials.AUTH.getName(),
//                SecurityCredentials.AUTH.getPasswd());
//        provider.setCredentials(AuthScope.ANY, credentials);
        HttpClient httpClient = HttpClientBuilder.create().setMaxConnTotal(poolSize).setMaxConnPerRoute(poolSize)
                .setDefaultRequestConfig(requestConfig).setSSLSocketFactory(sslSocketFactory).build();
//                .setDefaultCredentialsProvider(provider).build();
        // hc = new HttpClientFacade(new BaseHttpClient(httpClient));
        hc = new HttpClientFacade(httpClient);
    }

    /**
     * 获得语言
     */
    // private String getInvokeLocale() {
    // return null;
    // }

    private static Map<String, String> assembleGetContentType(Map<String, String> header) {
        if (header == null) {
            header = new HashMap<String, String>(1);
        }
        if (!header.containsKey(HTTP.CONTENT_TYPE)) {
            // header.put(HTTP.CONTENT_TYPE,
            // "application/x-www-form-urlencoded; charset=utf-8");
            header.put(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
        }
        return header;
    }

    /**
     * 组装signature
     */
    // private String assembleSignature(String uri, Map<String, String> header)
    // {
    // return null;
    // }

    public <T> T accessGet(String uri, Map<String, String> urlParam, TypeReference<T> type) {
        return accessGet(uri, urlParam, (Map<String, String>) null, type);
    }

    public <T> T accessGet(String uri, Map<String, String> urlParam, Map<String, String> header, TypeReference<T> type) {
        header = assembleGetContentType(header);
        return accessObject(uri, urlParam, HttpMethod.GET, header, null, type);
    }

    public <T> T accessPost(String uri, Object param, TypeReference<T> type) {
        return accessPost(uri, (Map<String, String>) null, param, type);
    }

    public <T> T accessPost(String uri, Map<String, String> header, Object param, TypeReference<T> type) {
        return accessObject(uri, null, HttpMethod.POST, header, param, type);
    }

    public <T> T accessPut(String uri, Object param, TypeReference<T> type) {
        return accessPut(uri, (Map<String, String>) null, param, type);
    }

    public <T> T accessPut(String uri, Map<String, String> header, Object param, TypeReference<T> type) {
        return accessObject(uri, null, HttpMethod.PUT, header, param, type);
    }

    public <T> T accessObject(String uri, Map<String, String> urlParam, HttpMethod method, Map<String, String> header, Object param,
            final TypeReference<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("'type' is required");
        }
        if (header == null) {
            header = new HashMap<String, String>(2);
            header.put(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
        }

        try {

            // FastJson 序列化
            HttpEntity entity = null;
            if (param != null) {
                entity = new StringEntity(JSONObject.toJSONString(param, SerializerFeature.WriteDateUseDateFormat),
                        ContentType.APPLICATION_JSON);
            }

            return hc.access(uri, method, new MyRequestHandler(header, entity, urlParam), new TextCallback<T>() {
                @Override
                protected T handleResponse(String response) throws HttpAccessException, CallbackWrapException {
                    if (StringUtils.isBlank(response)) {
                        return null;
                    }
                    try {
                        // FastJson 反序列化
                        return JSONObject.parseObject(response, type);
                    } catch (Exception ex) {
                        // throw new ApplicationException(ErrCode.SYSTEM_ERROR,
                        // "error occur on parse text: "
                        // + response, ex);
                        throw new RuntimeException(ex);
                    }
                }
            });
        } catch (Exception ex) {
            // throw new ApplicationException(ErrCode.SYSTEM_ERROR,
            // "error occur on call[" + uri
            // + "] with method[" + method + "]", ex);
            throw new RuntimeException(ex);
        }
    }

    private static class MyRequestHandler implements RequestHandler {

        private final Map<String, String> header;
        private final HttpEntity httpEntity;
        private final Map<String, String> urlParam;

        public MyRequestHandler(Map<String, String> header, HttpEntity httpEntity, Map<String, String> urlParam) {
            this.header = header;
            this.httpEntity = httpEntity;
            this.urlParam = urlParam;
        }

        public void handle(HttpUriRequest request) {
            if (!(header == null || header.isEmpty())) {
                for (Entry<String, String> entry : header.entrySet()) {
                    if (StringUtils.isEmpty(entry.getKey())) {
                        continue;
                    }
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }

            if (httpEntity != null && request instanceof HttpEntityEnclosingRequest) {
                ((HttpEntityEnclosingRequest) request).setEntity(httpEntity);
            }
            
            // 设置签名
            try {
		        Map<String, String> headers = XauthSignUtil.buildSignHeader(request.getMethod(), null, urlParam, accessId, accessKey);
		        String signature = headers.get(HeaderKeys.HEADER_SIGNATURE);
		        
		        String url = XauthSignUtil.initUrl(request.getURI().toString(), urlParam);
		        
		        if (urlParam==null || urlParam.isEmpty()) {
		            url = url + "?accessId="+accessId+"&timestamp=" + headers.get(HeaderKeys.HEADER_TIMESTAMP)+"&signature=" + URLEncoder.encode(signature,"UTF-8");
		        } else {
		            url = url + "&accessId="+accessId+"&timestamp=" + headers.get(HeaderKeys.HEADER_TIMESTAMP)+"&signature=" + URLEncoder.encode(signature,"UTF-8");
		        }
		        ((HttpRequestBase)request).setURI(new URI(url));
		     } catch (UnsupportedEncodingException e) {
		        System.out.println("httpclient invoker encode error."+e.getMessage());
		     } catch (URISyntaxException e) {
		    	 System.out.println("httpclient invoker uri syntax error."+ e.getMessage());
		     } catch (Exception e) {
		    	 System.out.println("httpclient invoker occur error."+e.getMessage());
		     }
        }

    }

    private static class TrustAnySignedStrategy implements TrustStrategy {
        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            return true;
        }
    }

}

