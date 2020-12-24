package com.web.common.web;

import java.util.LinkedHashMap;
import java.util.Map;

import com.web.common.constant.WebError;
import com.web.common.security.permission.annotation.Opened;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 常量接口
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@RestController
@RequestMapping("constant")
public class ConstantController {

    @RequestMapping("viewErrorCodes")
    @Opened
    public Object viewErrorCodes(Integer errorCode) {
        Map<Integer, String> errorCodes = null;
        if (errorCode != null) {
            errorCodes = new LinkedHashMap<>(1);
            WebError error = WebError.valueOf("E" + errorCode);
            errorCodes.put(error.getCode(), error.getMessage());
        } else {
            WebError[] errors = WebError.values();
            errorCodes = new LinkedHashMap<>(errors.length);
            for (WebError error : errors) {
                errorCodes.put(error.getCode(), error.getMessage());
            }
        }
        return errorCodes;
    }


}
