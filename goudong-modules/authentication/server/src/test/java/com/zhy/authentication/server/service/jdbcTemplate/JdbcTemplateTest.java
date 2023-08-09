// package com.zhy.authentication.server.service.jdbcTemplate;
//
// import cn.hutool.core.date.DateUtil;
// import com.zhy.authentication.server.config.security.AuthenticationProviderImpl;
// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.jdbc.core.JdbcTemplate;
//
// import javax.annotation.Resource;
// import java.util.ArrayList;
// import java.util.List;
//
// /**
//  * 类描述：
//  *
//  * @ClassName JdbcTemplateTest
//  * @Author Administrator
//  * @Date 2023/7/30 8:50
//  * @Version 1.0
//  */
// @SpringBootTest
// public class JdbcTemplateTest {
//     //~fields
//     //==================================================================================================================
//     @Resource
//     private JdbcTemplate jdbcTemplate;
//
//     //~methods
//     //==================================================================================================================
//     @Test
//     public void a () {
//         String sql = "SELECT\n" +
//                 "\tbu.id,\n" +
//                 "\tbu.app_id,\n" +
//                 "\tbu.username,\n" +
//                 "\tbu.password,\n" +
//                 "\tbu.enabled,\n" +
//                 "\tbu.locked,\n" +
//                 "\tbu.valid_time,\n" +
//                 "\tbr.name as roleName\n" +
//                 "FROM\n" +
//                 "\tbase_user bu\n" +
//                 "\tinner join base_user_role bur on bu.id = bur.user_id\n" +
//                 "\tinner join base_role br on bur.role_id = br.id\n" +
//                 "\tinner join base_app ba on ba.id = bu.app_id\n" +
//                 "\twhere bu.id=1\n" +
//                 "\t";
//
//         List<AuthenticationProviderImpl.UserRole> urs = new ArrayList<>();
//         List<AuthenticationProviderImpl.UserRole> query = jdbcTemplate.query(sql, new Object[]{}, (rs, rowNum) -> {
//             AuthenticationProviderImpl.UserRole userRole = new AuthenticationProviderImpl.UserRole();
//             userRole.setId(rs.getLong("id"));
//             userRole.setAppId(rs.getLong("app_id"));
//             userRole.setUsername(rs.getString("username"));
//             userRole.setPassword(rs.getString("password"));
//             userRole.setEnabled(rs.getBoolean("enabled"));
//             userRole.setLocked(rs.getBoolean("locked"));
//             userRole.setValidTime( DateUtil.parse(rs.getString("valid_time")));
//             userRole.setRoleName(rs.getString("roleName"));
//             urs.add(userRole);
//             return userRole;
//         });
//         System.out.println("query = " + query);
//     }
// }
