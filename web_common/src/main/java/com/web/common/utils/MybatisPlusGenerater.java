package com.web.common.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.web.core.service.BaseService;
import com.web.core.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * MyBatis-Plus代码生成器
 * @author
 * @version 1.0
 */
public class MybatisPlusGenerater {
    /**
     * 数据源配置
     */
    private static DataSourceConfig dsc = new DataSourceConfig().setUrl("jdbc:mysql://localhost:3306/web_base?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true&useSSL=false")
                                                                .setDriverName("com.mysql.cj.jdbc.Driver")
                                                                .setUsername("root")
                                                                .setPassword("123456")
                                                                .setDbType(DbType.MYSQL)
                                                                .setTypeConvert(new MySqlTypeConvert());

//
//    private static DataSourceConfig dsc = new DataSourceConfig().setUrl("jdbc:oracle:thin:@42.157.130.158:1521/orcl.168.0.57")
//            .setDriverName("oracle.jdbc.driver.OracleDriver")
//            .setUsername("mrqs")
//            .setPassword("mrqs")
//            .setDbType(DbType.ORACLE)
//            .setTypeConvert(new MySqlTypeConvert());

    /**
     * 指定表
     */
    private static String[] tableNames = {
            "ts_dept_info","ts_menu","ts_menu_platform","ts_org_info","ts_platform","ts_static_info"
    };
    /**
     * 作者
     */
    private static String author = "sunhua";
    /**
     * 项目位置
     */
    private static String projectPath = "D:/web_protal/web_system/web_system_biz";
    /**
     * 生成类文件存放位置
     */
    private static String classOutPath = projectPath + "/src/main/java";
    /**
     * 模块结构（模块路径）
     */
    private static String modulePath = "com.web.system.biz.userInfo";
    /**
     * 生成xxxMapper.xml文件存放位置
     */
    private static String mapperXmlPath = projectPath + "/src/main/resources/META-INF/mapping/";

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(classOutPath);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        gc.setFileOverride(true);
        gc.setAuthor(author);
        gc.setOpen(false);
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(modulePath);
        pc.setEntity("entity");
        pc.setMapper("dao");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setController("web");
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude(tableNames);
        strategy.setTablePrefix("ts_", "tb_", "sn_", "tc_", "td_", "tm_", "tl_" , "td_vc_", "rp_");
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setSuperServiceClass(BaseService.class.getName());
        strategy.setSuperServiceImplClass(BaseServiceImpl.class.getName());
        //strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(false);
        //strategy.setEntityColumnConstant(true);
        strategy.entityTableFieldAnnotationEnable(true);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return mapperXmlPath + "/" + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // TODO 自定义初始化配置参数
            }
        };
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        //生成代码
        mpg.execute();
    }

}