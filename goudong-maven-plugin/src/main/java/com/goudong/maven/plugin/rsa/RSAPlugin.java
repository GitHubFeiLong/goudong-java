package com.goudong.maven.plugin.rsa;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类描述：
 * 自定义的maven插件rsa，会在项目的resources/.ssh 目录下生成公钥和私钥文件
 * @author msi
 * @version 1.0
 * @date 2022/2/5 9:52
 */
// @Mojo(name = "rsa")
public class RSAPlugin extends AbstractMojo {

    private static final Logger log = LoggerFactory.getLogger(RSAPlugin.class);
    //~fields
    //==================================================================================================================
    @Parameter(property = "rsa.commons_name", defaultValue = "goudong-commons")
    private String commonsModulePackageName;
    //~methods
    //==================================================================================================================

    /**
     * 在commons模块resources/.ssh下创建密钥文件，将公钥和私钥保存到文件中。
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        // 1. 判断resources目录下是否存在文件(resources/.ssh/id_rsa 和 resources/.ssh/id_rsa.pub)
        // try {
        //     RSAUtil.generateKeyPair2File();
        // } catch (Exception e) {
        //     log.error("自定义插件rsa，执行失败:{}", e.getMessage());
        // }
    }

}