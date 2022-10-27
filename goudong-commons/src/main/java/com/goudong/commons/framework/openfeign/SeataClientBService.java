package com.goudong.commons.framework.openfeign;

import com.goudong.core.lang.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 类描述：
 * seata测试客户端b，用于seata的功能测试
 * @author cfl
 * @date 2022/8/3 18:34
 * @version 1.0
 */
@FeignClient(name="seata-client-b", path = "/api/seata")
@ResponseBody
public interface SeataClientBService {

    /**
     * 扣库
     * @param product 商品
     * @return
     */
    @DeleteMapping("/warehouse/del-warehouse")
    Result<Object> save(@RequestParam("product") String product);
}
