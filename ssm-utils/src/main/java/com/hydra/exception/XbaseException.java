package com.hydra.exception;


import com.hydra.utils.NetworkUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class XbaseException extends RuntimeException {

	private static final long serialVersionUID = 8194015142921530770L;

	private static final Logger logger = LoggerFactory.getLogger(XbaseException.class);

    /** 对应Exception.message */
    private static Field detailMessageField;

    static {
        try {
            detailMessageField = Throwable.class.getDeclaredField("detailMessage");
            detailMessageField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /** 是否输出additions的信息 */
    protected boolean logAdditions = true;

    /** 错误代码 */
    protected String errorCode;

    /** 错误信息,可以展示在前端页面 */
    protected String errorDesc;

    /** 错误详细原因,不需要展示在前端页面 */
    protected Map<String, Object> additions = new HashMap<String, Object>();

    /** errorDesc的参数 */
    private List<String> exParams = new LinkedList<String>();

    public XbaseException() {
        super(getHostName());
    }

    public XbaseException(String errorMessage) {
        super(getHostName() + errorMessage);
    }

    public XbaseException(Throwable e) {
        super(getHostName() + e.getMessage(), e);
    }

    public XbaseException(XbaseError error) {
        super(getHostName() + error.getError());
        this.errorCode = error.getErrorCode();
        this.errorDesc = error.getErrorDesc();
    }

    public XbaseException(XbaseError error, Throwable e) {
        super(getHostName() + error.getError(), e);
        this.errorCode = error.getErrorCode();
        this.errorDesc = error.getErrorDesc();
    }

    public XbaseException(XbaseError error, String errorMessage) {
        super(getHostName() + error.getError(errorMessage));
        this.errorCode = error.getErrorCode();
        this.errorDesc = error.getErrorDesc();
    }

    /**
     * 生成服务地址
     * 
     * @return
     */
    private static String getHostName() {
//        return "[" + hostInfo.getName() + "]";
        String ip = NetworkUtil.getHostIp();
        // 返回执行机 ip尾号
        return StringUtils.substring(ip, ip.length()-1) + " ";
    }

    /**
     * Getter method for property <tt>logAdditions</tt>.
     *
     * @return the logAdditions
     */
    public boolean isLogAdditions() {
        return logAdditions;
    }

    /**
     * Setter method for property <tt>logAdditions</tt>.
     * 
     * @param logAdditions
     */
    public void setLogAdditions(boolean logAdditions) {
        this.logAdditions = logAdditions;
    }

    /**
     * Getter method for property <tt>errorCode</tt>.
     *
     * @return the errorCode
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Setter method for property <tt>errorCode</tt>.
     *
     * @param errorCode value to be assigned to property errorCode
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Getter method for property <tt>errorDesc</tt>.
     *
     * @return getErrorDesc(errorDesc)
     */
    public String getErrorDesc() {
        return getErrorDesc(errorDesc);
    }

    /**
     * Getter method for property <tt>errorDesc</tt>.
     * 
     * @param desc
     * @return String
     */
    public String getErrorDesc(String desc) {
        if (!exParams.isEmpty()) {
            int size = exParams.size();
            for (int i = 0; i < size; i++) {
                desc = StringUtils.replace(desc, "${" + i + "}", exParams.get(i));
            }
        }
        String regix = "(\\$\\{[0-9]*\\}|\"\\$\\{[0-9]*\\}\")";
        if (StringUtils.isNotBlank(desc)) {
            desc = desc.replaceAll(regix, "");
        }
        return desc;
    }

    /**
     * Setter method for property <tt>errorDesc</tt>.
     * 
     * @param errorDesc the errorDesc to set
     */
    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    /**
     * Getter method for property <tt>additions</tt>.
     *
     * @return additions.get(key)
     */
    public Object getAddition(String key) {
        return additions.get(key);
    }

    /**
     * Getter method for property <tt>additions</tt>.
     *
     * @return this.additions
     */
    public Map<String, Object> getAdditions() {
        return this.additions;
    }

    /**
     * add to errorDesc
     * 
     * @param errorDesc the errorDesc to set
     */
    public <E extends XbaseException> E addErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
        return (E) this;
    }

    /**
     * add to additions
     *
     * @param key
     * @param value
     * @return E
     */
    public <E extends XbaseException> E addAddition(String key, Object value) {
        this.additions.put(key, value);
        return (E) this;
    }

    /**
     * add to additions
     * 
     * @param additions
     * @return E
     */
    public <E extends XbaseException> E addAddition(Map<String, ?> additions) {
        this.additions.putAll(additions);
        return (E) this;
    }

    /**
     * add to exParams
     *
     * @param exParam
     * @return E
     */
    public <E extends XbaseException> E addExceptionParam(String exParam) {
        if (exParam != null && exParam.length() > 0) {
            this.exParams.add(exParam);
            try {
                detailMessageField.set(this, detailMessageField.get(this) + ":" + exParam);
            } catch (Exception e) {
                logger.error("set Throwable.detailMessage failed! " + e.getMessage());
            }
        }
        return (E) this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        if (logAdditions && additions.size() > 0) {
            sb.append(": additions=").append(additions);
        }
        return sb.toString();
    }
}
