/** 
 * Project Name:ygbnet
 * File Name:IdentifieUtil.java
 * 
*/  
  
package com.web.common.utils.print;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * 上传附件、删除附件常用工具类
 *  
 * @author
 * @version
 */
@Slf4j
public class FileUploadUtil {
	
    /**
     * 
     * fileReplace:(将原路径替换成windows和Linux通用路径). <br/> 
     * 
     * @Title: fileReplace
     * @Description: TODO
     * @param path
     * @param request
     * @return    设定参数
     * @return String    返回类型
     * @throws
     */
    public static String fileReplace(String path,HttpServletRequest request){
        StringBuffer newPath = new StringBuffer();
        String[] pathArray = path.split("#");
        for(int i=0;i<pathArray.length;i++){
            newPath.append(pathArray[i]+File.separator);//将原路径替换成windows和Linux通用路径
        }
        //模拟有session时
        String s = request.getSession().getServletContext().getRealPath(newPath.substring(0,newPath.length()-1).toString());
        if (!s.endsWith(File.separator)) {
            s = s + File.separator;
            }
        return s;
    }
    
    /**
     * 获取文件内容
     * @param htmlName
     * @return
     * @throws IOException
     */
    public static String getHtmlContent(String htmlName) throws IOException{
    	String data = "";
        ClassPathResource cpr = new ClassPathResource("pdf-Template/" + htmlName);
        try {
            byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            data = new String(bdata, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.warn("IOException", e);
        }
        return data;
    }
    
    public static void main(String[] args) throws IOException {
    	getHtmlContent("printDemo.html");
	}
}
  