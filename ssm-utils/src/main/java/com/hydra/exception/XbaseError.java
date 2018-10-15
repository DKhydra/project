package com.hydra.exception;

public enum XbaseError {

	LOGIN_ERROR("000001", "登录失败."),
	NOT_FOUND_ERROR("000002", "没有找到."),
	NOT_ALLOW_ERROR("000003", "不能删除，存在关联数据."),
	LOGIN_VERIFICATION_ERROR("000004","验证码错误"),
	NOT_LOGIN("000005","未登录或Session过期，请重新登录。"),
	IMAGE_OVERLOAD_ERROR("X0250015", "上传图片大小不能超过1M！"),
	IMAGE_UPLOAD_FAILER_ERROR("X0250016", "图片上传失败！"),
	IMAGE_UPLOAD_NULL_ERROR("X0250017", "上传图像不可为空"),
	IMAGE_DELETE_URL_ERROR("X0250018", "图像地址错误, 无法删除"),

	LOGIN_STATE_VERIFY("000006","权限不足"),

	SSO_REDIS_QUERY_ERROR("X0120001", "cache query error."),
	SSO_REDIS_BACKEND_ERROR("X0120002", "cache backend error."),
	SSO_REDIRECT_URL_ILLEGAL("X0120003", "the redirectUrl is illegal."),

	USER_NOT_EXIST_ERROR("X0130001", "user not exist."),
	USER_PASS_ERROR("X0130002", "username or password error."),
	USER_PASS_CONFIRM_ERROR("X0130003", "password must equals of confirmpass"),
	USER_EXIST_ERROR("X0130004", "user already exist."),
	USER_NEW_PASS_ERROR("X0130005", "new password must be diff with old password"),
	USER_STATUS_ERROR("X0140006", "user status is disabled."),

	SYS_ERROR("X0110001", "System internal error."),
	SYS_NPE_ERROR("X0110002", "System error, no data returned."),
	SYS_PARAM_ERROR("X0110003", "Parameter invalid."),
	SYS_DECRYPTION_ERROR("X0110004", "decryption error."),
	SYS_ENCRYPTION_ERROR("X0110005", "encryption error."),
	SYS_OPERATION_ERROR("X0110006", "System operation error."),
	SYS_DB_ERROR("X0110007", "db error."),
	SYS_XAUTH_ERROR("X0110008", "api signature validate error."),
	SYS_SESSION_TIMEOUT("X0110009", "system session timeout."),
	SYS_PARAM_LENGTH_ERROR("X0110010", "Parameter length invalid."),
	;

	private String errorCode;
	private String errorDesc;

	XbaseError(String errorCode, String errorDesc) {
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}

	public String getErrorCode() {
		return errorCode;
	}
	public String getErrorDesc() {
		return errorDesc;
	}

	public String getError() {
		return errorCode + ":" + errorDesc;
	}

	public String getError(String msg) {
		return errorCode + ":" + errorDesc + "," + msg;
	}
}