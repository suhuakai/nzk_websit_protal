package com.web.common.utils.pdf;

import com.itextpdf.text.pdf.BaseFont;
import com.web.core.annotation.Evaluate;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Pdf生成工具
 * @author
 *
 * @since JDK 1.8
 */
public class PdfUtils {

    public final static Logger logger = LoggerFactory.getLogger(PdfUtils.class);
    private static Configuration configuration;
    private static String osName;

    static {
        //初始化Freemarker配置
        configuration = new Configuration(Configuration.getVersion());
        configuration.setDefaultEncoding("utf-8");
        configuration.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
        configuration.setClassicCompatible(true);
        configuration.setTemplateUpdateDelayMilliseconds(0);
        configuration.setLocale(Locale.CHINA);
        logger.info("已经初始化Freemarker配置。");
    }

    public static void main(String[] args) throws Exception {

    }

    /**
     * html模版渲染填充输出
     * @param ftlFile 模板文件
     * @param data 填充数据
     * @param response 响应对象
     * @author
     *      */
    public static void renderPdf(String ftlFile, Object data, HttpServletResponse response) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            //html模版渲染填充输出
            renderPdf(new File(ftlFile), data, output);
            //输出到用户浏览器
            response.setContentType("application/pdf");
            //response.setHeader("Content-disposition", "attachment;filename=ExportPdf.pdf");
            response.setContentLength(output.size());
            ServletOutputStream out = response.getOutputStream();
            output.writeTo(out);
            out.flush();
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    /**
     * html模版渲染填充输出
     * @param ftlFile 模板文件
     * @param data 填充数据
     * @param out 输出流
     * @author
     *      */
    @Evaluate
    public static void renderPdf(File ftlFile, Object data, OutputStream out) throws Exception {
        String html = renderFtl(ftlFile, data);
        /*
         * try {
         * 		IOUtils.write(html, htm);
         * } finally {
         * 		IOUtils.closeQuietly(htm);
         * }
         */
        try {
            ITextRenderer render = new ITextRenderer();
            ITextFontResolver fontResolver = render.getFontResolver();
            if (osName == null) {
                osName = System.getProperty("os.name", "").toLowerCase();
            }
            //windows系统默认simsun字体位置，没有就要安装字体
            if (osName.contains("windows")) {
                fontResolver.addFont("C:/Windows/Fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            }
            //linux系统默认simsun字体位置，没有就要安装字体
            else if (osName.contains("linux")) {
                fontResolver.addFont("/usr/share/fonts/chinese/TrueType/SIMSUN.TTC", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            }
            //苹果系统默认simsun字体位置，没有就要安装字体
            else if (osName.contains("mac os x")) {
            	//fontResolver.addFont("/font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            	fontResolver.addFont("/font/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            }
            render.setDocumentFromString(html);
            render.layout();
            render.createPDF(out);
            render.finishPDF();
        } finally {
            out.close();
        }
    }

    /**
     * html模版渲染填充
     * @param ftlFile 模板文件
     * @param data 填充数据
     * @return String
     * @author
     *      */
    @Evaluate
    public static String renderFtl(File ftlFile, Object data) throws Exception {
        //设置模板目录
        configuration.setDirectoryForTemplateLoading(ftlFile.getParentFile());
        //获取模板
        Template template = configuration.getTemplate(ftlFile.getName());
        StringWriter writer = new StringWriter();
        //模版渲染填充
        template.process(data, writer);
        writer.flush();
        return writer.toString();
    }

}
