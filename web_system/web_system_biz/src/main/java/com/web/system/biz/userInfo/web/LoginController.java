package com.web.system.biz.userInfo.web;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.web.common.util.RandomUtil;
import com.web.core.config.WebConfig;
import com.web.core.util.VerifyUtil;
import com.web.system.api.vo.SmsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;

/**
 * 登录
 *
 * @author
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j
@RestController
@RequestMapping("/login")
class LoginController {
    @Autowired
    private SmsVo smsVo;


    @Value("${spring.mail.username}")
    private String from;

    @Resource
    JavaMailSender javaMailSender;


    /**
     * 退出
     *
     * @author
     */
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


    /**
     * 发送邮箱验证码
     *
     * @param email
     * @param session
     */
    @RequestMapping(value = "/getMailCode/{email}", method = RequestMethod.GET)
    public void sendMail(@PathVariable String email, HttpSession session) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = null;
            try {
                mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            //设置发件人
            assert mimeMessageHelper != null;
            mimeMessageHelper.setFrom(from);
            //设置收件人
            mimeMessageHelper.setTo(email);
            //设置邮件主题
            mimeMessageHelper.setSubject("门户信息管理验证");
            String company = "湖北新纵科病毒疾病工程技术有限公司";
            //生成随机数
            String random = RandomUtil.generateDigitalNumString(6);
            //将随机数放置到session中
            session.setAttribute("email", email);
            session.setAttribute("code", random);
            mimeMessageHelper.setSentDate(new Date());
            String emailCodeText = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title></title>\n" +
                    "<style>\n" +
                    ".qmbox {\n" +
                    "\tpadding: 0;\n" +
                    "}\n" +
                    ".qm_con_body_content {\n" +
                    "\theight: auto;\n" +
                    "\tmin-height: 100px;\n" +
                    "\t_height: 100px;\n" +
                    "\tword-wrap: break-word;\n" +
                    "\tfont-size: 14px;\n" +
                    "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                    "}\n" +
                    ".body {\n" +
                    "\tline-height: 170%;\n" +
                    "}\n" +
                    "BODY {\n" +
                    "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                    "\tfont-size: 12px;\n" +
                    "\t-webkit-font-smoothing: subpixel-antialiased;\n" +
                    "}\n" +
                    "BODY {\n" +
                    "\tmargin: 0;\n" +
                    "\tpadding: 0;\n" +
                    "}\n" +
                    "BODY {\n" +
                    "\tbackground-color: #fff;\n" +
                    "\tfont-size: 12px;\n" +
                    "}\n" +
                    "BODY {\n" +
                    "\tbackground: #fff;\n" +
                    "}\n" +
                    "BODY {\n" +
                    "\tbackground: #fff;\n" +
                    "\tcolor: #000;\n" +
                    "\tfont-weight: normal;\n" +
                    "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                    "\tpadding: 0 7px 6px 4px;\n" +
                    "\tmargin: 0;\n" +
                    "}\n" +
                    "HTML {\n" +
                    "\ttop: 0px;\n" +
                    "}\n" +
                    ".body P {\n" +
                    "\tline-height: 170%;\n" +
                    "}\n" +
                    "</style>\n" +
                    "</head>\n" +
                    "\n" +
                    "<BODY mu=\"mu\" md=\"md\" module=\"qmReadMail\" context=\"ZC1912-rQ7uXSy7P7HThxdLFCOuY92\"><DIV class=mailcontainer id=qqmail_mailcontainer>\n" +
                    "<DIV id=mainmail style=\"MARGIN-BOTTOM: 12px; POSITION: relative; Z-INDEX: 1\">\n" +
                    "<DIV class=body id=contentDiv style=\"FONT-SIZE: 14px; HEIGHT: auto; POSITION: relative; ZOOM: 1; PADDING-BOTTOM: 10px; PADDING-TOP: 15px; PADDING-LEFT: 15px; Z-INDEX: 1; LINE-HEIGHT: 1.7; PADDING-RIGHT: 15px\" onmouseover=getTop().stopPropagation(event); onclick=\"getTop().preSwapLink(event, 'html', 'ZC1912-rQ7uXSy7P7HThxdLFCOuY92');\">\n" +
                    "<DIV id=qm_con_body>\n" +
                    "<DIV class=\"qmbox qm_con_body_content qqmail_webmail_only\" id=mailContentContainer>\n" +
                    "<DIV class=main style=\"OVERFLOW: hidden; WIDTH: 100%; BACKGROUND-COLOR: #f7f7f7\">\n" +
                    "<DIV class=content style=\"BORDER-TOP: #cccccc 1px solid; BORDER-RIGHT: #cccccc 1px solid; BACKGROUND: #ffffff; BORDER-BOTTOM: #cccccc 1px solid; PADDING-BOTTOM: 10px; PADDING-TOP: 10px; PADDING-LEFT: 25px; BORDER-LEFT: #cccccc 1px solid; MARGIN: 50px; PADDING-RIGHT: 25px\">\n" +
                    "<DIV class=header style=\"MARGIN-BOTTOM: 30px\">\n" +
                    "<P>亲爱的用户：</P></DIV>\n" +
                    "<P>您好！您正在进行邮箱验证，本次请求的验证码为：</P>\n" +
                    "<P><SPAN style=\"FONT-SIZE: 18px; FONT-WEIGHT: bold; COLOR: #f90\">" + random + "</SPAN><SPAN style=\"COLOR: #000000\">(为了保障您帐号的安全性，请在10分钟内完成验证)</SPAN></P>\n" +
                    "<DIV class=footer style=\"MARGIN-TOP: 30px\">\n" +
                    "<P>" + company + "</P>\n" +
                    "<P><SPAN style=\"BORDER-BOTTOM: #ccc 1px dashed; POSITION: relative; _display: inline-block\" t=\"5\" times=\"\" isout=\"0\">2019-02-12</SPAN></P></DIV>\n" +
                    "<DIV class=tip style=\"COLOR: #cccccc; TEXT-ALIGN: center\">该邮件为系统自动发送，请勿进行回复 </DIV></DIV></DIV></DIV></DIV></DIV></DIV></DIV></BODY>\n" +
                    "</html>\n";
            //设置验证码的样式
            mimeMessageHelper.setText(emailCodeText, true);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }

    /**
     * 发送手机验证码
     * @param mobile
     */
    @RequestMapping(value = "/getMobileCode/{mobile}", method = RequestMethod.GET)
    public boolean sendMobile(@PathVariable String mobile) throws ClientException {
        String code = RandomUtil.generateDigitalNumString(6);
        String messageJSON = "{\"code\":\"" + code + "\"}";
        //可自助调整超时时间
        /*System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");*/
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile(smsVo.regoinId, smsVo.accessKeyId, smsVo.secret);
       // DefaultProfile.addEndpoint(smsConfig.regoinId, smsConfig.regoinId, smsConfig.product, smsConfig.domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(mobile);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(smsVo.signName);    // TODO 修改成自己的
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(smsVo.templateCode);    // TODO 修改成自己的
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
//        request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");
        request.setTemplateParam(messageJSON);
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");
        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if(sendSmsResponse.getCode()!= null && sendSmsResponse.getCode().equals("OK")){
            return true;
        }
        return false;
    }

}