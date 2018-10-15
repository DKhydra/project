package com.hydra.utils;

import com.alibaba.fastjson.JSON;
import com.hydra.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ResponseUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ResponseUtil.class);
	
	@SuppressWarnings("rawtypes")
	public static final void responseResult(HttpServletResponse response, ResultModel resultModel) {
		if(resultModel != null) {
			response.setContentType("text/javascript; charset=utf-8");
			PrintWriter pw = null;
			try {
				pw = response.getWriter();
				pw.write(JSON.toJSONString(resultModel));
				pw.flush();
				pw.close();
			} catch (Exception e) {
				logger.error("response write error.", e);
				pw = null;
			} finally {
				if(pw!=null) {
					pw.close();
				}
			}
		}
	}

}
