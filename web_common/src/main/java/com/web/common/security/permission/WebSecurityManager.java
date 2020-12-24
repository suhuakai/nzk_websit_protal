package com.web.common.security.permission;

import com.web.core.util.LocalAssert;
import com.web.core.util.ReflectKit;
import com.web.core.web.WebUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 访问权限控制
 * @author
 * @version 1.0
 */
@Aspect
@Component
public class WebSecurityManager {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;

    /**
     * 权限控制器集合
     */
    @Autowired
    List<Logic> permitLogics;
    /**
     * 明确可用注解范围，可以避免不必要的检查
     */
    List<Class<Annotation>> supportsAnnotations;

    @PostConstruct
    public void init() {
        if (CollectionUtils.isNotEmpty(permitLogics)) {
            supportsAnnotations = new ArrayList<>();
            for (Logic permitLogic : permitLogics) {
                supportsAnnotations.add(ReflectKit.getClassGenericType(permitLogic.getClass(), 0));
            }
        }
    }

    /**
     * 权限控制
     * @param point
     * @return Object
     * @throws Throwable
     * @author
     */
    @Around("execution(* com.web..*Controller.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object checkPermission(ProceedingJoinPoint point) throws Throwable {
        LocalAssert.notEmpty(permitLogics, "WebSecurityManager已启用，必须指定“权限控制器”列表（permissions）！");
        String uri = request.getRequestURI();
        logger.debug("准备检查接口权限，URI={}", uri);
        //切面信息
        MethodSignature signature = (MethodSignature) point.getSignature();
        //接口方法
        Method method = signature.getMethod();
        //接口权限注解
        Annotation[] permissionAnnos = filterPermissionAnnotations(method.getDeclaredAnnotations());
        if (ArrayUtils.isEmpty(permissionAnnos)) {
            //所属声明类
            Class declareClazz = signature.getDeclaringType();
            permissionAnnos = filterPermissionAnnotations(declareClazz.getDeclaredAnnotations());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("接口权限注解：{}", Arrays.toString(permissionAnnos));
        }

        //权限控制是否通过标识
        boolean permissFlag = false;
        //权限控制不通过原因
        Exception exception = null;
        if (ArrayUtils.isNotEmpty(permissionAnnos)) {
            //是否命中权限控制逻辑
            boolean doneProcessPermit = false;
            for (Annotation annotation : permissionAnnos) {
                //接口权限注解
                Annotation curPermissionAnno = annotation;
                //注解类
                Class<? extends Annotation> annotationClz = curPermissionAnno.annotationType();
                try {
                    for (Logic permitLogic : permitLogics) {
                        if (permitLogic.supportsPermitted(annotationClz)) {
                            doneProcessPermit = true;
                            //根据接口权限注解进行访问权限控制
                            permissFlag = permitLogic.processPermit(curPermissionAnno);
                            if (!permissFlag) {
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    permissFlag = false;
                    exception = e;
                }
            }
            if (!doneProcessPermit) {
                permissFlag = false;
                exception = new SecurityException("该接口未被权限控制保护！uri=" + uri);
            }
        } else {
            permissFlag = false;
            exception = new SecurityException("请明确接口权限控制方式！");
        }

        //接口权限验证通过，继续调用接口并返回调用结果
        if (permissFlag) {
            logger.debug("接口权限验证通过。URI=" + uri);
            return point.proceed();
        } else {
            logger.error("***********非法接口访问！URI=" + uri);
            if (exception == null) {
                exception = new SecurityException("鉴权：非法接口访问！");
            }
            //接口权限验证不通过，提示错误信息
            WebUtils.writeResResult(exception, response, false);
            return null;
        }
    }

    /**
     * 过滤出接口权限注解
     * @param annotations
     * @return Annotation[]
     */
    private Annotation[] filterPermissionAnnotations(Annotation[] annotations) {
        Annotation[] annos = new Annotation[0];
        if (ArrayUtils.isNotEmpty(annotations)) {
            for (Annotation anno : annotations) {
                Class<? extends Annotation> annotationClz = anno.annotationType();
                if (CollectionUtils.isNotEmpty(supportsAnnotations) && supportsAnnotations.contains(annotationClz)) {
                    Annotation[] _annos = new Annotation[annos.length + 1];
                    System.arraycopy(annos, 0, _annos, 0, annos.length);
                    _annos[_annos.length - 1] = anno;
                    annos = _annos;
                }
            }
        }
        return annos;
    }

}