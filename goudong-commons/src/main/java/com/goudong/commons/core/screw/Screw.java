package com.goudong.commons.core.screw;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.goudong.commons.constant.SpringProfileConst;
import com.goudong.commons.constant.SystemEnvConst;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 类描述：
 * 数据库文档生成工具
 * @author msi
 * @version 1.0
 * @date 2021/12/11 9:39
 */
public class Screw {

    /**
     * spring的环境
     */
    private final Environment environment;
    /**
     * 忽略表
     */
    private ArrayList<String> ignoreTableNameList = new ArrayList<>();

    /**
     * 忽略表前缀
     */
    private ArrayList<String> ignorePrefixList = new ArrayList<>();

    /**
     * 忽略表后缀
     */
    private ArrayList<String> ignoreSuffixList = new ArrayList<>();

    public Screw(Environment environment){
        this.environment = environment;
        this.create();
    }

    /**
     * 创建文档
     * @return
     */
    public Screw create() {
        String[] activeProfiles = environment.getActiveProfiles();

        // dev 和 test 才生成文档
        long count = Stream.of(activeProfiles).filter(f -> Objects.equals(SpringProfileConst.DEVELOPMENT, f) || Objects.equals(SpringProfileConst.TEST, f))
                .count();

        if (count > 0) {
            //数据源
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));

            hikariConfig.setJdbcUrl(environment.getProperty("spring.datasource.url"));
            hikariConfig.setUsername(environment.getProperty("spring.datasource.username"));
            hikariConfig.setPassword(environment.getProperty("spring.datasource.password"));
            //设置可以获取tables remarks信息
            hikariConfig.addDataSourceProperty("useInformationSchema", "true");
            hikariConfig.setMinimumIdle(2);
            hikariConfig.setMaximumPoolSize(5);
            DataSource dataSource = new HikariDataSource(hikariConfig);

            //生成配置
            EngineConfig engineConfig = EngineConfig.builder()
                    //生成文件路径
                    .fileOutputDir(SystemEnvConst.USER_DIR)
                    //打开目录
                    .openOutputDir(false)
                    //文件类型
                    .fileType(EngineFileType.HTML)
                    //生成模板实现
                    .produceType(EngineTemplateType.freemarker)
                    //自定义文件名称 TODO 截取数据库名称
                    .fileName("自定义文件名称").build();

            ProcessConfig processConfig = ProcessConfig.builder()
                    //指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
                    //根据名称指定表生成
                    .designatedTableName(new ArrayList<>())
                    //根据表前缀生成
                    .designatedTablePrefix(new ArrayList<>())
                    //根据表后缀生成
                    .designatedTableSuffix(new ArrayList<>())
                    //忽略表名
                    .ignoreTableName(ignoreTableNameList)
                    //忽略表前缀
                    .ignoreTablePrefix(ignorePrefixList)
                    //忽略表后缀
                    .ignoreTableSuffix(ignoreSuffixList)
                    .build();
            //配置
            Configuration config = Configuration.builder()
                    //版本
                    .version("1.0.0")
                    //描述
                    .description(environment.getProperty("spring.application.name"))
                    //数据源
                    .dataSource(dataSource)
                    //生成配置
                    .engineConfig(engineConfig)
                    //生成配置
                    .produceConfig(processConfig)
                    .build();
            //执行生成
            new DocumentationExecute(config).execute();
        }

        return this;
    }

    public Screw ignoreTableNames(ArrayList<String> ignoreTableNameList){
        this.ignoreTableNameList = ignoreTableNameList;
        return this;
    }

    public Screw ignorePrefixList(ArrayList<String> ignorePrefixList){
        this.ignorePrefixList = ignorePrefixList;
        return this;
    }

    public Screw ignoreSuffixList(ArrayList<String> ignoreSuffixList){
        this.ignoreSuffixList = ignoreSuffixList;
        return this;
    }
}
