package com.web.core.util;

import com.sun.crypto.provider.SunJCE;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * DES加解密工具
 * @author
 */
public class DEScryptUtils {

    private Cipher encryptCipher = null;

    private Cipher decryptCipher = null;

    /**
     * 指定密钥构造
     * @param algorithm 算法（如：DES）
     * @param keyString 指定的密钥
     * @throws Exception
     */
    public DEScryptUtils(String keyString) throws Exception {
        Security.addProvider(new SunJCE());
        Key key = getKey(keyString.getBytes(UTF_8));

        encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    /**
     * 加密字符串
     * @param content 需加密的字符串
     * @return 加密后的字符串
     * @throws Exception
     */
    public String encrypt(String content) throws Exception {
        return Base64.encodeBase64String(encrypt(content.getBytes(UTF_8)));
        //return new String(decrypt(new BASE64Decoder().decodeBuffer(content)), UTF_8);
    }

    /**
     * 加密字节数组
     * @param byteContent 需加密的字节数组
     * @return 加密后的字节数组
     * @throws Exception
     */
    public byte[] encrypt(byte[] byteContent) throws Exception {
        return encryptCipher.doFinal(byteContent);
    }

    /**
     * 解密字节数组
     * @param bytes 需解密的字节数组
     * @return 解密后的字节数组
     * @throws Exception
     */
    public byte[] decrypt(byte[] bytes) throws Exception {
        return decryptCipher.doFinal(bytes);
    }

    /**
     * 解密字符串
     * @param content 需解密的字符串
     * @return 解密后的字符串
     * @throws Exception
     */
    public String decrypt(String content) throws Exception {
        return new String(decrypt(Base64.decodeBase64(content.getBytes(UTF_8))), UTF_8);
        //return new String(decrypt(new BASE64Decoder().decodeBuffer(content)), "UTF-8");
    }

    /**
     * 从指定字符串生成密钥
     * 密钥所需的字节数组长度为8位，
     * 不足8位时后面补0，
     * 超出8位只取前8位
     * @param keyBytes 构成该字符串的字节数组
     * @return 生成的密钥
     */
    private Key getKey(byte[] keyBytes) {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] byteArr = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < keyBytes.length && i < byteArr.length; i++) {
            byteArr[i] = keyBytes[i];
        }
        // 生成密钥
        return new SecretKeySpec(byteArr, "DES");
    }

    public static void main(String[] args) {
        try {
            DEScryptUtils inst = new DEScryptUtils("cryptPwd");
            String encryptStr = inst.encrypt("s中■dfafsafdsaf@#8sa082304230sd)*&(~!ysdfsd98f7s9afsf#97sd9fdsd9f$%d7s9df7");
            System.out.println(encryptStr);
            System.out.println(inst.decrypt(encryptStr));

            String hexString = DEScryptUtils.byteArrayHexString("abc英文s中@#8sa08".getBytes(UTF_8));
            System.out.println(hexString);
            System.out.println(new String(DEScryptUtils.hexStr2ByteArray(hexString), UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将byte数组转换为表示16进制值的字符串（互为可逆的转换过程）
     * @param bytes 需要转换的byte数组
     * @return 转换后的字符串
     */
    public static String byteArrayHexString(byte[] bytes) {
        int iLen = bytes.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuilder sb = new StringBuilder(iLen * 2);
        for (int aByte : bytes) {
            int intTmp = aByte;
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组（互为可逆的转换过程）
     * @param content 需要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static byte[] hexStr2ByteArray(String content) throws Exception {
        byte[] bytes = content.getBytes(UTF_8);
        int iLen = bytes.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(bytes, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

}