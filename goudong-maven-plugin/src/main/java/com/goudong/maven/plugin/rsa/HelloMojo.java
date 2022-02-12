package com.goudong.maven.plugin.rsa;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/2/12 16:28
 */
@Mojo(name = "hello")
public class HelloMojo extends AbstractMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("hello world");
    }
}