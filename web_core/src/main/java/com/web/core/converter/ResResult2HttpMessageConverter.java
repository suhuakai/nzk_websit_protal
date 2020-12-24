package com.web.core.converter;

import com.web.core.entity.ResResult;
import com.web.core.exception.ValidationException;
import com.web.core.interceptor.ResResultInterceptor;
import com.web.core.util.JSONUtils;
import com.web.core.util.RestHelper;
import com.web.core.web.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 接口响应返回Json格式消息定义器重写扩展
 * @author
 * @version 1.0
 */
public class ResResult2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 是否使用格式化json输出，默认否
	 */
	private boolean prettyPrint = false;

	@Override
	protected void writeInternal(Object result, @Nullable Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		try {
			HttpServletRequest request = WebUtils.getRequest();
			Assert.notNull(request, "request，未获取到请求上下文对象");

			Boolean ignoreStdResult = (Boolean) request.getAttribute(ResResultInterceptor.IGNORE_STD_RESULT);
			if (!Boolean.TRUE.equals(ignoreStdResult) && !(result instanceof ResResult)) {
				ResResult resResult = RestHelper.ok(result);
				//resResult.setResult(result);
				//resResult.evalDuring();//计算请求处理时长
				result = resResult;
			}

			if (logger.isTraceEnabled()) {
				if (this.prettyPrint) {
					logger.info("■[ResponseBody]：" + JSONUtils.toPrettyJsonLoosely(result));
				} else {
					logger.info("■[ResponseBody]：" + JSONUtils.toJsonLoosely(result));
				}
			}
			super.writeInternal(result, type, outputMessage);
			//标准报文已经写入
			WebUtils.setRequestAttribute(ResResultInterceptor.RESPONSE_RESULT_WRITTEN_FLAG, true);
		} catch (ValidationException e) {
			logger.error(e.getMessage()/*, e*/);
		}
	}

	@Override
	public void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

}