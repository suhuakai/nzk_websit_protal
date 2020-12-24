package com.web.common.filter;

import com.web.common.config.WebConfig;
import com.web.common.constant.CustomConst.LoginUser;
import com.web.common.constant.WebError;
import com.web.core.exception.ValidationException;
import com.web.core.web.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录认证检查
 * @author
 * @version 1.0
 */
@Slf4j
@WebFilter("/*")
@Configuration
@ConditionalOnProperty(name = "usercenter.LoginFilter.enable", havingValue = "true", matchIfMissing = true)
public class LoginFilter implements Filter {

    /**
     * 白名单
     */
    String[] excludeUrls = {
            "(?:/images/|/css/|/js/|/template/|/static/|/constant/).+$",
            "/login/ajaxLogin",
            "/login/createImage"
    };

    /**
     * 开放api
     */
    String[] openApiUrls = {
            "/apiTask/[^\\s]*"
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException, SecurityException {
        log.debug("➧➧➧ 用户登录认证检查 -- 开始");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String serviceName = request.getServerName();
        WebUtils.bindRequestAndResponse(request, response, true);
        log.debug("ServiceName={}，RequestUri={}, ContextPath={}", serviceName, requestUri, contextPath);
        //添加到白名单的URL放行
        for (String url : excludeUrls) {
            if (requestUri.matches(contextPath + url) || (serviceName.matches(url))) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        //处理开放api
        for (String url : openApiUrls) {
            if (requestUri.matches(contextPath + url) || (serviceName.matches(url))) {
//                String token = request.getHeader("token");
//                if (StringUtils.isBlank(token)){
//                    WebUtils.writeResResult(new ValidationException(YgbError.OPENAPI_TOKEN_MISSING), response, true);
//                    return;
//                }else{
//                    try{
//                        //获取token中的时间戳
//                        String timestamp = token.substring(token.lastIndexOf("-")+1, token.length());
//                        long longTimeStamp = new Long(new Long(timestamp) * 1000);
//                        Date begin = new Date(longTimeStamp);
//                        Date end = new Date();
//                        //根据token生成时间来计算token是否失效
//                        long min =(end.getTime()-begin.getTime())/1000/60;//除以1000是为了转换成秒
//                        if (min > 120){
//                            WebUtils.writeResResult(new ValidationException(YgbError.OPENAPI_TOKEN_OVERDUE), response, true);
//                            return;
//                        }
//                    }catch (Exception e){
//                        WebUtils.writeResResult(new ValidationException(YgbError.OPENAPI_TOKEN_OVERDUE), response, true);
//                        return;
//                    }
//                }

                filterChain.doFilter(request, response);
                return;
            }
        }

        if(RequestMethod.OPTIONS.name().equals(request.getMethod())){
            filterChain.doFilter(request, response);
            return;
        }

        //检查：缺少请求头“token”!
        String tokenId = request.getHeader(WebConfig.HTTP_SESSION_ID);
        if (StringUtils.isBlank(tokenId)) {
            //请求里全部cookies
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("token".equals(c.getName())) {
                        tokenId = c.getValue();
                        break;
                    }
                }
            }
            if (StringUtils.isBlank(tokenId)) {
                WebUtils.writeResResult(new ValidationException(WebError.LOGIN_REQUIRE_TOKEN), response, true);
                return;
            }
        }
        //检查：登录已过期，请重新登录！
        HttpSession session = request.getSession(false);
        if (session == null) {
            WebUtils.writeResResult(new ValidationException(WebError.LOGIN_SESSION_EXPIRE), response, true);
            return;
        }
        //检查：用户未正常登录！
        Object sessionUser = session.getAttribute(LoginUser.SESSION_USER_INFO);
        if (null == sessionUser) {
            WebUtils.writeResResult(new ValidationException(WebError.LOGIN_SESSION_UNKNOWN), response, true);
            return;
        }
        log.debug("➧➧➧ 用户登录认证检查 -- 通过。");
        filterChain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
