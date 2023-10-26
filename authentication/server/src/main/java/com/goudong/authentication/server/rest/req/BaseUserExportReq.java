package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 类描述：
 * 导出用户
 * @author chenf
 * @version 1.0
 */
@Data
public class BaseUserExportReq {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "勾选的id")
    private List<Long> ids;

    @ApiModelProperty(value = "分页查询参数")
    private BaseUserPageReq pageReq;
    //~methods
    //==================================================================================================================
}
