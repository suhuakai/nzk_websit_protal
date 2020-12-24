package com.web.common.operation.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.web.common.annotation.OperationInfo;
import com.web.common.constant.CustomConst;
import com.web.common.utils.HttpClientUtils;
import com.web.core.util.IdentifieUtil;
import com.web.core.web.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Aspect
@Component
@Slf4j
@ConditionalOnProperty(name = "usercenter.WebLog.enable", havingValue = "true", matchIfMissing = true)
public class WebLogConfig {

    /**
     * 拦截注解的路径
     */
    @Pointcut("execution(public * com.web..*Controller.*(..))")
    public void webLogConfigInfo() {

    }

    /**
     * 回执通知
     * @param point
     */
    @AfterReturning("webLogConfigInfo()")
    public void doAfterReturning(JoinPoint point) {
        try {
            HttpServletRequest request = WebUtils.getRequest();
            HttpServletResponse response = WebUtils.getResponse();
            if (request == null || response == null || point == null) {
                return;
            }

            OperationInfo operationInfo = getOperationInfo(point);
            String targetUrl = request.getRequestURI();
            // 访问登录的,这里创建一个登录日志
            if (targetUrl != null && (targetUrl.contains("/login/ajaxLogin") || targetUrl.endsWith("/login/ajaxLogin"))) {
                userLogin(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拦截异常信息
     * @param point
     * @param exc
     */
    @AfterThrowing(throwing = "exc", pointcut = "webLogConfigInfo()")
    public void doAfterThrowing(JoinPoint point, Exception exc) {
        try {
            HttpServletRequest request = WebUtils.getRequest();
            HttpServletResponse response = WebUtils.getResponse();
            if (request == null || response == null || point == null) {
                return;
            }
            OperationInfo operationInfo = getOperationInfo(point);
            if (operationInfo != null) {// 有操作日志注解，即记录操作日志
                // 创建一个操作日志,记录错误信息
               /* UserOperation userOperation = new UserOperation();
                userOperation.setOperationCode("500");
                userOperation.setOperationRes("操作失败");
                String errorInfo = exc.toString().replace("\r", "").replace("\n", " ");
                if (errorInfo.length() > 500) {
                    userOperation.setErrorReason(errorInfo.substring(0, 500));
                } else {
                    userOperation.setErrorReason(errorInfo);
                }*/
               /* String requestParam = JSON.toJSONString(point.getArgs());
                parseAnnotation(userOperation, operationInfo, requestParam, point);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 记录登录人信息
     * @param request
     */
    public void userLogin(HttpServletRequest request) {
        Object sessionUser = WebUtils.getSessionAttribute(CustomConst.LoginUser.SESSION_USER_INFO);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(sessionUser));
        String orgId = jsonObject.getString("orgId");
        String platformId = jsonObject.getString("platformId");
        if (null != sessionUser) {
           /* String userAgent = request.getHeader("User-Agent");
            UserLogin userLogin = new UserLogin();
            userLogin.setLoginGuid(IdentifieUtil.getGuId());
            userLogin.setLoginUserid(jsonObject.getString("userId"));
            userLogin.setLoginUsername(jsonObject.getString("userName"));
            userLogin.setOrgId(orgId);
            userLogin.setOrgName(jsonObject.getString("orgName"));
            userLogin.setPlatformId(platformId);
            userLogin.setLoginIp(request.getRemoteAddr());
            userLogin.setLoginBrowser(HttpClientUtils.getLogBowser(userAgent));
            userLogin.setLoginOs(HttpClientUtils.getLogOS(userAgent));
            userLogin.setLoginDate(LocalDateTime.now());
            operationApi.insertUserLogin(userLogin);
            String key = "login:operation:loginLogid"+"_"+userLogin.getLoginUserid();
            request.getSession().setAttribute(key, userLogin.getLoginGuid());*/
        }
    }


    /**
     * 判断是否有注解
     * @param point
     * @return
     */
    public OperationInfo  getOperationInfo(JoinPoint point){
        OperationInfo operationInfo = null;
        //方法名
        String method = point.getSignature().getName();
        //获取到这个类上面的方法全名
        Method methods[] = point.getSignature().getDeclaringType().getMethods();
        for (Method m : methods) {
            boolean flag = false;
            if (m.getName().equals(method)) {
                Annotation[] annotations = m.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType() == OperationInfo.class) {
                        operationInfo = m.getAnnotation(OperationInfo.class);
                        flag = true;
                        break;
                    }
                }
                if(flag){
                    break;
                }
            }
        }
        return operationInfo;
    }

    /**
     * 记录操作信息
     * @param userOperation
     * @param operationInfo
     * @param requestParam
     * @param point
     */
   /* public void parseAnnotation(UserOperation userOperation, OperationInfo operationInfo, String requestParam, JoinPoint point) {
        //操作名称
        String operationName = operationInfo.operationName();
        userOperation.setOperationName(operationName);
        //模块id
        String moduleId = operationInfo.moduleId();
        userOperation.setModuleId(moduleId);
        //模块名称
        String moduleName = operationInfo.moduleName();
        userOperation.setModuleName(moduleName);
        //需要记录参数keys，逗号分隔
        String keys = operationInfo.keys();
        //需要记录的参数values，是keys对应的中文名称
        String values = operationInfo.values();
        try {
            //解析keys和values，拼装参数
            if (StringUtils.isNotBlank(keys)) {
                String[] keyArgs = keys.split(",");
                if (StringUtils.isNotBlank(values)) {
                    String[] valueArgs = values.split(",");
                    for (int index = 0; index < keyArgs.length; index++) {
                        String operationArgs = "";
                        for (Object object : point.getArgs()) {
                            String json = JSON.toJSONString(object);
                            ObjectMapper objectMapper = new ObjectMapper();
                            Map<String, Object> map = objectMapper.readValue(json, Map.class);
                            Set<Map.Entry<String, Object>> set = map.entrySet();
                            for (Map.Entry entry : set) {
                                if (entry.getKey().equals(keyArgs[index])) {
                                    if (index < keyArgs.length - 1) {
                                        operationArgs += valueArgs[index] + ":" + "[" + entry.getValue() + "]" + ",";
                                    } else {
                                        operationArgs += valueArgs[index] + ":" + "[" + entry.getValue() + "]";
                                    }
                                    if(operationArgs.length()>500){
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            userOperation(userOperation, point);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解析失败",e);
        }
    }*/

    /**
     * 记录操作信息
     * @param userOperation
     * @param point
     */
   /* public void userOperation(UserOperation userOperation,JoinPoint point) {
        HttpServletRequest request = WebUtils.getRequest();
        String targetUrl = request.getRequestURI();
        userOperation.setOperationGuid(IdentifieUtil.getGuId());
        userOperation.setOperationUrl(targetUrl);
        userOperation.setOperationDate(LocalDateTime.now());
        if (StringUtils.isBlank(userOperation.getOperationArgs())) {
            // 如果没有封装参数，默认的参数是request过来的参数串，最大只能存储500个
            List<String> strList = Lists.newArrayList();
            try{
                for (Object object : point.getArgs()) {
                    boolean flag = false;
                    String json = JSON.toJSONString(object);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> map = objectMapper.readValue(json, Map.class);
                    Set<Map.Entry<String, Object>> set = map.entrySet();
                    int index = 0;
                    String operationArgs = "";
                    for (Map.Entry entry : set) {
                        if(operationArgs.length() < 500 && index < set.size()-1){
                            operationArgs += entry.getKey() + ":" + "[" + entry.getValue() + "]" + ",";
                        }else if(operationArgs.length() < 500 && index == set.size()-1){
                            operationArgs += entry.getKey() + ":" + "[" + entry.getValue() + "]";
                            break;
                        }else{
                            operationArgs += entry.getKey() + ":" + "[" + entry.getValue() + "]";
                            flag = true;
                            break;
                        }
                        index++;
                    }
                    strList.add(operationArgs);
                    if(flag){
                        break;
                    }
                }
                userOperation.setOperationArgs(String.join(";",strList));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Object sessionUser = WebUtils.getSessionAttribute(CustomConst.LoginUser.SESSION_USER_INFO);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(sessionUser));
        userOperation.setPlatformSystemId(jsonObject.getString("platformSystemId"));
        String key = "login:operation:loginLogid"+"_"+jsonObject.getString("userId");
        userOperation.setLoginGuid((String)request.getSession().getAttribute(key));
        operationApi.insertUserOperation(userOperation);
    }*/
}
