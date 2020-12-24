package com.web.common.utils.excel;

import com.google.common.collect.Lists;
import com.web.common.utils.DateUtils;
import com.web.common.utils.StringUtils;
import com.web.common.utils.bean2map.BeanMapConvertUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * 常用数据视图导出导入工具
 *
 * @version 1.0
 */
public class ExportUtil<T> {

    public final static Logger logger = LoggerFactory.getLogger(ExportUtil.class);
    //导入excel版本
    private final static String XLS = "xls";
    private final static String XLSX = "xlsx";

    //时间格式
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 根据给定数据源，导出EXCEL文档
     *
     * @param response  HttpServletResponse
     * @param fieldName excel每一列的表头.
     * @param dataList  excel数据，其中的key和表头中的值对应，value为数据
     * @param title     表格标题，在表格最上面
     * @param before    表格页眉（比如制单时间，科室之类的数据），紧跟标题，list中的值由逗号(英文)分隔数据（单元格），每一个String为一排
     * @param after     表格页尾（比如制单时间，科室之类的数据），在表格末尾，list中的值由逗号(英文)分隔数据（单元格），每一个String为一排
     * @param formatMap 数据格式，其中的key和表头中的key对应，value是格式编码。（如果为日期，则传日期格式yyyy-MM-dd之类的， 如果是数字，想保留小数位数，保留1位小数传0.0，保留2位小数传0.00，以此类推）
     * @param excelName
     * @throws Exception
     */
    public static void exportExcel(HttpServletResponse response, List<String> fieldName,
                                   List<Map<String, Object>> dataList, Map<String, String> formatMap, String title, List<String> before,
                                   List<String> after, String excelName) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(title);

