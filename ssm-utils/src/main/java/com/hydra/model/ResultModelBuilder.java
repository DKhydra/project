package com.hydra.model;


import com.hydra.exception.XbaseError;

public class ResultModelBuilder {
	public static <T> ResultModel<T> data(T t) {
        return new ResultModel<T>(t);
    }
    
    public static <T> ResultModel<T> nvl() {
        return new ResultModel<T>();
    }
    
    public static <T> ResultPagingModel<T> data(PageResult<T> t) {
    	return new ResultPagingModel<T>(t);
    }

    public static ResultModel<Boolean> success() {
        return new ResultModel<Boolean>(true);
    }

    public static <T> ResultModel<T> failure() {
        return failure("");
    }

    public static <T> ResultModel<T> failure(String desc) {
        ResultModel<T> model = new ResultModel<T>();
        model.setReturnCode(XbaseError.SYS_OPERATION_ERROR.getErrorCode());
        model.setReturnMessage(XbaseError.SYS_OPERATION_ERROR.getErrorDesc() + ":" + desc);
        return model;
    }

    public static <T> ResultModel<T> failure(XbaseError error) {
        return failure(error, "");
    }

    public static <T> ResultModel<T> failure(XbaseError error, String desc) {
        ResultModel<T> model = new ResultModel<T>();
        model.setReturnCode(error.getErrorCode());
        model.setReturnMessage(error.getErrorDesc() + ":" + desc);
        return model;
    }

}
