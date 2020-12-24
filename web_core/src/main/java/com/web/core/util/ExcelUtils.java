package com.web.core.util;

import com.web.core.exception.ValidationException;
import lombok.Data;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.Serializable;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.apache.poi.ss.usermodel.WorkbookFactory.create;

/**
 * Excel导入工具
 * @author
 * @version 1.0
 */
public class ExcelUtils {

    private final static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    private final static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 直接将excel表格封装成实体对象列表
     * @param in 文档数据流
     * @param clazz 映射实体类型
     * @return List<T> 实体列表
     * @throws Exception
     * @author
     *
     */
    public static <T> List<T> readExcel(InputStream in, Class<T> clazz) throws Exception {
        return readExcel(in, clazz, null);
    }

    /**
     * 直接将excel表格封装成实体对象列表
     * @param in 文档数据流
     * @param clazz 映射实体类型
     * @param entityHander 实体对象附加处理器
     * @return List<T> 实体列表
     * @throws Exception
     * @author
     *
     */
    public static <T> List<T> readExcel(InputStream in, Class<T> clazz, EntityHandler<T> entityHander) throws Exception {
        //封装实体对象列表
        List<T> entityList = null;
        //产品出现次数统计
        final List<AbstractImportEntityCounter<T>> importEntityCounters = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        long loadFinishTime = 0;
        try {
            Workbook workbook = null;
            boolean enabledTraceLogger = logger.isTraceEnabled();
            if (!in.markSupported()) {
                in = new PushbackInputStream(in, 8);
            }
            if (POIFSFileSystem.hasPOIFSHeader(in)) {
                workbook = new HSSFWorkbook(in);
            } else if (POIXMLDocument.hasOOXMLHeader(in)) {
                workbook = new XSSFWorkbook(OPCPackage.open(in));
            } else {
                throw new ValidationException("不支持的表格文档类型！");
            }
            logger.trace("表格数量 => {}", workbook.getNumberOfSheets());
            logger.debug("\n➧载入文件：使用 " + (System.currentTimeMillis() - startTime) + " 毫秒");
            loadFinishTime = System.currentTimeMillis();

            //实体类属性元信息识别并收集
            ExcelTable excelTable = clazz.getAnnotation(ExcelTable.class);
            Map<String, PropertyContext> properties = propertyContextAware(clazz);
            Assert.notEmpty(properties, "请指定表格列与属性映射列表");

            StringBuffer rows = new StringBuffer("\n");
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                Sheet sheet = workbook.getSheetAt(sheetNum);
                String sheetName = sheet.getSheetName();
                int totalRowNum = sheet.getPhysicalNumberOfRows();
                //表头所在行号
                int headerAt = sheet.getFirstRowNum();
                //表头占用行数
                int headerRows = 1;
                int lastRowNum = sheet.getLastRowNum();
                if (totalRowNum == 0) {//跳过空表格
                    continue;
                }
                //------------------------------表格基本属性------------------------------
                if (excelTable != null) {
                    //表头所在行号
                    if (excelTable.headerAt() > 0) {
                        headerAt = excelTable.headerAt();
                    }
                    //表头占用行数
                    headerRows = excelTable.headerRows();
                }
                //------------------------------表头字段索引------------------------------
                Row headerRow = sheet.getRow(headerAt);//表头一行
                if (headerRow == null) {
                    continue;
                }
                int firstCellNum = headerRow.getFirstCellNum();//第一列列号
                int lastCellNum = headerRow.getLastCellNum();//最后一列列号
                String[] propertieNames = new String[lastCellNum - firstCellNum];
                String[] columnNames = new String[lastCellNum - firstCellNum];
                if (enabledTraceLogger) {
                    rows.append("【表头索引】\t\t");
                }
                for (int cellnum = firstCellNum, i = 0; cellnum <= lastCellNum; cellnum++, i++) {
                    Cell cell = headerRow.getCell(cellnum);
                    if (cell == null) {
                        continue;
                    }
                    //读取单元格内容
                    Object value = getCellValue(cell);
                    if ((value instanceof CharSequence) && StringUtils.isBlank(value.toString())) {
                        value = null;
                    }
                    if (enabledTraceLogger) {
                        rows.append(value != null ? value : "--\t").append(" \t ");
                    }
                    if (value == null) {
                        continue;
                    }
                    String columnName = value.toString();
                    if (properties.containsKey(columnName)) {
                        columnNames[i] = columnName;
                        propertieNames[i] = properties.get(columnName).getName();
                    }
                }
                if (enabledTraceLogger) {
                    rows.append("\n");
                }
                logger.info("\n➧【第{}张表格：{}】 总行数={}, 首行行号={}, 最后行号={}\n➧{}\n➧{}",
                        sheetNum,
                        sheetName,
                        totalRowNum,
                        headerAt,
                        lastRowNum,
                        Arrays.toString(columnNames),
                        Arrays.toString(propertieNames)
                );

                LocalAssert.intGreaterEqual(headerRows, 0, "标题总行数，应该是非负的整数");
                //表格数据遍历和封装（一般开始于：headerAt + headerRows）
                for (int rownum = headerAt + headerRows; rownum <= lastRowNum; rownum++) {
                    Row row = sheet.getRow(rownum);
                    if (row == null) {
                        continue;
                    }
                    //对象封装
                    T entity = clazz.newInstance();
                    boolean isBlankRow = true;
                    StringBuffer rowString = enabledTraceLogger ? new StringBuffer() : null;
                    for (int cellnum = firstCellNum, i = 0; cellnum < firstCellNum + propertieNames.length; cellnum++, i++) {
                        Cell cell = row.getCell(cellnum);
                        if (StringUtils.isNotEmpty(propertieNames[i])) {
                            //读取单元格的值
                            Object cellValue = getCellValue(cell);
                            //封装对象的属性
                            PropertyContext pc = properties.get(columnNames[i]);
                            Field field = pc.getField();
                            if("gwPrice1".equals(propertieNames[i])){
                                System.out.println();
                            }
                            Object value = ObjectUtils.defaultIfNull(cellValue, StringUtils.trimToNull(pc.getDefaultValue()));
                            //如果单元格是字符串，看是否进行全/半角转换
                            if (value instanceof CharSequence) {
                                String strValue = value.toString();
                                switch (pc.getCharWidth()) {
                                    case FULL: value = LocalStringUtils.toFullWidth(strValue);break;
                                    case HALF: value = LocalStringUtils.toHalfWidth(strValue);break;
                                    default:;
                                }
                            }
                            if (value != null) {
                                logger.trace("【设定对象属性】列名={}, value={}, fieldType={}", propertieNames[i], value, field.getType());
                                //如果单元格有值，设定对象属性
                                field.set(entity, ConvertUtils.convert(value, field.getType()));
                                //方式2：Method setter = pc.getSetter(); setter.invoke(entity, ConvertUtils.convert(value, setter.getParameterTypes()[0]));
                                //方式3：LocalBeanUtils.setProperty(entity, propertieNames[i], value);
                                //方式4：BeanUtils.setProperty(entity, propertieNames[i], value);
                            }
                            isBlankRow = ((propertieNames[i] == null || cellValue == null) && isBlankRow);
                            if (enabledTraceLogger) {
                                rowString.append(cellValue != null ? cellValue : "--\t").append(" \t ");
                            }
                        }
                    }
                    //当前行：数据封装完成
                    if (!isBlankRow) {
                        //基本数据格式校验
                        RowAware rowAware = validateEntity(sheetName, rownum, entity);
                        //实体处理扩展器
                        if (entityHander != null) {
                            if(StringUtils.isNotBlank(rowAware.getFatal())) {
                                //上报每行严重错误
                                entityHander.reportEntityFatal(sheetName, rownum, rowAware.getFatal());
                            }
                            if(StringUtils.isNotBlank(rowAware.getError())) {
                                //上报每行一般错误
                                entityHander.reportEntityError(sheetName, rownum, rowAware.getError());
                            }
                            //解析封装成实体对象以后进一步的处理与加工
                            entityHander.doAfterRowAnalysed(sheetName, rownum, entity);
                            //统计数据重复出现次数
                            AbstractImportEntityCounter<T> importEntityCounter = entityHander.getImportEntityCounter();
                            if (importEntityCounter != null) {
                                AbstractImportEntityCounter couter = (AbstractImportEntityCounter) CollectionUtils.find(importEntityCounters, object -> {
                                    return ((AbstractImportEntityCounter) object).equalsTo(entity);
                                });
                                if (couter != null) {
                                    couter.setTimes(couter.getTimes() + 1).addPosition(sheetName + "-第" + (rownum + 1) + "行");
                                } else {
                                    couter = importEntityCounter.instanceOf(entity).setKeyNames(entity);
                                    couter.setTimes(1).addPosition(sheetName + "-第" + (rownum + 1) + "行");
                                    importEntityCounters.add(couter);
                                }
                            }
                        }
                        if (entity != null) {
                            (entityList = entityList == null ? new ArrayList<T>() : entityList).add(entity);
                        }
                        if (enabledTraceLogger) {
                            rows.append("[第" + rownum + "行: " + firstCellNum + "-" + lastCellNum + "列] - ").append(rowString).append("\n");
                        }
                    }
                }
            }
            LocalAssert.notEmpty(entityList, "表格至少1行信息（列表头部除外）");
            if (enabledTraceLogger) {
                logger.trace(rows.deleteCharAt(rows.length() - 1).toString());
            }

            //数据出现次数统计器
            if (entityHander != null) {
                //重复行检查上报
                entityHander.findRepeatRowsAndReport(importEntityCounters);
            }
        } finally {
            IOUtils.closeQuietly(in);
        }
        logger.info("\n➧解析封装：使用 " + (System.currentTimeMillis() - loadFinishTime) + " 毫秒");
        logger.info("\n➧本次导入：使用 " + (System.currentTimeMillis() - startTime) + " 毫秒");
        return entityList;
    }

    /**
     * 直接将excel表格封装成实体对象列表
     * @param in 文档数据流
     * @param clazz 映射实体类型
     * @param sheetRow 表格序号
     * @param EncryptionKey 密码
     * @return List<T> 实体列表
     * @throws Exception
     * @author
     *
     */
    public static <T> List<T> readExcel(InputStream in, Class<T> clazz,Integer sheetRow , String EncryptionKey) throws Exception {
        //封装实体对象列表
        List<T> entityList = null;
        //产品出现次数统计
        final List<AbstractImportEntityCounter<T>> importEntityCounters = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        long loadFinishTime = 0;
        try {
            Workbook workbook = null;
            boolean enabledTraceLogger = logger.isTraceEnabled();
            if (!in.markSupported()) {
                in = new PushbackInputStream(in, 8);
            }
            if (StringUtils.isBlank(EncryptionKey)){
                if (POIFSFileSystem.hasPOIFSHeader(in)) {
                    workbook = new HSSFWorkbook(in);
                } else if (POIXMLDocument.hasOOXMLHeader(in)) {
                    workbook = new XSSFWorkbook(OPCPackage.open(in));
                } else {
                    throw new ValidationException("不支持的表格文档类型！");
                }
            }else{
                workbook = create(in, EncryptionKey);//设置密码打开
            }

            logger.trace("表格数量 => {}", workbook.getNumberOfSheets());
            logger.debug("\n➧载入文件：使用 " + (System.currentTimeMillis() - startTime) + " 毫秒");
            loadFinishTime = System.currentTimeMillis();

            //实体类属性元信息识别并收集
            ExcelTable excelTable = clazz.getAnnotation(ExcelTable.class);
            Map<String, PropertyContext> properties = propertyContextAware(clazz);
            Assert.notEmpty(properties, "请指定表格列与属性映射列表");

            StringBuffer rows = new StringBuffer("\n");
            int sNum = 0;
            //如果设置行
            if (sheetRow == null){
                sheetRow = workbook.getNumberOfSheets();
            }else{
                sNum = sheetRow-1;
            }

            for (int sheetNum = sNum; sheetNum < sheetRow; sheetNum++) {
                Sheet sheet = workbook.getSheetAt(sheetNum);
                String sheetName = sheet.getSheetName();
                int totalRowNum = sheet.getPhysicalNumberOfRows();
                //表头所在行号
                int headerAt = sheet.getFirstRowNum();
                //表头占用行数
                int headerRows = 1;
                int lastRowNum = sheet.getLastRowNum();
                if (totalRowNum == 0) {//跳过空表格
                    continue;
                }
                //------------------------------表格基本属性------------------------------
                if (excelTable != null) {
                    //表头所在行号
                    if (excelTable.headerAt() > 0) {
                        headerAt = excelTable.headerAt();
                    }
                    //表头占用行数
                    headerRows = excelTable.headerRows();
                }
                //------------------------------表头字段索引------------------------------
                Row headerRow = sheet.getRow(headerAt);//表头一行
                if (headerRow == null) {
                    continue;
                }
                int firstCellNum = headerRow.getFirstCellNum();//第一列列号
                int lastCellNum = headerRow.getLastCellNum();//最后一列列号
                String[] propertieNames = new String[lastCellNum - firstCellNum];
                String[] columnNames = new String[lastCellNum - firstCellNum];
                if (enabledTraceLogger) {
                    rows.append("【表头索引】\t\t");
                }
                for (int cellnum = firstCellNum, i = 0; cellnum <= lastCellNum; cellnum++, i++) {
                    Cell cell = headerRow.getCell(cellnum);
                    if (cell == null) {
                        continue;
                    }
                    //读取单元格内容
                    Object value = getCellValue(cell);
                    if ((value instanceof CharSequence) && StringUtils.isBlank(value.toString())) {
                        value = null;
                    }
                    if (enabledTraceLogger) {
                        rows.append(value != null ? value : "--\t").append(" \t ");
                    }
                    if (value == null) {
                        continue;
                    }
                    String columnName = value.toString();
                    if (properties.containsKey(columnName)) {
                        columnNames[i] = columnName;
                        propertieNames[i] = properties.get(columnName).getName();
                    }
                }
                if (enabledTraceLogger) {
                    rows.append("\n");
                }
                logger.info("\n➧【第{}张表格：{}】 总行数={}, 首行行号={}, 最后行号={}\n➧{}\n➧{}",
                        sheetNum,
                        sheetName,
                        totalRowNum,
                        headerAt,
                        lastRowNum,
                        Arrays.toString(columnNames),
                        Arrays.toString(propertieNames)
                );

                LocalAssert.intGreaterEqual(headerRows, 0, "标题总行数，应该是非负的整数");
                //表格数据遍历和封装（一般开始于：headerAt + headerRows）
                for (int rownum = headerAt + headerRows; rownum <= lastRowNum; rownum++) {
                    Row row = sheet.getRow(rownum);
                    if (row == null) {
                        continue;
                    }
                    //对象封装
                    T entity = clazz.newInstance();
                    boolean isBlankRow = true;
                    StringBuffer rowString = enabledTraceLogger ? new StringBuffer() : null;
                    for (int cellnum = firstCellNum, i = 0; cellnum < firstCellNum + propertieNames.length; cellnum++, i++) {
                        Cell cell = row.getCell(cellnum);
                        if (StringUtils.isNotEmpty(propertieNames[i])) {
                            //读取单元格的值
                            Object cellValue = getCellValue(cell);
                            //封装对象的属性
                            PropertyContext pc = properties.get(columnNames[i]);
                            Field field = pc.getField();
                            if("gwPrice1".equals(propertieNames[i])){
                                System.out.println();
                            }
                            Object value = ObjectUtils.defaultIfNull(cellValue, StringUtils.trimToNull(pc.getDefaultValue()));
                            //如果单元格是字符串，看是否进行全/半角转换
                            if (value instanceof CharSequence) {
                                String strValue = value.toString();
                                switch (pc.getCharWidth()) {
                                    case FULL: value = LocalStringUtils.toFullWidth(strValue);break;
                                    case HALF: value = LocalStringUtils.toHalfWidth(strValue);break;
                                    default:;
                                }
                            }
                            if (value != null) {
                                logger.trace("【设定对象属性】列名={}, value={}, fieldType={}", propertieNames[i], value, field.getType());
                                //如果单元格有值，设定对象属性
                                field.set(entity, ConvertUtils.convert(value, field.getType()));
                                //方式2：Method setter = pc.getSetter(); setter.invoke(entity, ConvertUtils.convert(value, setter.getParameterTypes()[0]));
                                //方式3：LocalBeanUtils.setProperty(entity, propertieNames[i], value);
                                //方式4：BeanUtils.setProperty(entity, propertieNames[i], value);
                            }
                            isBlankRow = ((propertieNames[i] == null || cellValue == null) && isBlankRow);
                            if (enabledTraceLogger) {
                                rowString.append(cellValue != null ? cellValue : "--\t").append(" \t ");
                            }
                        }
                    }
                    //当前行：数据封装完成
                    if (!isBlankRow) {
                        //基本数据格式校验
                        RowAware rowAware = validateEntity(sheetName, rownum, entity);
                        if (entity != null) {
                            (entityList = entityList == null ? new ArrayList<T>() : entityList).add(entity);
                        }
                        if (enabledTraceLogger) {
                            rows.append("[第" + rownum + "行: " + firstCellNum + "-" + lastCellNum + "列] - ").append(rowString).append("\n");
                        }
                    }
                }
            }
            LocalAssert.notEmpty(entityList, "表格至少1行信息（列表头部除外）");
            if (enabledTraceLogger) {
                logger.trace(rows.deleteCharAt(rows.length() - 1).toString());
            }
        } finally {
            IOUtils.closeQuietly(in);
        }
        logger.info("\n➧解析封装：使用 " + (System.currentTimeMillis() - loadFinishTime) + " 毫秒");
        logger.info("\n➧本次导入：使用 " + (System.currentTimeMillis() - startTime) + " 毫秒");
        return entityList;
    }

    /**
     * 实体处理器
     * @author
     * @version 1.0
     */
    public abstract static class EntityHandler<T> {

        /**
         * 数据出现次数统计器
         */
        protected AbstractImportEntityCounter<T> importEntityCounter;

        /**
         * 上报每行一般错误
         * @param sheetName 表格名称
         * @param rownum 表格行号
         * @param entityError 错误信息
         * @author
         */
        public void reportEntityError(String sheetName, int rownum, String entityError) {}

        /**
         * 严重错误的行检查上报
         * @param sheetName 表格名称
         * @param rownum 表格行号
         * @param entityFatal 错误信息
         * @author
         */
        public void reportEntityFatal(String sheetName, int rownum, String entityFatal) {}

        /**
         * 一行数据解析封装成实体对象以后进一步的处理与加工
         * @param sheetName 表格名称
         * @param rownum 表格行号
         * @param entity 实体对象
         * @author
         *
         */
        public abstract void doAfterRowAnalysed(String sheetName, int rownum, T entity);

        /**
         * 指定：数据出现次数统计器
         * @param counter
         * @author
         *          */
        public EntityHandler<T> supportImportEntityCounter(AbstractImportEntityCounter<T> counter) {
            this.importEntityCounter = counter;
            return this;
        }

        /**
         * 获取：数据出现次数统计器
         * @author
         *          */
        public AbstractImportEntityCounter<T> getImportEntityCounter() {
            return this.importEntityCounter;
        }

        /**
         * 重复行上报
         * @param repeatRows
         * @author
         *          */
        public abstract void reportRepeatRows(String repeatRows);

        /**
         * 重复数量的行检查上报
         * @param importEntityCounters
         * @param <T>
         * @return String
         */
        public <T> void findRepeatRowsAndReport(List<AbstractImportEntityCounter<T>> importEntityCounters) {
            if (this.importEntityCounter != null) {
                //导入检查: Excel表格行数据不能重复，过滤出重复出现的行
                final List<AbstractImportEntityCounter> repeats = (List<AbstractImportEntityCounter>) CollectionUtils.select(importEntityCounters, object -> {
                    return ((AbstractImportEntityCounter) object).getTimes() > 1;
                });
                if (CollectionUtils.isNotEmpty(repeats)) {
                    StringBuilder repeatRows = new StringBuilder();
                    String[] colors = {"limegreen", "blue", "#ff890d"};
                    for (AbstractImportEntityCounter iec : repeats) {
                        repeatRows.append("<b>【重复数据检查】</b> - ");
                        String[] keyNames = iec.getKeyNames();
                        int length = keyNames == null ? 0 : keyNames.length;
                        for (int i = 0; keyNames != null && i < length; i++) {
                            String keyName = keyNames[i];
                            repeatRows.append("<font color='").append(colors[i % 3]).append("'>").append(keyName).append("</font>").append(i < length - 1 ? "｜" : "");
                        }
                        repeatRows.append("：在[");
                        for (Iterator<String> ite = iec.getPositions().iterator(); ite.hasNext(); ) {
                            String position = ite.next();
                            repeatRows.append("<font style='text-decoration: underline; color:#1e7dd2;'>")
                                      .append(position)
                                      .append("</font>")
                                      .append(ite.hasNext() ? "，" : "");
                        }
                        repeatRows.append("] 重复出现 <b>").append("<font color='red'>").append(iec.getTimes()).append("</font>").append("</b> 次<br/>");
                    }
                    //重复行上报
                    this.reportRepeatRows(repeatRows.toString());
                }
            }
        }

    }

    /**
     * 实体类属性元信息识别并收集
     * @param clazz 实体类型
     * @param <T>
     * @return Map
     * @throws Exception
     */
    private static <T> Map<String, PropertyContext> propertyContextAware(Class<T> clazz) throws Exception {
        Map<String, PropertyContext> properties = new LinkedHashMap<>();
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            Field[] fields = superClass.getDeclaredFields();
            for (Field field : fields) {
                ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                if (excelProperty != null) {
                    PropertyContext pc = new PropertyContext();
                    String columnName = excelProperty.value().trim();
                    field.setAccessible(true);
                    pc.setColumnName(columnName);
                    pc.setName(field.getName());
                    pc.setField(field);
                    pc.setCharWidth(excelProperty.charWidth());
                    pc.setDefaultValue(excelProperty.defaultValue());
                    //pc.setSetter(LocalBeanUtils.getSetterMethodByField(clazz, field.getName(), true));
                    //pc.setGetter(LocalBeanUtils.getGetterMethodByField(clazz, field.getName(), true));
                    properties.put(columnName /*+ "[" + f.getName() + "]"*/, pc);
                }
            }
        }
        return properties;
    }

    /**
     * 基本数据格式校验
     * <ul>JSR提供的校验注解：
     * <li> @Null 被注释的元素必须为 null</li>
     * <li> @NotNull 被注释的元素必须不为 null</li>
     * <li> @AssertTrue 被注释的元素必须为 true</li>
     * <li> @AssertFalse 被注释的元素必须为 false</li>
     * <li> @Min(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值</li>
     * <li> @Max(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值</li>
     * <li> @DecimalMin(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值</li>
     * <li> @DecimalMax(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值</li>
     * <li> @Size(max=, min=)   被注释的元素的大小必须在指定的范围内</li>
     * <li> @Digits (integer, fraction)     被注释的元素必须是一个数字，其值必须在可接受的范围内</li>
     * <li> @Past 被注释的元素必须是一个过去的日期</li>
     * <li> @Future 被注释的元素必须是一个将来的日期</li>
     * <li> @Pattern(regex=,flag=) 被注释的元素必须符合指定的正则表达式</li>
     * </ul>
     * <ul>Hibernate Validator提供的校验注解：
     * <li>@NotBlank(message =)   验证字符串非null，且trim后长度必须大于0</li>
     * <li>@Email 被注释的元素必须是电子邮箱地址</li>
     * <li>@Length(min=,max=) 被注释的字符串的大小必须在指定的范围内</li>
     * <li>@NotEmpty 被注释的字符串的必须非空</li>
     * <li>@Range(min=,max=,message=) 被注释的元素必须在合适的范围内</li>
     * </ul>
     * @param <T>
     * @param entity 实体对象
     * @return String
     */
    private static <T> RowAware validateEntity(String sheetName, int rownum, T entity) {
        RowAware rowAware = (entity instanceof RowAware) ? (RowAware) entity : new RowAware();

        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (CollectionUtils.isNotEmpty(violations)) {//数据格式检查：不通过
            StringBuilder rowError = new StringBuilder();
            StringBuilder rowFatal = new StringBuilder();
            for (ConstraintViolation violation : violations) {
                Annotation annotation = violation.getConstraintDescriptor().getAnnotation();
                if(annotation instanceof Length) {
                    if (rowFatal.length()==0) {
                        rowFatal.append("<font color='green'>[").append(sheetName).append("]</font>-<font color='#00AEAE'>[第").append(rownum + 1).append("行]</font>");
                    }
                    rowFatal.append(violation.getMessage());
                } else {
                    if(rowError.length()==0){
                        rowError.append("[").append(sheetName).append("]-[第").append(rownum + 1).append("行]");
                    }
                    rowError.append(violation.getMessage());
                }
            }
            String entityError = LocalStringUtils.trimToNull(rowError);
            String entityFatal = LocalStringUtils.trimToNull(rowFatal);
            rowAware.setSheetName(sheetName);
            rowAware.setRownum(rownum);
            rowAware.setValidPassed(entityError==null && entityFatal==null);
            rowAware.setError(entityError);
            rowAware.setFatal(entityFatal);
        } else {//数据格式检查：通过
            rowAware.setSheetName(sheetName);
            rowAware.setRownum(rownum);
            rowAware.setValidPassed(true);
        }
        return rowAware;
    }

    /**
     * 读取行数据单元格的值
     * @param cell 单元格对象
     * @return Object  单元格值
     * @author
     *
     */
    public static Object getCellValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            //判断数据的类型
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_STRING: //字符串
                    cellValue = StringUtils.trimToNull(cell.getStringCellValue());
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC: //数字
                    cellValue = cell.getNumericCellValue();
                    if (DateUtil.isCellDateFormatted(cell)) {//如果是日期
                        cellValue = cell.getDateCellValue();
                    } else {
                        HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
                        String formatValue = dataFormatter.formatCellValue(cell);
                        if (NumberUtils.isDigits(formatValue)) {
                            cellValue = new BigInteger(formatValue);
                        } else if (NumberUtils.isNumber(formatValue)) {
                            cellValue = new BigDecimal(formatValue);
                        }
                    }
                    break;
                case HSSFCell.CELL_TYPE_FORMULA: //公式
                    cellValue = cell.getCellFormula();
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN: //布尔值
                    cellValue = cell.getBooleanCellValue();
                    break;
                case HSSFCell.CELL_TYPE_BLANK: //空白单元格
                    cellValue = null;
                    break;
                case Cell.CELL_TYPE_ERROR:
                    cellValue = cell.getErrorCellValue();
                    break;
                default:
                    cellValue = null;
                    break;
            }
        }
        return cellValue;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public @interface ExcelTable {

        /**
         * 表头所在行号
         */
        int headerAt() default 0;

        /**
         * 表头占用行数（默认1行）
         */
        int headerRows() default 1;

    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public @interface ExcelProperty {

        /** 属性名称 */
        String value() default "";

        /** 全/半角标识 */
        Width charWidth() default Width.MIXED;

        /** 默认值 */
        String defaultValue() default "";

    }

    /**
     * 字符全/半角标识
     */
    public enum Width {

        /** 混合 */
        MIXED,

        /** 半角 */
        HALF,

        /** 全角 */
        FULL

    }

    /**
     * 属性上下文
     * @author
     * @version 1.0
     *
     * @since JDK 1.8
     */
    @Data
    public static class PropertyContext {

        /**
         * 属性名
         */
        private String name;

        /**
         * 列名
         */
        private String columnName;

        /**
         * 属性Field
         */
        private Field field;

        /**
         * 全半角转向设置
         */
        private Width charWidth;

        /**
         * 默认值
         */
        private String defaultValue;

    }

    /**
     * Excel解析行错误可自动注入属性
     * @author
     * @version 1.0
     *
     * @since JDK 1.8
     */
    @Data
    public static class RowAware {

        /**
         * 表格名称
         */
        private String sheetName;

        /**
         * 行号
         */
        private Integer rownum;

        /**
         * 行验证是否通过（true是、false否）
         */
        private Boolean validPassed;

        /**
         * 一般错误
         */
        private String  error;

        /**
         * 严重错误
         */
        private String fatal;

    }

    /**
     * 导入数据出现次数统计信息
     * @author
     * @version 1.0
     *
     * @since JDK 1.8
     */
    public abstract static class AbstractImportEntityCounter<T> implements Serializable {

        /**
         * 唯一标识
         */
        private String[] keyNames;

        /**
         * 出现次数
         */
        private int times = 0;

        /**
         * 出现位置
         */
        private List<String> positions = new ArrayList<>();

        /**
         * ImportEntityCounter实例构造方法
         * @param entity
         * @return AbstractImportEntityCounter
         */
        public abstract AbstractImportEntityCounter instanceOf(T entity);

        /**
         * 判断excel行实体与当前ImportEntityCounter是否表示同一个对象
         * @param entity
         * @return boolean
         */
        public abstract boolean equalsTo(T entity);

        /**
         * 添加重复行坐标位置
         * @param position
         * @return AbstractImportEntityCounter
         * @author
         *          */
        public AbstractImportEntityCounter addPosition(String position) {
            positions.add(position);
            return this;
        }

        /**
         * 获取：唯一标识
         * @return String[]
         */
        public String[] getKeyNames() {
            return keyNames;
        }

        /**
         * 设置：唯一标识
         */
        protected void setKeyNames(String[] keyNames) {
            this.keyNames = keyNames;
        }

        /**
         * 设置：唯一标识
         * @param entity
         * @author
         *          */
        public abstract AbstractImportEntityCounter setKeyNames(T entity);

        public int getTimes() {
            return times;
        }

        /**
         * 设置：出现次数
         * @param times
         * @return AbstractImportEntityCounter
         */
        public AbstractImportEntityCounter setTimes(int times) {
            this.times = times;
            return this;
        }

        /**
         * 获取：出现位置
         * @return List
         */
        public List<String> getPositions() {
            return positions;
        }

    }


    public static Workbook create(InputStream inp, String password) throws IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException, EncryptedDocumentException {
        if (!(inp).markSupported()) {
            inp = new PushbackInputStream(inp, 8);
        }

        byte[] header8 = org.apache.poi.util.IOUtils.peekFirst8Bytes(inp);
        if (NPOIFSFileSystem.hasPOIFSHeader(header8)) {
            NPOIFSFileSystem fs = new NPOIFSFileSystem(inp);
            Workbook workbook;
            workbook = create(fs, password);
            return workbook;
        } else if (DocumentFactoryHelper.hasOOXMLHeader(inp)) {
            return new XSSFWorkbook(OPCPackage.open(inp));
        } else {
            throw new org.apache.poi.openxml4j.exceptions.InvalidFormatException("提示！请核对模板是否为加密文档");
        }
    }

    private static Workbook create(NPOIFSFileSystem fs, String password) throws IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        DirectoryNode root = fs.getRoot();
        if (root.hasEntry("EncryptedPackage")) {
            InputStream stream = DocumentFactoryHelper.getDecryptedStream(fs, password);
            OPCPackage pkg = OPCPackage.open(stream);
            return (Workbook) create(pkg);
        } else {
            if (password != null) {
                Biff8EncryptionKey.setCurrentUserPassword(password);
            }

            HSSFWorkbook var3;
            try {
                var3 = new HSSFWorkbook(root, true);
            } finally {
                Biff8EncryptionKey.setCurrentUserPassword(null);
            }

            return var3;
        }
    }

    private static Object create(OPCPackage pkg) throws IOException {
        return new XSSFWorkbook(pkg);
    }

}

