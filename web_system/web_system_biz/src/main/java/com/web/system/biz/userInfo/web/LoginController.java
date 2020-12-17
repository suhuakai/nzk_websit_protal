package com.web.system.biz.userInfo.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.web.core.config.WebConfig;
import com.web.core.util.VerifyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
/**
 * 登录
 *
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Slf4j
@RestController
@RequestMapping("login")
class LoginController {

    /**
     * 退出
     *
     * @author
     *      */
    @RequestMapping("/logout")
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            //注销会话
            session.invalidate();
        }
    }

    /**
     * 验证码接口
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/createImage")
    public void createImage(HttpServletResponse response, HttpServletRequest request) throws IOException {
        //利用图片工具生成图片
        //第一个参数是生成的验证码，第二个参数是生成的图片
        Object[] objs = VerifyUtil.createImage();
        //将验证码存入Session
        HttpSession session = request.getSession();
        if (!session.isNew()) {
            response.setHeader(WebConfig.HTTP_SESSION_ID, session.getId());
            log.debug("➧➧➧ 复用会话：JSESSIONID={}", session.getId());
        }
        session.setAttribute("imageCode", objs[0]);
        log.info("yzm session id:" + session.getId());
        //将图片输出给浏览器
        BufferedImage image = (BufferedImage) objs[1];
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "png", os);
        os.flush();
        os.close();
    }


}