package cn.edu.jit.tianyu_paas.shared.util;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ResourceBundle;

/**
 * 生成service类和pojo类
 */
public class GeneratorUtil {

    private static final boolean GENERATOR_SERVICE_INTERFACE = false;

    private static String username;
    private static String password;
    private static String url;
    private static String driverClassName;

    public static void main(String[] args) {
        String packageName = "cn.edu.jit.tianyu_paas.shared";
        initDataSource();
        generateByTables(packageName, "market_app_mount");
    }

    private static void initDataSource() {
        //读取application.properties文件，不加.properties后缀，不加路径名
        ResourceBundle bundle = ResourceBundle.getBundle("application");
        username = bundle.getString("spring.datasource.username");
        password = bundle.getString("spring.datasource.password");
        url = bundle.getString("spring.datasource.url");
        driverClassName = bundle.getString("spring.datasource.driverClassName");
    }

    private static void generateByTables(String packageName, String... tableNames) {
        GlobalConfig config = new GlobalConfig();
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(url)
                .setUsername(username)
                .setPassword(password)
                .setDriverName(driverClassName);
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setEntityLombokModel(false)
                .setDbColumnUnderline(true)
                .setNaming(NamingStrategy.underline_to_camel)
                .setSuperMapperClass("com.baomidou.mybatisplus.mapper.BaseMapper")
                .setInclude(tableNames);
        config.setActiveRecord(false)
                .setEnableCache(false)
                .setAuthor("倪龙康")
                .setBaseResultMap(true)
                .setOutputDir("D:/codeGen")
                .setFileOverride(true);
        new AutoGenerator().setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(new PackageConfig()
                        .setParent(packageName)
                        .setController("controller")
                        .setEntity("entity")
                ).execute();
    }

}
