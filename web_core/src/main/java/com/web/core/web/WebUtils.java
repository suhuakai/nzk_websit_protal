package com.web.core.web;

import com.web.core.entity.ResResult;
import com.web.core.exception.MediotException;
import com.web.core.exception.ValidationException;
import com.web.core.interceptor.ResResultInterceptor;
import com.web.core.util.ExceptionUtils;
import com.web.core.util.JSONUtils;
import com.web.core.util.LocalAssert;
import com.web.core.util.RestHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.*;

/**
 * 控制器支持类, 包含控制器需要的公用性较强的一些方法
 * @author
 * @version 1.0
 */
public class WebUtils {

    public static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

    private static ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal<>();

    private static ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal<>();

    /**
     * 当前线程绑定：request、response
     * @param request 请求
     * @param response 响应
     * @param initResResultFlag 是否初始化标准结果
     */
    public static void bindRequestAndResponse(HttpServletRequest request, HttpServletResponse response, boolean initResResultFlag) throws IOException {
        requestLocal.set(request);
        responseLocal.set(response);
        if (initResResultFlag) {
            //初始化并绑定标准响应对象
            bindResResult();
        }
        logger.trace("WebUtils：已经绑定request和response资源。");
    }

    /**
     * 初始化标准响应对象
     * @return ResResult
     * @throws IOException
     * @author
     *
     */
    public static ResResult bindResResult() throws IOException {
        //获取当前request
        HttpServletRequest request = getRequest();
        //从请求中获取：ResResult
        ResResult resResult = (ResResult) request.getAttribute(ResResultInterceptor.RESPONSE_RESULT);
        if (resResult == null) {
            String requestURI = request.getRequestURI();
            if (requestURI != null) {
                requestURI = URLDecoder.decode(requestURI, "UTF-8").replaceAll("[/\\\\]{2,}", "/");
            }
            //请求摘要（路径、参数）
            StringBuilder requestSummary = new StringBuilder();
            if (logger.isDebugEnabled()) {
                requestSummary.append("┋requestURI=" + requestURI + "┋" + request.getContextPath());
            }
            if (logger.isTraceEnabled()) {
                requestSummary.append("┋params= " + JSONUtils.toJsonLoosely(request.getParameterMap()));
            }
            logger.trace("{}", requestSummary);

            resResult = RestHelper.ok();
            //resResult.setReqParams(request.getParameterMap());
            request.setAttribute(ResResultInterceptor.RESPONSE_RESULT, resResult);
            logger.trace("WebUtils：当前请求已经绑定ResResult。");
        }
        return resResult;
    }

    /**
     * 获取公共的响应结果实体 <p>
     * 可以保证前端接收到的响应报文的数据结构一致，<br>
     * 原则上建议大家都使用这个作为返回<p>
     * @return ResResult
     */
    public static ResResult getResResult() throws IOException {
        ResResult resResult = null;
        if (requestLocal.get() != null) {
            resResult = (ResResult) requestLocal.get().getAttribute(ResResultInterceptor.RESPONSE_RESULT);
            if (resResult == null) {
                resResult = bindResResult();
            }
        }
        if (resResult == null) {
            resResult = RestHelper.ok();
        }
        return resResult;
    }

