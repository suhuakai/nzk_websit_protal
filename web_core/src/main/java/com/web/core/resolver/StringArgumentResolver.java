package com.web.core.resolver;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

/**
 * 字符串参数定义器
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
public class StringArgumentResolver extends AbstractNamedValueMethodArgumentResolver {

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        return new NamedValueInfo("", false, ValueConstants.DEFAULT_NONE);
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        String[] param = request.getParameterValues(name);
        if (param == null) {
            return null;
        }
        if (StringUtils.isBlank(param[0])) {
            return null;
        }
        return param[0].trim();
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(String.class);
    }

}