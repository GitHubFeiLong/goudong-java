package com.goudong.authentication.server.rest.resp;

import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类描述：
 * 菜单
 * @author cfl
 * @version 1.0
 */

@Data
public class BaseMenuGetAllResp {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "菜单集合")
    private List<BaseMenuDTO> records = new ArrayList<>(0);
}