    /**
     * 获取请求主机IP地址
     * @param request
     */
    public static final String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        StringBuilder iptrace = new StringBuilder();
        iptrace.append("|X-Forwarded-For=").append(ip);
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
                iptrace.append("|Proxy-Client-IP=").append(ip);
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                iptrace.append("|WL-Proxy-Client-IP=").append(ip);
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                iptrace.append("|HTTP_CLIENT_IP=").append(ip);
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                iptrace.append("|HTTP_X_FORWARDED_FOR=").append(ip);
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                iptrace.append("|getRemoteAddr=").append(ip);
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String strIp : ips) {
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        logger.trace("客户主机追溯: {}", iptrace.toString());
        logger.debug("获取请求主机IP地址: task ip = {}", ip);
        return ip;
    }

    /**
     * 标准报文输出
     * @param exception
     * @param response
     * @param prettyPrint
     * @throws IOException
     */
    public static void writeResResult(Exception exception, HttpServletResponse response, boolean prettyPrint) throws IOException {
        ResResult resResult=RestHelper.error();
        Integer status = MediotException.DEFAULT_ERROR_CODE;
        String message = null;
        if (exception != null) {
            if (exception instanceof MediotException) {
                MediotException MediotException = ((MediotException) exception);
                status = MediotException.getCode();
                message = ExceptionUtils.getRootCauseMsg(MediotException);
                resResult=RestHelper.error(status,message,MediotException.getAttachment());
            } else if (exception instanceof NoHandlerFoundException) {
                status = HttpStatus.NOT_FOUND.value();
            } else if (exception instanceof MethodArgumentNotValidException) {
                List<FieldError> fieldErrors = ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors();
                status = HttpStatus.BAD_REQUEST.value();
                message = concatMessage(fieldErrors);
            } else if (exception instanceof BindException) {
                List<FieldError> fieldErrors = ((BindException) exception).getBindingResult().getFieldErrors();
                status = HttpStatus.BAD_REQUEST.value();
                message = concatMessage(fieldErrors);
            } else {
                message = ExceptionUtils.getRootCauseMsg(exception);
            }
            //resResult.setEpt(ExceptionUtils.getRootCause(exception).getClass().getSimpleName());
            resResult.message(message);
            //openAPI添加错误返回报文
//            if (resResult.getUri().matches("/apiTask/[^\\s]*")){
//                if (Objects.isNull(resResult.getResult())){
//                    if (StringUtils.isNotBlank(message) && !("操作成功").equals(message)){
//                        Map<String,Object> map = new HashMap<String,Object>();
//                        map.put("code", "CE");
//                        map.put("desc", message);
//                        resResult.setResult(map);
//                    }
//                }
//            }
            logger.error("请求处理出现异常！异常信息为：", exception);
        } else {
            status = HttpStatus.OK.value();
        }
        //请求处理状态
        resResult.code(status);
        //计算请求处理时长（单位:毫秒）
        //resResult.evalDuring();
        //记录用户失败登陆次数
        //resResult.setFailCount(requestLocal.get().getSession().getAttribute("failCount") == null ? 0 :(Integer.valueOf(requestLocal.get().getSession().getAttribute("failCount").toString())));
        //标准报文输出
        writeResResult(resResult, response, prettyPrint);
    }
    
    /**
     * Spring数据校验错误转化成友好的提示语
     * @param fieldErrors
     * @return String
     * @author
     *      */
    private static String concatMessage(List<FieldError> fieldErrors) {
        String message = null;
        if (CollectionUtils.isNotEmpty(fieldErrors)) {
            StringBuilder msg = new StringBuilder();
            for (FieldError fieldError : fieldErrors) {
                if (fieldError != null) {
                    msg.append(fieldError.getDefaultMessage());
                }
            }
            message = msg.toString();
        } else {
            message = "请求数据格式校验不通过，请检查！";
        }
        return message;
    }

    /**
     * 标准报文输出
     * @param resResult
     * @param response
     * @return void
     * @throws IOException
     *
     */
    private static void writeResResult(ResResult resResult, HttpServletResponse response, boolean prettyPrint) throws IOException {
        //是否已经响应写入
        if (response.isCommitted()) {
            return;
        }
        String jsonResResult = "";
        if (prettyPrint) {
            jsonResResult = JSONUtils.toPrettyJson(resResult);
        } else {
            jsonResResult = JSONUtils.toJson(resResult);
        }
        try {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(jsonResResult);
            out.close();
            WebUtils.setRequestAttribute(ResResultInterceptor.RESPONSE_RESULT_WRITTEN_FLAG, true);
            if (logger.isDebugEnabled()) {
                logger.debug("■[ResponseBody]：" + jsonResResult);
            }
        } catch (Exception e1) {
            logger.error("输出标准结果，出现异常！", e1);
            ServletOutputStream outstream = response.getOutputStream();
            IOUtils.write(jsonResResult, outstream);
            logger.info("输出标准结果，再次重试，输出成功。");
            try {
                WebUtils.setRequestAttribute(ResResultInterceptor.RESPONSE_RESULT_WRITTEN_FLAG, true);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
            if (logger.isDebugEnabled()) {
                logger.debug("■[ResponseBody]：" + jsonResResult);
            }
        }
    }

    /**
     * 获取当前request，没有就抛出异常
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        Assert.notNull(requestLocal.get(), "WebUtils没有绑定request对象！");
        return requestLocal.get();
    }

    /**
     * 获取当前response，没有就抛出异常
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        Assert.notNull(responseLocal.get(), "WebUtils没有绑定response对象！");
        return responseLocal.get();
    }

    /**
     * request参数设定
     * @param name
     * @param value
     * @return void
     * @throws ValidationException
     */
    public static void setRequestAttribute(String name, Object value) throws ValidationException {
        Assert.notNull(requestLocal.get(), "WebUtils没有绑定request对象！");
        requestLocal.get().setAttribute(name, value);
    }

    /**
     * 获取当前会话
     * @return HttpSession
     */
    public static HttpSession getSession() {
        HttpSession session = getRequest().getSession(false);
        LocalAssert.notNull(session, "当前请求会话无法获取！");
        return session;
    }

    /**
     * 获取当前会话数据
     * @return HttpSession
     */
    public static <T> T getSessionAttribute(String attributeName) {
        HttpSession session = getRequest().getSession(false);
        LocalAssert.notNull(session, "当前请求会话无法获取！");
        return (T) session.getAttribute(attributeName);
    }

    /**
     * 获取当前会话用户
     * @return HttpSession
     * @author
     *      */
    public static <T> T getSessionUser(String attributeName) {
        T sessionUser = (T) getSessionAttribute(attributeName);
        LocalAssert.notNull(sessionUser, "请确保已正常登录！");
        return sessionUser;
    }

}
