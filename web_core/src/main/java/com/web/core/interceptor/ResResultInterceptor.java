package com.web.core.interceptor;

import com.web.core.web.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 标准响应结果处理器
 * @author
 * @version 1.1
 */
public class ResResultInterceptor extends HandlerInterceptorAdapter {
	public final Logger logger = LoggerFactory.getLogger(ResResultInterceptor.class);

	public static final String RESPONSE_RESULT = "_ResResult";

	/**
	 * 标识名称: 标准报文是否已经写入
	 */
	public static final String RESPONSE_RESULT_WRITTEN_FLAG = "RESPONSE_RESULT_WRITTEN_FLAG";

	/**
	 * 标识名称: 是否忽略标准结果
	 */
	public static final String IGNORE_STD_RESULT = "IGNORE_STD_RESULT";

	/**
	 * 是否使用格式化json输出，默认否
	 */
	private boolean prettyPrint = false;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		WebUtils.bindRequestAndResponse(request, response, true);
		logger.trace("标准响应结果处理器：前置动作就绪。");
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
		//是否已经响应写入
		if (response.isCommitted()) {
			return;
		}
		if (handler.getClass().equals(HandlerMethod.class)) {
			//接口方法信息
			HandlerMethod handlermethod = (HandlerMethod) handler;
			//标准报文已经写入：是|否
			Boolean isWrittenFlag = (Boolean) request.getAttribute(RESPONSE_RESULT_WRITTEN_FLAG);
			//是否忽略标准结果：是|否
			Boolean ignoreStdResult = (Boolean) request.getAttribute(IGNORE_STD_RESULT);
			//如果ResponseBody类型的请求
			if (handlermethod.getMethodAnnotation(ResponseBody.class) != null
                    || handlermethod.getBeanType().getAnnotation(RestController.class) != null) {
				if (!Boolean.TRUE.equals(isWrittenFlag) && !Boolean.TRUE.equals(ignoreStdResult)) {
					//标准报文输出
					WebUtils.writeResResult(exception, response, this.prettyPrint);
				}
			}
		}
	}

	/**
	 * 是否使用格式化json输出
	 * @param prettyPrint
	 * @return void
	 * @author
	 */
	public void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

}
