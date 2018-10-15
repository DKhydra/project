package com.hydra.common.xauth;

import org.apache.commons.lang.StringUtils;

public interface XauthHttpKeys {

	public static class URLKeys {
        public static final String URL_ACCESSID = "accessId";
        public static final String URL_TIMESTAMP = "timestamp";
        public static final String URL_SIGNATURE = "signature";
    }

    public static class HeaderKeys {
        public static final String HEADER_ACCESSID = "X-La-AccessId";
        public static final String HEADER_TIMESTAMP = "X-La-timestamp";
        public static final String HEADER_SIGNATURE = "X-La-Signature";
    }
    
    
    enum XauthToken {

    	SPD_XECOMMERCE("xecom","322d628bd7994720a4a488be9b757933"),
    	SPD_XDISTRIBUTE("xedis","8fe1be7c59b043d990e39830cbc63452"),
    	;
    	String accessId;
    	String accessKey;
    	
    	XauthToken(String accessId, String accessKey) {
    		this.accessId = accessId;
    		this.accessKey = accessKey;
    	}
    	
    	public String getAccessId() {
    		return accessId;
    	}
    	
    	public String getAccessKey() {
    		return accessKey;
    	}
    	
    	static XauthToken getXauthTokenByAccessId(String accessId) {
    		for(XauthToken token : XauthToken.values()) {
    			if (StringUtils.equals(token.getAccessId(), accessId)) {
    				return token;
    			}
    		}
    		return null;
    	}
    	
    }

}