        /**样式定义*/
        //居中样式
        HSSFCellStyle centerStyle = workbook.createCellStyle();//设置格式
        centerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        centerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        //居中样式
        HSSFCellStyle rightStyle = workbook.createCellStyle();//设置格式
        rightStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//右对齐
        rightStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        //创建顶部标题样式
        HSSFCellStyle cellTitleStyle = workbook.createCellStyle();
        cellTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        cellTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        //创建顶部标题字体
        HSSFFont fontTitle = workbook.createFont();
        //fontTitle.setFontName("宋体");
        fontTitle.setFontHeightInPoints((short) 16);
        fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellTitleStyle.setFont(fontTitle);
        //表头列名样式
        HSSFCellStyle colNameStype = workbook.createCellStyle();
        colNameStype.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        colNameStype.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        colNameStype.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);//灰色25
        colNameStype.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充

        /**表名（大标题）*/
        for (int i = 0; i < fieldName.size(); i++) {
            sheet.setDefaultColumnStyle(i, centerStyle);
            sheet.setColumnWidth(i, 4500);
        }

        /**创建标题*/
        int rowNum = 0;//第一行
        HSSFRow rowTitle = sheet.createRow(rowNum++);
        rowTitle.setHeightInPoints(40);
        HSSFCell cellTitle = rowTitle.createCell(0);//表名单元格
        cellTitle.setCellValue(title);
        cellTitle.setCellStyle(cellTitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) (fieldName.size() - 1)));

        if (CollectionUtils.isNotEmpty(before)) {
            //创建页眉
            for (String item : before) {
                HSSFRow rowHeader = sheet.createRow(rowNum++);
                rowHeader.setHeightInPoints(18);
                if (StringUtils.isEmpty(item)) {
                    continue;
                }
                String[] strs = item.split(",");
                for (int i = 0; i < strs.length; i++) {
                    HSSFCell cell = rowHeader.createCell(i);//创建单元格
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);//定义单元格为字符串类型
                    cell.setCellValue(strs[i]);
                }
            }
        }

        //创建表格列表(DataList)列标
        HSSFRow rowTableHead = sheet.createRow(rowNum++);
        rowTableHead.setHeight((short) 400);
        for (int i = 0; i < fieldName.size(); i++) {
            HSSFCell cell = rowTableHead.createCell(i);//创建单元格
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);//定义单元格为字符串类型
            cell.setCellValue(fieldName.get(i));
            cell.setCellStyle(colNameStype);
        }

        //创建表格列表(DataList)数据区域
        for (int i = 0; i < dataList.size(); i++) {
            HSSFRow row = sheet.createRow(rowNum++);//创建行
            row.setHeight((short) 400);
            for (int j = 0; j < fieldName.size(); j++) {
                HSSFCell cell = row.createCell(j);//创建单元格
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);//定义单元格为字符串类型
                Object columnValue = dataList.get(i).get(fieldName.get(j));//数据
                String columnValueFomart = MapUtils.isEmpty(formatMap) ? null : formatMap.get(fieldName.get(j));//数据格式
                if (columnValue instanceof Date) {
                    //日期格式化
                    if (StringUtils.isBlank(columnValueFomart)) {
                        columnValueFomart = TIME_FORMAT;
                    }
                    cell.setCellValue(DateUtils.format((Date) columnValue, columnValueFomart));
                } else if (columnValue instanceof Number) {
                    //数字格式化
                    if (null == columnValueFomart) {
                        cell.setCellValue(columnValue.toString());
                    } else {
                        DecimalFormat df1 = new DecimalFormat(columnValueFomart);
                        cell.setCellValue(df1.format(columnValue));
                    }
                    cell.setCellStyle(rightStyle);
                } else {
                    cell.setCellValue(columnValue == null ? "" : columnValue.toString());
                }
            }
        }

        if (CollectionUtils.isNotEmpty(after)) {
            // 创建页尾
            for (String string : after) {
                HSSFRow fieldRows = sheet.createRow(rowNum);
                rowNum++;
                if (StringUtils.isEmpty(string)) {
                    continue;
                }
                String[] strs = string.split(",");
                for (int i = 0; i < strs.length; i++) {
                    // 创建单元格
                    HSSFCell cell = fieldRows.createCell(i);

                    // 定义单元格为字符串类型
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(strs[i]);
                }
            }
        }
        //导出Excel文件流到浏览器
        sendExcel(response, workbook, excelName);
    }

    /**
     * 导出Excel文件流到浏览器
     *
     * @param response
     * @param workbook
     * @param excelName
     * @return void
     * @throws Exception
     */
    public static void sendExcel(HttpServletResponse response, HSSFWorkbook workbook, String excelName) throws Exception {
        if (workbook != null) {
            response.setContentType("application/vnd.ms-excel");

            //response.setHeader("Content-disposition", "attachment;filename=" + new String(excelName.getBytes("gb2312"), "ISO8859-1") + ".xls");
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(excelName, "UTF-8") + ".xls");
            //response.setHeader("Content-disposition", "attachment;filename=" + new String(excelName.getBytes("gb2312"), "ISO8859-1") + ".xls");
            //response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            OutputStream ouputStream = null;
            try {
                ouputStream = response.getOutputStream();
                workbook.write(ouputStream);
                ouputStream.flush();
            } catch (IOException e) {
                logger.error("导出Excel文件流到浏览器的时候异常!", e);
            } finally {
                try {
                    ouputStream.close();
                } catch (IOException e) {
                    logger.error("关闭OutputStream异常!", e);
                }
            }
        }
    }


    /**
     * 创建表格
     *
     * @param response
     * @param fieldName
     * @param dataList
     * @param formatMap
     * @param title
     * @param before
     * @param after
     * @param excelName
     * @param excelmap
     * @throws Exception
     */
    public static <T> void exportExcel(HttpServletResponse response,
                                       List<T> list, Map<String, String> formatMap, String title, List<String> before,
                                       List<String> after, String excelName, Map<String, String> excelmap, Class<T> clazz) throws Exception {

        List<Map<String, Object>> dataList = BeanMapConvertUtil.beanToMap(list, clazz);

        List<String> fieldName = null;
        if (null != excelmap) {
            fieldName = Lists.newArrayList();
            Set<Entry<String, String>> entrySet = excelmap.entrySet();
            if (null != entrySet) {
                for (Entry<String, String> entry : entrySet) {
                    fieldName.add(entry.getKey());
                }
            }
        }


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(title);

        /**样式定义*/
        //居中样式
        HSSFCellStyle centerStyle = workbook.createCellStyle();//设置格式
        centerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        centerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        //居中样式
        HSSFCellStyle rightStyle = workbook.createCellStyle();//设置格式
        rightStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//右对齐
        rightStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        //创建顶部标题样式
        HSSFCellStyle cellTitleStyle = workbook.createCellStyle();
        cellTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        cellTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        //创建顶部标题字体
        HSSFFont fontTitle = workbook.createFont();
        //fontTitle.setFontName("宋体");
        fontTitle.setFontHeightInPoints((short) 16);
        fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellTitleStyle.setFont(fontTitle);
        //表头列名样式
        HSSFCellStyle colNameStype = workbook.createCellStyle();
        colNameStype.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        colNameStype.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        colNameStype.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);//灰色25
        colNameStype.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充

        /**表名（大标题）*/
        for (int i = 0; i < fieldName.size(); i++) {
            sheet.setDefaultColumnStyle(i, centerStyle);
            sheet.setColumnWidth(i, 4500);
        }

        /**创建标题*/
        int rowNum = 0;//第一行
        HSSFRow rowTitle = sheet.createRow(rowNum++);
        rowTitle.setHeightInPoints(40);
        HSSFCell cellTitle = rowTitle.createCell(0);//表名单元格
        cellTitle.setCellValue(title);
        cellTitle.setCellStyle(cellTitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) (fieldName.size() - 1)));

        if (CollectionUtils.isNotEmpty(before)) {
            //创建页眉
            for (String item : before) {
                HSSFRow rowHeader = sheet.createRow(rowNum++);
                rowHeader.setHeightInPoints(18);
                if (StringUtils.isEmpty(item)) {
                    continue;
                }
                String[] strs = item.split(",");
                for (int i = 0; i < strs.length; i++) {
                    HSSFCell cell = rowHeader.createCell(i);//创建单元格
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);//定义单元格为字符串类型
                    cell.setCellValue(strs[i]);
                }
            }
        }

        //创建表格列表(DataList)列标
        HSSFRow rowTableHead = sheet.createRow(rowNum++);
        rowTableHead.setHeight((short) 400);
        for (int i = 0; i < fieldName.size(); i++) {
            HSSFCell cell = rowTableHead.createCell(i);//创建单元格
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);//定义单元格为字符串类型
            cell.setCellValue(excelmap.get(fieldName.get(i)));
            cell.setCellStyle(colNameStype);
        }

        //创建表格列表(DataList)数据区域
        for (int i = 0; i < dataList.size(); i++) {
            HSSFRow row = sheet.createRow(rowNum++);//创建行
            row.setHeight((short) 400);
            for (int j = 0; j < fieldName.size(); j++) {
                HSSFCell cell = row.createCell(j);//创建单元格
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);//定义单元格为字符串类型
                Object columnValue = dataList.get(i).get(fieldName.get(j));//数据
                String columnValueFomart = MapUtils.isEmpty(formatMap) ? null : formatMap.get(fieldName.get(j));//数据格式
                if (columnValue instanceof Date) {
                    //日期格式化
                    if (StringUtils.isBlank(columnValueFomart)) {
                        columnValueFomart = TIME_FORMAT;
                    }
                    cell.setCellValue(DateUtils.format((Date) columnValue, columnValueFomart));
                } else if (columnValue instanceof Number) {
                    //数字格式化
                    if (null == columnValueFomart) {
                        cell.setCellValue(columnValue.toString());
                    } else {
                        DecimalFormat df1 = new DecimalFormat(columnValueFomart);
                        cell.setCellValue(df1.format(columnValue));
                    }
                    cell.setCellStyle(rightStyle);
                } else {
                    cell.setCellValue(columnValue == null ? "" : columnValue.toString());
                }
            }
        }

        if (CollectionUtils.isNotEmpty(after)) {
            // 创建页尾
            for (String string : after) {
                HSSFRow fieldRows = sheet.createRow(rowNum);
                rowNum++;
                if (StringUtils.isEmpty(string)) {
                    continue;
                }
                String[] strs = string.split(",");
                for (int i = 0; i < strs.length; i++) {
                    // 创建单元格
                    HSSFCell cell = fieldRows.createCell(i);

                    // 定义单元格为字符串类型
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(strs[i]);
                }
            }
        }
        //导出Excel文件流到浏览器
        sendExcel(response, workbook, excelName);
    }

    /**
     * 创建表格
     *
     * @param response
     * @param fieldName
     * @param dataList
     * @param formatMap
     * @param title
     * @param before
     * @param after
     * @param excelName
     * @param excelmap
     * @throws Exception
     */
    public static void exportExcel(HttpServletResponse response, List<String> fieldName,
                                   List<Map<String, Object>> dataList, Map<String, String> formatMap, String title, List<String> before,
                                   List<String> after, String excelName, Map<String, String> excelmap) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(title);

        /**样式定义*/
        //居中样式
        HSSFCellStyle centerStyle = workbook.createCellStyle();//设置格式
        centerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        centerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        //居中样式
        HSSFCellStyle rightStyle = workbook.createCellStyle();//设置格式
        rightStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//右对齐
        rightStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        //创建顶部标题样式
        HSSFCellStyle cellTitleStyle = workbook.createCellStyle();
        cellTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        cellTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        //创建顶部标题字体
        HSSFFont fontTitle = workbook.createFont();
        //fontTitle.setFontName("宋体");
        fontTitle.setFontHeightInPoints((short) 16);
        fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellTitleStyle.setFont(fontTitle);
        //表头列名样式
        HSSFCellStyle colNameStype = workbook.createCellStyle();
        colNameStype.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        colNameStype.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        colNameStype.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);//灰色25
        colNameStype.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充

        /**表名（大标题）*/
        for (int i = 0; i < fieldName.size(); i++) {
            sheet.setDefaultColumnStyle(i, centerStyle);
            sheet.setColumnWidth(i, 4500);
        }

        /**创建标题*/
        int rowNum = 0;//第一行
        HSSFRow rowTitle = sheet.createRow(rowNum++);
        rowTitle.setHeightInPoints(40);
        HSSFCell cellTitle = rowTitle.createCell(0);//表名单元格
        cellTitle.setCellValue(title);
        cellTitle.setCellStyle(cellTitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) (fieldName.size() - 1)));

        if (CollectionUtils.isNotEmpty(before)) {
            //创建页眉
            for (String item : before) {
                HSSFRow rowHeader = sheet.createRow(rowNum++);
                rowHeader.setHeightInPoints(18);
                if (StringUtils.isEmpty(item)) {
                    continue;
                }
                String[] strs = item.split(",");
                for (int i = 0; i < strs.length; i++) {
                    HSSFCell cell = rowHeader.createCell(i);//创建单元格
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);//定义单元格为字符串类型
                    cell.setCellValue(strs[i]);
                }
            }
        }

        //创建表格列表(DataList)列标
        HSSFRow rowTableHead = sheet.createRow(rowNum++);
        rowTableHead.setHeight((short) 400);
        for (int i = 0; i < fieldName.size(); i++) {
            HSSFCell cell = rowTableHead.createCell(i);//创建单元格
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);//定义单元格为字符串类型
            cell.setCellValue(excelmap.get(fieldName.get(i)));
            cell.setCellStyle(colNameStype);
        }

        //创建表格列表(DataList)数据区域
        for (int i = 0; i < dataList.size(); i++) {
            HSSFRow row = sheet.createRow(rowNum++);//创建行
            row.setHeight((short) 400);
            for (int j = 0; j < fieldName.size(); j++) {
                HSSFCell cell = row.createCell(j);//创建单元格
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);//定义单元格为字符串类型
                Object columnValue = dataList.get(i).get(fieldName.get(j));//数据
                String columnValueFomart = MapUtils.isEmpty(formatMap) ? null : formatMap.get(fieldName.get(j));//数据格式
                if (columnValue instanceof Date) {
                    //日期格式化
                    if (StringUtils.isBlank(columnValueFomart)) {
                        columnValueFomart = TIME_FORMAT;
                    }
                    cell.setCellValue(DateUtils.format((Date) columnValue, columnValueFomart));
                } else if (columnValue instanceof Number) {
                    //数字格式化
                    if (null == columnValueFomart) {
                        cell.setCellValue(columnValue.toString());
                    } else {
                        DecimalFormat df1 = new DecimalFormat(columnValueFomart);
                        cell.setCellValue(df1.format(columnValue));
                    }
                    cell.setCellStyle(rightStyle);
                } else {
                    cell.setCellValue(columnValue == null ? "" : columnValue.toString());
                }
            }
        }

        if (CollectionUtils.isNotEmpty(after)) {
            // 创建页尾
            for (String string : after) {
                HSSFRow fieldRows = sheet.createRow(rowNum);
                rowNum++;
                if (StringUtils.isEmpty(string)) {
                    continue;
                }
                String[] strs = string.split(",");
                for (int i = 0; i < strs.length; i++) {
                    // 创建单元格
                    HSSFCell cell = fieldRows.createCell(i);

                    // 定义单元格为字符串类型
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(strs[i]);
                }
            }
        }
        //导出Excel文件流到浏览器
        sendExcel(response, workbook, excelName);
    }


    /*************************导入*************************/
    /**
     * 通用导入excel方法
     *
     * @param formFile 文件
     * @param clas     映射的类
     * @return 返回集合对象
     * @throws Exception
     */
    public static <T> List<T> readExcel(MultipartFile formFile, Class clas) throws Exception {
        //检查文件
        checkFile(formFile);
        //获得工作簿对象
        Workbook workbook = getWorkBook(formFile);
        //创建返回对象，把每行中的值作为一个数组，所有的行作为一个集合返回
        List<T> list = new ArrayList<T>();

        //保存键值（对象属性和值）
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != workbook) {
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {

                //获取当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (null == sheet) {
                    continue;
                }
                //获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第一行之外的所有行
                for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                    //获得对象实例
                    Object obj = clas.newInstance();
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    //后的当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    //获得当前行的列数
                    int lastCellNum = row.getPhysicalNumberOfCells();
                    //循环当前行
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        Field[] field = clas.getDeclaredFields();
                        Cell cell = row.getCell(cellNum);
                        //得到对应的键值
                        map.put(field[cellNum].getName(), getCellValue(cell));
                    }
                    //通过属性名对对象进行赋值操作
                    setFieldValue(obj, map);
                    list.add((T) obj);
                }
            }
        }
        return list;
    }


    public static <T> List<T> readExcelList(MultipartFile formFile, Class clas, Integer sheetNum) throws Exception {
        //检查文件
        checkFile(formFile);
        //获得工作簿对象
        Workbook workBook =getWorkBook(formFile);
        //创建返回对象，把每行中的值作为一个数组，所有的行作为一个集合返回
        List<T> list = new ArrayList<T>();

        //保存键值（对象属性和值）
        Map<String, Object> map;
        if (null != workBook) {
            //获取当前sheet工作表
            Sheet sheet = workBook.getSheetAt(sheetNum);
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum();
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            //循环除了第一行之外的所有行
            for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                //获得对象实例
                Object obj = clas.newInstance();
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                //后的当前行的开始列
                int firstCellNum = row.getFirstCellNum();
                //获得当前行的列数,getPhysicalNumberOfCells只能获取所有不为空的字段
                int lastCellNum =row.getLastCellNum();
                //循环当前行
                for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                    Field[] field = clas.getDeclaredFields();
                    Cell cell = row.getCell(cellNum);
                    //得到对应的键值
                    map = new HashMap<>();
                    map.put(field[cellNum].getName(), getCellValue(cell));
                    //通过属性名对对象进行赋值操作
                    setFieldValue(obj, map);
                }

                list.add((T) obj);

            }
        }
        return list;
    }


    /**
     * 为bean赋值属性值
     *
     * @param bean
     * @param valMap
     */
    public static void setFieldValue(Object bean, Map<String, Object> valMap) {
        Class<?> cls = bean.getClass();
        // 取出bean里的所有方法
        Method[] methods = cls.getDeclaredMethods();
        //取出bean 里面的属性
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            try {
                //通过属性名找到对应的set方法
                String fieldSetName = parSetName(field.getName());
                //判断set方法是否存在
                if (!checkSetMet(methods, fieldSetName)) {
                    continue;
                }
                //通过set方法和属性类型得到对应的方法
                Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());

                //通过属性名得到对应的值
                String value = (String) valMap.get(field.getName());

                if (null != value && !"".equals(value)) {
                    //得到属性类型的简写 如：java.lang.String 得到String
                    String fieldType = field.getType().getSimpleName();

                    //判断属性类型 为其进行转换
                    if ("String".equals(fieldType)) {
                        //赋值
                        fieldSetMet.invoke(bean, value);
                    } else if ("Date".equals(fieldType)) {
                        Date temp = parseDate(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
                        Integer intval = Integer.parseInt(value);
                        fieldSetMet.invoke(bean, intval);
                    } else if ("Long".equalsIgnoreCase(fieldType)) {
                        Long temp = Long.parseLong(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Double".equalsIgnoreCase(fieldType)) {
                        Double temp = Double.parseDouble(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                        Boolean temp = Boolean.parseBoolean(value);
                        fieldSetMet.invoke(bean, temp);
                    } else {

                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
    }


    /**
     * 判断是否存在某属性的 set方法
     *
     * @param methods
     * @param fieldSetMet
     * @return boolean
     */
    private static boolean checkSetMet(Method[] methods, String fieldSetMet) {
        for (Method met : methods) {
            if (fieldSetMet.equals(met.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 格式化string为Date
     *
     * @param datestr
     * @return date
     */
    private static Date parseDate(String datestr) {
        if (null == datestr || "".equals(datestr)) {
            return null;
        }
        try {
            String fmtstr = null;
            if (datestr.indexOf(':') > 0) {
                if (datestr.indexOf("-") > 0) {
                    fmtstr = "yyyy-MM-dd HH:mm:ss";
                } else if (datestr.indexOf("/") > 0) {
                    fmtstr = "yyyy-MM-dd HH:mm:ss";
                } else {
                    fmtstr = "HH:mm:ss";
                }
            } else {
                fmtstr = "yyyy-MM-dd";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(fmtstr);
            return sdf.parse(datestr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 拼接在某属性的 set方法
     *
     * @param fieldName
     * @return String
     */
    private static String parSetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return "set" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);
    }

    /**
     * 获取当前行数据
     *
     * @param cell
     * @return
     */
    @SuppressWarnings("deprecation")
    private static String getCellValue(Cell cell) {
        String cellValue = "";

        if (cell == null) {
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                //用于转化为日期格式
                Date d = cell.getDateCellValue();
                DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                cellValue = formater.format(d);
                return cellValue;
            } else {
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }
        }

        //判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC://数字0
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING://字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN://Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA://公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK://空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR://故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }


    /**
     * 获得工作簿对象
     *
     * @param formFile
     * @return
     */
    public static Workbook getWorkBook(MultipartFile formFile) {
        //获得文件名
        //	String fileName = formFile.getName();
        String fileName = formFile.getOriginalFilename();
        //创建Workbook工作簿对象，表示整个excel
        Workbook workbook = null;
        try {
            //获得excel文件的io流
            InputStream is = formFile.getInputStream();
            //根据文件后缀名不同（xls和xlsx）获得不同的workbook实现类对象
            if (fileName.endsWith(XLS)) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(XLSX)) {
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }


    /**
     * 检查文件
     *
     * @param formFile
     * @throws IOException
     */
    public static void checkFile(MultipartFile formFile) throws IOException {
        //判断文件是否存在
//			logger.error("文件不存在！");
        if (null == formFile) {
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        //	String fileName = formFile.getName();
        String fileName = formFile.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith(XLS) && !fileName.endsWith(XLSX)) {
//			logger.error(fileName+"不是excel文件！");
            throw new IOException(fileName + "不是excel文件！");
        }
    }


}
