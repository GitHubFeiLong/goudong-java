package com.goudong.boot.web.util;

import com.goudong.boot.web.core.BasicException;
import com.goudong.core.lang.Result;
import lombok.Data;

/**
 * 类描述：
 *  统一API响应结果封装
 * @ClassName Result
 * @Author msi
 * @Date 2020/10/5 18:42
 * @Version 1.0
 */
@Data
public class ResultUtil<T> {

    /**
     * 只返回失败信息，不抛额异常
     * @return
     */
    public static Result ofFail(BasicException basicException) {
        return new Result(basicException.getStatus(), basicException.getCode(), basicException.getClientMessage(), basicException.getServerMessage());
    }

}
