package com.goudong.commons.pojo;

import com.goudong.commons.dto.AuthorityMenuDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 将登录用户保存到redis的Hash类型
 * @Author e-Feilong.Chen
 * @Date 2021/8/12 15:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHashRedis implements Serializable {
    private static final long serialVersionUID = -5146906868492351447L;

    /**
     * token字符串
     */
    private String token;

    /**
     * 能访问的菜单集合
     */
    private List<AuthorityMenuDTO> menuDTOList;
}
