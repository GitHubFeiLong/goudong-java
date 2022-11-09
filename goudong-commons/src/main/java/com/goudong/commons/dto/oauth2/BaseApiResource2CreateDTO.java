package com.goudong.commons.dto.oauth2;

import com.goudong.boot.web.validation.EnumIgnoreCaseValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 类描述：
 * 保存api接口资源(BaseApiResource)的参数
 * @author cfl
 * @date 2022/8/2 22:50
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseApiResource2CreateDTO implements Serializable {
    /**
     * 路径Pattern
     */
    @NotBlank(message="参数pattern不能为空")
    private String pattern;
    /**
     * 请求方式,例如：GET，get都可以
     * @see HttpMethod
     */
    @EnumIgnoreCaseValidator(enumClass = HttpMethod.class)
    private String method;
    /**
     * 接口所在应用
     */
    @NotBlank
    private String applicationName;
    /**
     * 备注
     */
    private String remark;

}

