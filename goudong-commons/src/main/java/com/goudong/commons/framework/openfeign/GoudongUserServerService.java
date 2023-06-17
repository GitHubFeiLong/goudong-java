// package com.goudong.commons.framework.openfeign;
//
// import com.goudong.commons.dto.oauth2.BaseUserDTO;
// import com.goudong.commons.dto.user.BaseUser2QueryPageReq;
// import com.goudong.core.lang.PageResult;
// import com.goudong.core.lang.Result;
// import org.springframework.cloud.openfeign.FeignClient;
// import org.springframework.cloud.openfeign.SpringQueryMap;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ResponseBody;
//
// /**
//  * User接口
//  * @Author msi
//  */
// @FeignClient(name="goudong-user-server", path = "/api/user")
// @ResponseBody
// public interface GoudongUserServerService {
//
//
//     /**
//      * 分页查询
//      * @param page
//      * @return
//      */
//     @GetMapping("/base-user/page")
//     Result<PageResult<BaseUserDTO>> pageUser (@SpringQueryMap BaseUser2QueryPageReq page);
// }
