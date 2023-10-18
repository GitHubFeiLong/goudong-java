package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * 类描述：
 * 导入角色
 * @author chenf
 * @version 1.0
 */
@Data
public class BaseRoleImportReq {
    //~fields
    //==================================================================================================================
    @NotNull
    @ApiModelProperty("文件")
    private MultipartFile file;
    //~methods
    //==================================================================================================================
}
