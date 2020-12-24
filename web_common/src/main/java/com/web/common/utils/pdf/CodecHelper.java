package com.web.common.utils.pdf;

import com.web.core.exception.ValidationException;
import com.web.core.util.LocalAssert;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import java.io.*;

/**
 * 编码格式处理辅助工具
 * @author
 * @version 1.0
 */
public class CodecHelper {

    public static String encodeBase64(String str, String charset) throws UnsupportedEncodingException {
        return Base64.encodeBase64String(str.getBytes(charset));
    }

    /**
     * Base64字符串解码
     * @param base64Str
     * @throws IOException
     */
    public static String decodeBase64(String base64Str) throws IOException {
        return decodeBase64(base64Str, "UTF-8");
    }

    /**
     * Base64字符串解码
     * @param base64Str
     * @param charset
     * @throws IOException
     */
    public static String decodeBase64(String base64Str, String charset) throws IOException {
        return new String(Base64.decodeBase64(base64Str.getBytes(charset)), charset);
    }

    /**
     * 生成base64格式二维码
     * @param qrtext
     * @param imageType
     * @return String
     * @throws ValidationException
     * @author
     *      */
    public static String toBase64QRCode(String qrtext, ImageType imageType) throws ValidationException {
        LocalAssert.notEmpty(qrtext, "生成二维码：必需指定qrtext");
        LocalAssert.notNull(imageType, "生成二维码：必需指定imageType");
        ByteArrayOutputStream out = QRCode.from(qrtext).to(imageType).stream();
        //对字节数组Base64编码
        return Base64.encodeBase64String(out.toByteArray());
    }

    /**
     * 生成base64格式二维码（网页嵌入式图片）
     * @param qrtext
     * @param imageType
     * @return String
     * @throws ValidationException
     * @author
     *      */
    public static String toBase64QRCodeForHtml(String qrtext, ImageType imageType) throws ValidationException {
        return "data:image/" + imageType.name().toLowerCase() + ";base64," + toBase64QRCode(qrtext, imageType);
    }

    /**
     * base64格式文件转成数据流
     * @param base64File
     * @return InputStream
     * @author
     *
     */
    public static InputStream decodeBase64File(String base64File) {
        if (StringUtils.isBlank(base64File)) {
            return null;
        }
        String base64Content = base64File.replaceAll("data:.*;base64,", "");
        return new BufferedInputStream(new ByteArrayInputStream(Base64.decodeBase64(base64Content)));
    }

    /**
     * 从base64格式文档中分析出文件类型
     * @param base64File
     * @return String
     */
    public static String fileTypeFromBase64(String base64File) throws ValidationException {
        if (StringUtils.isBlank(base64File)) {
            return "";
        }
        //获取文件类型
        String prefix = base64File.substring(0, base64File.indexOf(";base64,") + 8);
        int beginIndex = prefix.indexOf("/");
        int endIndex = prefix.indexOf(";base64,");
        String fileType = beginIndex >= 0 ? prefix.substring(beginIndex + 1, endIndex) : "";
        if (StringUtils.isNotBlank(fileType)) {
            if ("vnd.openxmlformats-officedocument.wordprocessingml.document".equalsIgnoreCase(fileType)) {
                fileType = "docx";
            }
            fileType = "." + fileType;
        } else {
            throw new ValidationException("未知的上传文件类型!");
        }
        return fileType;
    }

    /**
     * 本地图片转换成base64字符串
     * @param imgFile 图片本地路径
     * @return String
     */
    public static String localImageToBase64(String imgFile) throws IOException {
        InputStream in = new FileInputStream(imgFile);
        byte[] data = new byte[in.available()];
        try {
            in.read(data);
        } finally {
            in.close();
        }
        return Base64.encodeBase64String(data);
    }

    public static void main(String[] args) throws IOException {
        //System.out.println(fileTypeFromBase64("data:.*;base64,fa/fs+ad/fas+fa/sfas"));
        //System.out.println(fileTypeFromBase64("data:image/png;base64,fa/fs+ad/fas+fa/sfas"));

        String base64ImgString = toBase64QRCode("中华民族", ImageType.PNG);
        System.out.println(base64ImgString);

        String base64String = encodeBase64("中华民族", "UTF-8");
        System.out.println(base64String);
        System.out.println(decodeBase64(base64String));
    }

}
