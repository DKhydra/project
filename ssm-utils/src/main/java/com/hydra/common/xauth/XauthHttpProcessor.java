package com.hydra.common.xauth;

import com.rv.common.xauth.XauthHttpKeys.HeaderKeys;
import com.rv.common.xauth.XauthHttpKeys.URLKeys;
import com.rv.common.xauth.XauthHttpKeys.XauthToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * signature签名校验
 *
 */
@Component
public class XauthHttpProcessor {
	private static final Integer defaultTime = 10 * 60 * 1000; // 默认10min

	public boolean validateSign(HttpServletRequest request) throws UnsupportedEncodingException {
        // 先判断参数位置
        boolean isHeader = fetchKeyPos(request);
        String accessId = fetchKey(request, isHeader, URLKeys.URL_ACCESSID, HeaderKeys.HEADER_ACCESSID);
        if (StringUtils.isBlank(accessId)) {
            throw new IllegalArgumentException("accessId is null.");
        }
        String timestamp = fetchKey(request, isHeader, URLKeys.URL_TIMESTAMP, HeaderKeys.HEADER_TIMESTAMP);
        if (StringUtils.isBlank(timestamp) || !isLong(timestamp)) {
            throw new IllegalArgumentException("timestamp is null.");
        }
        String signature = fetchKey(request, isHeader, URLKeys.URL_SIGNATURE, HeaderKeys.HEADER_SIGNATURE);
        if (StringUtils.isBlank(signature)) {
            throw new IllegalArgumentException("signature is null.");
        }
        // 拿到客户端对应的 accessKey
        XauthToken authToken = XauthToken.getXauthTokenByAccessId(accessId);
        if (authToken == null) {
            throw new IllegalArgumentException("accessId is invalid.");
        }
        String accessKey = authToken.getAccessKey();
        // 校验时间戳
        if ((System.currentTimeMillis() - Long.valueOf(timestamp)) > defaultTime) {
            throw new IllegalArgumentException("timestamp is timeout.");
        }

        // 计算签名
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(HeaderKeys.HEADER_ACCESSID, accessId);
        headers.put(HeaderKeys.HEADER_TIMESTAMP, timestamp);
        String method = request.getMethod();
        // 所有值为null的key都不参与签名
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String[]> paramMap = request.getParameterMap();
        for (String param : paramMap.keySet()) {
            // 排除参与签名的固定值
            if(isQueryToSign(param)) {
                for (String v : paramMap.get(param)) {
                    querys.put(param, v);
                    break; // 多个值 取第一个
                }
            }
        }
        String serverSign = XauthSignUtil.sign(accessKey, method, headers, querys);

        if(!StringUtils.equals(signature, serverSign)) {
            throw new RuntimeException("signature invalid.");
        }
        return true;
    }

    private boolean isQueryToSign(String paramName) {
        if (StringUtils.isBlank(paramName)) {
            return false;
        }

        if (StringUtils.equals(paramName, URLKeys.URL_ACCESSID)
            ||StringUtils.equals(paramName, URLKeys.URL_TIMESTAMP)
            ||StringUtils.equals(paramName, URLKeys.URL_SIGNATURE)) {
            return false;
        }
        return true;
    }

    private boolean fetchKeyPos(HttpServletRequest request) {

        // 根据时间戳判断参数位置：优先级 HttpHeader > UrlParam
        String headerVal = request.getHeader(XauthHttpKeys.HeaderKeys.HEADER_TIMESTAMP);
        return StringUtils.isNotBlank(headerVal);
    }


    private String fetchKey(HttpServletRequest request, boolean isHeader, String urlKey, String headerKey) {
        return isHeader ? fetchHeaderKey(request, headerKey) : fetchUrlKey(request, urlKey);
    }


    private String fetchHeaderKey(HttpServletRequest request, String headerKey) {
        return request.getHeader(headerKey);
    }

    private String fetchUrlKey(HttpServletRequest request, String urlKey) {
        return request.getParameter(urlKey);
    }
    private boolean isLong(String str) {
        try {
            Long.parseLong(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
	
}
