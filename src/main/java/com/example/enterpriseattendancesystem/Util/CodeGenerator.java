package com.example.enterpriseattendancesystem.Util;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;

public class CodeGenerator {
    public static void main(String[] args) {
        generator("谢妍雨", "enterpriseAttendanceSystem", "com.example.enterpriseattendancesystem", "", "leave");
        //generator("xghao", "", "com.teamxu.learnmybatis", "m2", "role", FileType.ENTITY, FileType.MAPPER);
    }

    /**
     * Mybatis一键生成entity,mapper,mapper.xml,service,serviceImpl,controller
     *
     * @param author            开发人员
     * @param projectName       项目名
     * @param parentPackageName 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     * @param moduleName        模块名
     * @param tableName         表名
     * @param fileType          文件类型
     *                          -如果参数为空，生成全部文件。如果文件已存在，如果是entity，生成覆盖，否则不生成;
     *                          -如果参数不为空，生成传入的文件类型，生成该文件，如果存在，生成覆盖。
     */
    public static void generator(String author,
                                 String projectName,
                                 String parentPackageName,
                                 String moduleName,
                                 String tableName,
                                 FileType... fileType) {
        AutoGenerator mpg = new AutoGenerator();
        mpg.setGlobalConfig(globalConfig(author, projectName));
        mpg.setDataSource(dataSourceConfig(
                "jdbc:mysql://127.0.0.1:3306/enterpriseattendancesystem?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8"
                , "com.mysql.cj.jdbc.Driver"
                , "root"
                , "1233210"));
        mpg.setTemplate(templateConfig());
        mpg.setStrategy(strategyConfig(tableName));
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.setPackageInfo(packageConfig(parentPackageName, moduleName, fileType));
        mpg.setCfg(injectionConfig(fileType));
        mpg.execute();
    }

    /**
     * 全局配置
     *
     * @param author      开发人员
     * @param projectName 项目名
     * @return GlobalConfig
     */
    private static GlobalConfig globalConfig(String author, String projectName) {
        String projectPath = System.getProperty("user.dir");
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(projectPath + "/src/main/java");
        globalConfig.setFileOverride(true);
        globalConfig.setAuthor(author);
        globalConfig.setSwagger2(false);
        globalConfig.setOpen(false);
        globalConfig.setEnableCache(false);
        globalConfig.setKotlin(false);
        globalConfig.setActiveRecord(false);
        globalConfig.setBaseResultMap(false);
        globalConfig.setBaseColumnList(false);
        globalConfig.setEntityName("");
        globalConfig.setMapperName("");
        globalConfig.setXmlName("");
        globalConfig.setServiceName("");
        globalConfig.setServiceImplName("");
        globalConfig.setControllerName("");
        return globalConfig;
    }

    /**
     * 数据源设置
     *
     * @param url        驱动连接的URL
     * @param driverName 驱动名称
     * @param username   数据库连接用户名
     * @param password   数据库连接密码
     * @return DataSourceConfig
     */
    private static DataSourceConfig dataSourceConfig(String url,
                                                     String driverName,
                                                     String username,
                                                     String password) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setUrl(url);
        dataSourceConfig.setDriverName(driverName);
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(password);
        return dataSourceConfig;
    }

    /**
     * 包配置
     *
     * @param parentName   父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     * @param moduleName   模块名称
     * @param fileTypeEnum 文件类型
     * @return PackageConfig
     */
    private static PackageConfig packageConfig(String parentName, String moduleName, FileType... fileTypeEnum) {
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(parentName);
        packageConfig.setModuleName(moduleName);
        if (fileTypeEnum.length == 0) {
            return packageConfig;
        }
        packageConfig.setEntity("");
        packageConfig.setMapper("");
        packageConfig.setXml("");
        packageConfig.setService("");
        packageConfig.setServiceImpl("");
        packageConfig.setController("");
        for (int i = 0; i < fileTypeEnum.length; i++) {
            if (fileTypeEnum[i] == FileType.ENTITY)
                packageConfig.setEntity("entity");
            else if (fileTypeEnum[i] == FileType.MAPPER)
                packageConfig.setMapper("mapper");
            else if (fileTypeEnum[i] == FileType.XML)
                packageConfig.setXml("mapper.xml");
            else if (fileTypeEnum[i] == FileType.SERVICE)
                packageConfig.setService("service");
            else if (fileTypeEnum[i] == FileType.SERVICE_IMPL)
                packageConfig.setServiceImpl("service.impl");
            else if (fileTypeEnum[i] == FileType.CONTROLLER)
                packageConfig.setController("controller");
        }
        return packageConfig;
    }


    /**
     * 模板路径配置项
     *
     * @return TemplateConfig
     */
    private static TemplateConfig templateConfig() {
        TemplateConfig templateConfig = new TemplateConfig();
        // 设置为null以禁用生成controller, service, mapper等
//        templateConfig.setController(null);
//        templateConfig.setService(null);
//        templateConfig.setServiceImpl(null);
//        templateConfig.setMapper(null);
//        templateConfig.setXml(null);
        // 设置实体类模板路径
        templateConfig.setEntity("templates/entity.java");
        templateConfig.setMapper("templates/mapper.java");
        templateConfig.setService("templates/service.java");
        templateConfig.setController("templates/controller.java");

        return templateConfig;
    }

    /**
     * 策略配置
     *
     * @param tableName 数据库表名称，多个用英文逗号隔开
     * @return StrategyConfig
     */
    private static StrategyConfig strategyConfig(String tableName) {
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setEntityLombokModel(true);
        strategyConfig.setRestControllerStyle(true);
        strategyConfig.setSuperEntityColumns("com.example.enterpriseattendancesystem.entity");
        strategyConfig.setInclude(tableName);
        strategyConfig.setControllerMappingHyphenStyle(true);
        return strategyConfig;
    }

    /**
     * 自定义配置
     *
     * @param fileTypeEnum 文件类型
     * @return InjectionConfig
     */
    private static InjectionConfig injectionConfig(FileType... fileTypeEnum) {
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        injectionConfig.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                if (fileTypeEnum.length == 0) {
                    //无参情况下，先检查.java file是否存在：
                    //如果不存在，创建；如果存在，判断是否是entity.java：如果是，创建（覆盖）；否则，不创建。
                    checkDir(filePath);
                    File file = new File(filePath);
                    boolean exist = file.exists();
                    if (exist) {
                        if (FileType.ENTITY == fileType) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                    return true;
                } else {
                    //有参情况下，只创建传入的.java，无论存在都直接覆盖。
                    boolean isType = false;
                    for (int i = 0; i < fileTypeEnum.length; i++) {
                        if (fileTypeEnum[i] == fileType) {
                            isType = true;
                            break;
                        }
                    }
                    if (!isType) {
                        return false;
                    }
                    checkDir(filePath);
                    return true;
                }
            }
        });
        return injectionConfig;
    }
}