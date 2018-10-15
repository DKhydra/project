package com.hydra.model;

import com.hydra.exception.XbaseError;
import com.hydra.exception.XbaseException;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultModel<T> implements Serializable {

	private static final long serialVersionUID = -9058230013921591135L;

	private String returnCode = "000000";
	/** 错误码 */
	private String returnMessage;

	/** 错误堆栈 */
	protected String exStack;

	/** 错误解决方案 */
	protected String solution;

	/** 错误处理人，一般可以指定维护人员 */
	protected String[] opers;

	/** 当前请求的唯一性标识 */
	protected String traceId;

	/** 返回值 */
	protected T data;

	/** 附加信息 */
	protected Map<String, Object> additions = new HashMap<String, Object>();

	public boolean checkInteger(Integer param, String name) {
		if (param == null) {
			XbaseException xe = new XbaseException(XbaseError.SYS_PARAM_ERROR, String.format("%s不能为NULL", name));
			xe.setErrorDesc(String.format("%s为必填数据", name));
			throw xe;
		}
		return true;
	}
	public boolean checkLong(Long param, String name) {
		if (param == null) {
			XbaseException xe = new XbaseException(XbaseError.SYS_PARAM_ERROR, String.format("%s不能为NULL", name));
			xe.setErrorDesc(String.format("%s为必填数据", name));
			throw xe;
		}
		return true;
	}
	public boolean checkList(List param, String name) {
		if (param == null || param.size() == 0) {
			XbaseException xe = new XbaseException(XbaseError.SYS_PARAM_ERROR, String.format("%s不能为NULL", name));
			xe.setErrorDesc(String.format("%s为必填数据", name));
			throw xe;
		}
		return true;
	}

	public boolean checkDate(Date param, String name) {
		if (param == null) {
			XbaseException xe = new XbaseException(XbaseError.SYS_PARAM_ERROR, String.format("%s不能为NULL", name));
			xe.setErrorDesc(String.format("%s为必填数据", name));
			throw xe;
		}
		return true;
	}

	public boolean checkFloat(Float param, String name) {
		if (param == null) {
			XbaseException xe = new XbaseException(XbaseError.SYS_PARAM_ERROR, String.format("%s不能为NULL", name));
			xe.setErrorDesc(String.format("%s为必填数据", name));
			throw xe;
		}
		return true;
	}

	public boolean checkString(String param, String name) {
		if (StringUtils.isEmpty(param)) {
			XbaseException xe = new XbaseException(XbaseError.SYS_PARAM_ERROR, String.format("%s不能为NULL", name));
			xe.setErrorDesc(String.format("%s为必填数据", name));
			throw xe;
		}
		return true;
	}

	/**
	 * <p>
	 * Description: ResultModel
	 * </p>
	 */
	public ResultModel() {

	}

	/**
	 * <p>
	 * Description:ResultModel
	 * </p>
	 * 
	 * @param data
	 */
	public ResultModel(T data) {
		this.data = data;
	}

	/**
	 * <p>
	 * Description:ResultModel
	 * </p>
	 * 
	 * @param returnCode
	 * @param returnMessage
	 */
	public ResultModel(String returnCode, String returnMessage) {
		this.setReturnCode(returnCode);
		this.setReturnMessage(returnMessage);
	}

	/**
	 * <p>
	 * Description: ResultModel
	 * </p>
	 * 
	 * @param data
	 * @param returnCode
	 * @param returnMessage
	 */
	public ResultModel(T data, String returnCode, String returnMessage) {
		this(returnCode, returnMessage);
		this.data = data;
	}

	/**
	 * 请求是否成功操作成功
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		return StringUtils.equals("000000", getReturnCode());
	}

	/**
	 * @return the exStack
	 */
	public String getExStack() {
		return exStack;
	}

	/**
	 * @param exStack
	 *            the exStack to set
	 */
	public void setExStack(String exStack) {
		this.exStack = exStack;
	}

	/**
	 * @return the opers
	 */
	public String[] getOpers() {
		return opers;
	}

	/**
	 * @param opers
	 *            the opers to set
	 */
	public void setOpers(String[] opers) {
		this.opers = opers;
	}

	/**
	 * @return the solution
	 */
	public String getSolution() {
		return solution;
	}

	/**
	 * @param solution
	 *            the solution to set
	 */
	public void setSolution(String solution) {
		this.solution = solution;
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * @return the traceId
	 */
	public String getTraceId() {
		return traceId;
	}

	/**
	 * @param traceId
	 *            the traceId to set
	 */
	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	/**
	 * @return the additions
	 */
	public Object getAddition(String key) {
		return additions.get(key);
	}

	/**
	 * @param
	 */
	public void setAddition(String key, Object value) {
		this.additions.put(key, value);
	}

	/**
	 * 将结果转换为map类型, 并附加result
	 * 
	 * @param result
	 * @return
	 */
	public Map<String, Object> dump(final Map<String, ?> result) {
		Map<String, Object> retVal = dump();
		retVal.putAll(result);
		return retVal;
	}

	/**
	 * 将结果转换为map类型
	 * 
	 * @return
	 */
	public Map<String, Object> dump() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(ResultKey.data, data);
		result.put(ResultKey.returnCode, getReturnCode());
		if (getReturnMessage() != null)
			result.put(ResultKey.returnMessage, getReturnMessage());
		if (exStack != null)
			result.put(ResultKey.exStack, exStack);
		if (solution != null)
			result.put(ResultKey.solution, solution);
		return result;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

}
