package com.web.common.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public class QRCodeUtils {

    /**
     * 二维码默认宽度
     */
    private static final int QRCODE_WIDTH = 300;

    /**
     * 二维码默认高度
     */
    private static final int QRCODE_HEIGHT = 300;

    /**
     * logo默认宽度
     */
    private static final int LOGO_WIDTH = 90;

    /**
     * logo默认高度
     */
    private static final int LOGO_HEIGHT = 90;

    /**
     * 默认二维码文件格式
     */
    private static final String FORMAT = "png";

    /**
     * 生成二维码参数
     */
    private static final Map<EncodeHintType, Object> ENCODE_HINTS = new HashMap();

    /**
     * 解析二维码参数
     */
    private static final Map<DecodeHintType, Object> DECODE_HINTS = new HashMap<>();

    static {
        /* 字符编码 */
        ENCODE_HINTS.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        /* 容错等级 L、M、Q、H 其中 L 为最低, H 为最高 */
        ENCODE_HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        /* 二维码与图片边距,，默认为0 */
        ENCODE_HINTS.put(EncodeHintType.MARGIN, 0);

        /* 字符编码 */
        DECODE_HINTS.put(DecodeHintType.CHARACTER_SET, "UTF-8");
    }

    /**
     * @title 生成二维码图片
     * @description 根据文本内容生成默认格式的二维图图片
     * @param text 文本内容
     * @param filePath 生成图片路径
     * @throws WriterException
     * @throws IOException
     */
    public static void create(String text, String filePath) throws WriterException, IOException {
        BitMatrix bitMatrix = createBitMatrix(text, BarcodeFormat.QR_CODE, QRCODE_WIDTH, QRCODE_HEIGHT, ENCODE_HINTS);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, FORMAT, path);
    }

    /**
     * @title 生成二维码图片
     * @description 根据文本内容生成二维码图片的字节输出流
     * @param text 文本内容
     * @return 字节输出流
     * @throws WriterException
     * @throws IOException
     */
    public static ByteArrayOutputStream create(String text) throws WriterException, IOException {
        BitMatrix bitMatrix = createBitMatrix(text, BarcodeFormat.QR_CODE, QRCODE_WIDTH, QRCODE_HEIGHT, ENCODE_HINTS);
        return writeToStream(bitMatrix, FORMAT);
    }

    /**
     * @title 生成二维码图片
     * @description 根据文本内容，自定义宽度高度，生成所需要的二维码图片
     * @param text 文本内容
     * @param filePath 生成图片文件路径
     * @param width 宽度
     * @param height 高度
     */
    public static void create(String text, String filePath, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = createBitMatrix(text, BarcodeFormat.QR_CODE, width, height, ENCODE_HINTS);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, FORMAT, path);
    }

    /**
     * @title 生成二维码图片
     * @description 根据文本内容，自定义宽度高度，生成所需要的二维码图片
     * @param text 文本内容
     * @param filePath 生成图片文件路径
     * @param width 宽度
     * @param height 高度
     * @param onColor 二维码颜色
     * @param offColor 二维码背景颜色
     */
    public static void create(String text, String filePath, int width, int height, Color onColor, Color offColor) throws WriterException, IOException {
        BitMatrix bitMatrix = createBitMatrix(text, BarcodeFormat.QR_CODE, width, height, ENCODE_HINTS);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, FORMAT, path, new MatrixToImageConfig(onColor.getRGB(), offColor.getRGB()));
    }

    /**
     * @title 生成二维码图片
     * @description 根据文本内容生成二维码图片的字节输出流
     * @param text 文本内容
     * @param width 宽度
     * @param height 高度
     * @return 字节输出流
     */
    public static ByteArrayOutputStream create(String text, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = createBitMatrix(text, BarcodeFormat.QR_CODE, width, height, ENCODE_HINTS);
        return writeToStream(bitMatrix, FORMAT);
    }

    /**
     * @title 生成二维码图片
     * @description 根据文本内容，自定义宽度高度，自定义图片格式，生成所需要的二维码图片
     * @param text 文本内容
     * @param filePath 生成图片文件路径
     * @param width 宽度
     * @param height 高度
     * @param format 图片格式
     */
    public static void create(String text, String filePath, int width, int height, String format) throws WriterException, IOException {
        BitMatrix bitMatrix = createBitMatrix(text, BarcodeFormat.QR_CODE, width, height, ENCODE_HINTS);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, format, path);
    }

    /**
     * @title 生成二维码图片
     * @description 根据文本内容生成二维码图片的字节输出流
     * @param text 文本内容
     * @param width 宽度
     * @param height 高度
     * @param format 自定义图片格式
     * @return 字节输出流
     */
    public static ByteArrayOutputStream create(String text, int width, int height, String format) throws WriterException, IOException {
        BitMatrix bitMatrix = createBitMatrix(text, BarcodeFormat.QR_CODE, width, height, ENCODE_HINTS);
        return writeToStream(bitMatrix, format);
    }

    /**
     * @title 生成二维码图片
     * @description 根据文本内容，自定义宽度高度，自定义图片格式，自定义配置信息，生成所需要的二维码图片
     * @param text 文本内容
     * @param filePath 生成图片文件路径
     * @param width 宽度
     * @param height 高度
     * @param format 图片格式
     * @param hintTypes 配置信息
     */
    public static void create(String text, String filePath, int width, int height, String format, Map<EncodeHintType, Object> hintTypes) throws WriterException, IOException {
        BitMatrix bitMatrix = createBitMatrix(text, BarcodeFormat.QR_CODE, width, height, hintTypes);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, format, path);
    }

    /**
     * @title 生成二维码图片
     * @param text 文本内容
     * @param width 宽度
     * @param height 高度
     * @param format 图片格式
     * @param hintTypes 配置信息
     * @return 字节输出流
     * @throws WriterException
     * @throws IOException
     */
    public static ByteArrayOutputStream create(String text, int width, int height, String format,  Map<EncodeHintType, Object> hintTypes) throws WriterException, IOException {
        BitMatrix bitMatrix = createBitMatrix(text, BarcodeFormat.QR_CODE, width, height, hintTypes);
        return writeToStream(bitMatrix, format);
    }

    /**
     * @title 生成BitMatrix
     * @param text 文本内容
     * @param barcodeFormat 二维码格式
     * @param width 宽度
     * @param height 高度
     * @param hintTypes 配置
     * @return BitMatrix
     * @throws WriterException
     */
    private static BitMatrix createBitMatrix(String text, BarcodeFormat barcodeFormat, int width, int height, Map<EncodeHintType, Object> hintTypes) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        return qrCodeWriter.encode(text, barcodeFormat, width, height, hintTypes);
    }

    /**
     * 转成字符输出流
     * @param bitMatrix bitMatrix
     * @param format 图片格式
     * @return
     * @throws IOException
     */
    private static ByteArrayOutputStream writeToStream(BitMatrix bitMatrix, String format) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);
        return outputStream;
    }

    /**
     * 解析二维码
     * @param binaryBitmap 二维码图类
     * @return 解析信息
     * @throws FormatException
     * @throws ChecksumException
     * @throws NotFoundException
     */
    private static Result readQRcode(BinaryBitmap binaryBitmap, Map<DecodeHintType, Object> decodeHints) throws FormatException, ChecksumException, NotFoundException {
        QRCodeReader reader = new QRCodeReader();
        return reader.decode(binaryBitmap, decodeHints);
    }


}
