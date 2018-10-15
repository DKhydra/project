package com.hydra.common.xauth;

import com.hydra.common.xauth.XauthHttpKeys.HeaderKeys;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 客户端签名工具
 * 工具类本身已经保证了待签名参数的有序
 */
public class XauthSignUtil {
	public static Map<String, String> buildSignHeader(String method, Map<String, String> headers,
			Map<String, String> querys, String appKey, String appSecret) throws Exception {
		
		headers = initialBasicHeader(method, headers, querys, appKey, appSecret);

		return headers;
	}
	
	 /**
     * 初始化基础Header
     * @param method
     * @param headers
     * @param querys
     * @param appKey
     * @param appSecret
     * @return
     * @throws MalformedURLException
     */
    private static Map<String, String> initialBasicHeader(String method,
                                                          Map<String, String> headers, 
                                                          Map<String, String> querys,
                                                          String appKey, String appSecret)
            throws MalformedURLException {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }

        headers.put(HeaderKeys.HEADER_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        headers.put(HeaderKeys.HEADER_ACCESSID, appKey);
        headers.put(HeaderKeys.HEADER_SIGNATURE, sign(appSecret, method, headers, querys));

        return headers;
    }
	
    /**
     * 计算签名
     *
     * @param secret APP密钥
     * @param method HttpMethod
     * @param headers
     * @param querys
     * @return 签名后的字符串
     */
    public static String sign(String secret, String method, 
    							Map<String, String> headers, 
    							Map<String, String> querys) {
        try {
            Mac hmacSha256 = Mac.getInstance(XauthSignConstants.HMAC_SHA256);
            byte[] keyBytes = secret.getBytes(XauthSignConstants.ENCODING);
            hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, XauthSignConstants.HMAC_SHA256));

            return new String(Base64.encodeBase64(
                    hmacSha256.doFinal(buildStringToSign(method, headers, querys)
                            .getBytes(XauthSignConstants.ENCODING))),
                    XauthSignConstants.ENCODING);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建待签名字符串
     * @param method
     * @param headers
     * @param querys
     * @return
     */
    private static String buildStringToSign(String method, 
    										Map<String, String> headers, 
    										Map<String, String> querys) {
        StringBuilder sb = new StringBuilder();

        sb.append(method.toUpperCase()).append(XauthSignConstants.LF);
        sb.append(XauthSignConstants.LF);
        sb.append(buildHeaders(headers));
        sb.append(buildResource(querys));
        
        return sb.toString();
    }

    /**
     * 构建待签名Query
     *
     * @param querys
     * @return 待签名
     */
    private static String buildResource(Map<String, String> querys) {
    	StringBuilder sb = new StringBuilder();
    	
        Map<String, String> sortMap = new TreeMap<String, String>();
        if (null != querys) {
        	for (Map.Entry<String, String> query : querys.entrySet()) {
        		if (!StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
        			sortMap.put(query.getKey(), query.getValue());
                }
        	}
        }
        
        
        StringBuilder sbParam = new StringBuilder();
        for (Map.Entry<String, String> item : sortMap.entrySet()) {
    		if (!StringUtils.isBlank(item.getKey())) {
    			if (0 < sbParam.length()) {
    				sbParam.append(XauthSignConstants.SPE3);
    			}
    			sbParam.append(item.getKey());
				sbParam.append(XauthSignConstants.SPE4).append(item.getValue());
            }
    	}
        if (0 < sbParam.length()) {
        	sb.append(sbParam);
        }
        
        return sb.toString();
    }
    
    /**
     * 构建待签名Http头
     *
     * @param headers 请求中所有的Http头
     * @return 待签名Http头
     */
    private static String buildHeaders(Map<String, String> headers) {
        StringBuilder sb = new StringBuilder();

        Map<String, String> sortMap = new TreeMap<String, String>();
        sortMap.putAll(headers);
        for (Map.Entry<String, String> header : sortMap.entrySet()) {
            if (!StringUtils.isBlank(header.getKey()) && !StringUtils.isBlank(header.getValue())) {
                // 都不为空才参与校验
                sb.append(header.getKey());
                sb.append(XauthSignConstants.SPE2);
                sb.append(header.getValue());
            }
            sb.append(XauthSignConstants.LF);
        }

        return sb.toString();
    }
    
    /**
     * 重新拼接url
     * 
     * @param path
     * @param querys
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String initUrl(String path, Map<String, String> querys) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        if (!StringUtils.isBlank(path)) {
            sbUrl.append(path);
        }
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append(XauthSignConstants.SPE3);
                }
//                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
//                    sbQuery.append(query.getValue());
//                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append(XauthSignConstants.SPE4);
                        sbQuery.append(URLEncoder.encode(query.getValue(), XauthSignConstants.ENCODING));
                    }                   
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append(XauthSignConstants.SPE5).append(sbQuery);
            }
        }
        
        return sbUrl.toString();
    }
}
