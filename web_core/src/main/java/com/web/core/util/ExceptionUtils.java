package com.web.core.util;

import com.web.core.exception.MediotException;
import com.web.core.exception.ValidationException;
import com.web.core.exception.WebException;
import org.apache.commons.lang.StringUtils;

import java.net.ConnectException;
import java.sql.SQLException;

/**
 * 基本工具方法类
 * @author
 * @version 1.0
 */
public class ExceptionUtils {

    /**
     * 异常内部约定处理 <p/>
     * （1）SQLException:约定自定义错误码范围[-20000, 20999];
     * @param e
     * @throws Exception
     */
    public static void processException(Exception e) throws Exception {
        Throwable realException = ExceptionUtils.getRootCause(e);
        if (realException instanceof SQLException) {
            SQLException ee = (SQLException) realException;
            int errorCode = Math.abs(ee.getErrorCode());
            if (20000 < errorCode && errorCode < 20999) {//约定自定义错误码范围[-20000, 20999]
                String originMsg = ee.getMessage();
                String error = originMsg.substring(originMsg.indexOf(errorCode + ":") + 7, originMsg.indexOf("\n"));
                throw new ValidationException(error);
            } else {
                throw e;
            }
        }
    }

    /**
     * 从异常堆栈中追朔根本异常
     * @param e
     * @return Throwable
     * @author
     *
     */
    public static Throwable getRootCause(Exception e) {
        Throwable cause = e.getCause();
        if (cause != null) {
            while (cause.getCause() != null) {
                Throwable nextcause = cause.getCause();
                cause = nextcause;
            }
        } else {
            cause = e;
        }
        return cause;
    }

    /**
     * 从异常堆栈中追朔根本异常的错误消息
     * @param e
     * @return String
     * @author
     *
     */
    public static String getRootCauseMsg(Exception e) {
        String error = null;
        Throwable exception = getRootCause(e);
        if (exception instanceof SQLException) {
            SQLException ee = (SQLException) exception;
            int errorCode = Math.abs(ee.getErrorCode());
            if (20000 < errorCode && errorCode < 20999) {//约定自定义错误码范围[20000, 20999]
                String originMsg = ee.getMessage();
                error = originMsg.substring(originMsg.indexOf(errorCode + ":") + 7, originMsg.indexOf("\n"));
            } else {
                error = "【数据库错误】" + exception.getMessage();
            }
        } else if (exception instanceof ConnectException) {
            error = exception.getMessage();
            if (error != null && error.contains("Connection refused: connect")) {
                error = "【网络问题】无法访问目标服务器！";
            }else{
                error = "【网络问题】" + error;
            }
        } else if (exception instanceof NullPointerException) {
            error = "空指针";
        } else {
            error = exception.getMessage();
        }
        int firstLineIndex = -1;
        if(error!=null && (firstLineIndex = error.indexOf("\r")) < 0){
            firstLineIndex = error.indexOf("\n");
        }
        return firstLineIndex>=0 ? error.substring(0, firstLineIndex) : error;
    }

    /**
     * 从异常堆栈中追朔根本异常和错误消息
     * @param e
     * @return String
     * @author
     *
     */
    public static String getRootCauseWithMsg(Exception e) {
        Throwable cause = getRootCause(e);
        StringBuffer exceptionName = new StringBuffer("[");
        if (cause instanceof WebException) {
            WebException webException = (WebException) cause;
            String nestedException = webException.getCauseException();
            exceptionName.append(cause.getClass()
                                      .getSimpleName());
            if (StringUtils.isNotBlank(nestedException)) {
                exceptionName.append("[")
                             .append(nestedException)
                             .append("]");
            }
        } else if (cause instanceof MediotException) {
            exceptionName.append(cause.getClass()
                                      .getSimpleName());
        } else {
            exceptionName.append(cause.getClass()
                                      .getCanonicalName());
        }
        exceptionName.append("]");
        return exceptionName.toString() + cause.getMessage();
    }

}
  