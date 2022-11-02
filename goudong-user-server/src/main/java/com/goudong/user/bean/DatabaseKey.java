package com.goudong.user.bean;

import com.goudong.boot.web.bean.DatabaseKeyInterface;
import com.goudong.user.enumerate.DatabaseKeyEnum;
import org.springframework.stereotype.Component;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/11/2 16:22
 */
@Component
public class DatabaseKey implements DatabaseKeyInterface {
    /**
     * 根据数据库中的{@code key}获取对应的提示信息
     *
     * @param key 数据库索引名
     * @return
     */
    @Override
    public String getClientMessage(String key) {
        return DatabaseKeyEnum.getClientMessage(key);
    }
}
