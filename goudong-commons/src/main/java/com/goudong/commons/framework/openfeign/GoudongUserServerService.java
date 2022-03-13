package com.goudong.commons.framework.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * User接口
 * @Author msi
 */
@FeignClient(name="goudong-user-server", path = "/api/user")
@ResponseBody
public interface GoudongUserServerService {





    //==================
}
