// package com.goudong.oauth2.mapper;
//
//
// import com.baomidou.mybatisplus.core.mapper.BaseMapper;
// import com.goudong.commons.dto.AuthorityUserDTO;
// import com.goudong.commons.po.AuthorityUserPO;
// import org.apache.ibatis.annotations.Mapper;
//
// import java.util.List;
//
// /**
//  * 接口描述：
//  * 用户权限
//  * @ClassName UserDao
//  * @Author msi
//  * @Date 2021-04-01 21:11
//  * @Version 1.0
//  */
// @Mapper
// public interface SelfAuthorityUserMapper extends BaseMapper<AuthorityUserPO> {
//
//     /**
//      * 根据用户名/手机号/邮箱查询正常的用户
//      * @param username
//      * @return
//      */
//     @Deprecated
//     AuthorityUserPO selectUserByUsername(String username);
//     /**
//      * 查询指定用户对应的角色名称
//      * @param userId 用户id
//      * @return
//      */
//     List<String> selectRoleNameByUserId(Long userId);
//
//     /**
//      * 查询用户的详细信息
//      * @param username
//      * @return
//      */
//     AuthorityUserDTO selectUserDetailByUsername(String username);
//
//     /**
//      * 根据请求路径和请求方式查询需要的角色
//      * @param requestUrl
//      * @param requestMethod
//      * @return
//      */
//     List<String> selectRoleNameByMenu(String requestUrl, String requestMethod);
// }
