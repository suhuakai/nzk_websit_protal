package com.web.core.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


/**
 * 
 * 上传附件、删除附件常用工具类
 *  
 * @author
 * @version
 */
public class FileUploadUtil {
	
	/**
	 * 上传附件
	 * @return String
	 */
	public static boolean saveFile(MultipartFile file,String path,String filename,HttpServletRequest request){
	    String filePath = fileReplace(path,request);//将原路径替换成windows和Linux通用路径
	 // 判断文件是否为空  
        if (file.getSize() > 0) {
        	File root = new File(filePath);
            if(!root.exists()){//如果路径不存在就创建一个
                new File(filePath).mkdirs();
            }
            try {  
                // 转存文件  
                File f = new File(filePath+File.separator+filename
                        + file.getOriginalFilename().substring(
                                file.getOriginalFilename().lastIndexOf(
                                        ".")));
                file.transferTo(f);
                return true;  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
		return false;
	}
	
	/**
     * 删除附件
     * @return String
     */
    public static boolean deleteFile(String path,String uploadName,HttpServletRequest request){
     // 判断文件名是否为空  
        if (StringUtils.isNotBlank(path) && StringUtils.isNotBlank(uploadName)) {
            String filePath = fileReplace(path,request);//将原路径替换成windows和Linux通用路径
            File f = new File(filePath+File.separator+uploadName);
            if(!f.isDirectory()){//判断是否属于文件,如果属于文件就删除
                return f.delete();
            }
            return false; 
        }  
        return false;
    }
    
    /**
     * 
     * fileExist:(判断文件是否存在). <br/> 
     * 
     * @Title: fileExist
     * @Description: TODO
     * @param path 文件绝对路径
     * @param uploadName 文件名(带后缀名)
     * @param request
     * @return    设定参数
     * @return boolean    返回类型
     * @throws
     */
    public static boolean fileExist(String path,String uploadName,HttpServletRequest request){
        // 判断文件名是否为空  
           if (StringUtils.isNotBlank(path) && StringUtils.isNotBlank(uploadName)) {
               String filePath = fileReplace(path,request);//将原路径替换成windows和Linux通用路径
               File f = new File(filePath+File.separator+uploadName);
	           //判断路径存在并且属于文件
	           return f.exists() && !f.isDirectory();
           }
           return false;
       }
    
    /**
     * 
     * fileReplace:(将原路径替换成windows和Linux通用路径). <br/> 
     * 
     * @Title: fileReplace
     * @Description: TODO
     * @param path
     * @param uploadName
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
        String s = request.getSession().getServletContext().getRealPath(newPath.toString());
        if (!s.endsWith(File.separator)) {
            s = s + File.separator;
            }
        return s;
    }

    /**
     * 获得通用路径
     * @param path
     * @return
     */
    public static String fileReplace(String path){
        Map<String,String> map  = System.getenv();
        String os = map.get("OS");
        if(os == null){
            path = path.replaceAll("#",File.separator);
        }else if(os.contains("Win")){
            path = path.replaceAll("#","/");
        }else{
            path = path.replaceAll("#",File.separator);
        }

       // path = path.replaceAll("#",File.separator);
        return path;
    }
    
    /**
	 * 将InputStream写入本地文件
	 * @param destination 写入本地目录
	 * @param imput	输入流
	 * @throws IOException
	 */
    public static void writeToLocal(String destination, InputStream imput)
			throws IOException {
		int index;
		byte[] bytes = new byte[1024];
		FileOutputStream downloadFile = new FileOutputStream(destination);
		while ((index = imput.read(bytes)) != -1) {
			downloadFile.write(bytes, 0, index);
			downloadFile.flush();
		}
		downloadFile.close();
		imput.close();
		
    }
    
}
  