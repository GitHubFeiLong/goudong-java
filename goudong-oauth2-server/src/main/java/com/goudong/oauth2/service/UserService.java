package com.goudong.oauth2.service;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/8/29 19:33
 */

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.goudong.commons.po.AuthorityUserPO;
import com.goudong.commons.pojo.SelfUserDetails;
import com.goudong.oauth2.entity.User;
import com.goudong.oauth2.mapper.SelfAuthorityUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by macro on 2019/9/30.
 */
@Service
public class UserService implements UserDetailsService {
    private List<User> userList;

    @Resource
    private SelfAuthorityUserMapper selfAuthorityUserMapper;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    // @PostConstruct
    // public void initData() {
    //     String password = passwordEncoder.encode("123456");
    //     userList = new ArrayList<>();
    //     userList.add(new User(0L,"macro", password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin")));
    //     userList.add(new User(0L,"andy", password, AuthorityUtils.commaSeparatedStringToAuthorityList("client")));
    //     userList.add(new User(0L,"mark", password, AuthorityUtils.commaSeparatedStringToAuthorityList("client")));
    // }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // List<User> findUserList = userList.stream().filter(user -> user.getUsername().equals(username)).collect(Collectors.toList());
        // if (!CollectionUtils.isEmpty(findUserList)) {
        //     return findUserList.get(0);
        // } else {
        //     throw new UsernameNotFoundException("用户名或密码错误");
        // }



        SelfUserDetails userInfo = new SelfUserDetails();
        // 查询用户信息
        LambdaQueryWrapper<AuthorityUserPO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.nested(i->i.eq(AuthorityUserPO::getUsername, username)
                .or()
                .eq(AuthorityUserPO::getEmail, username)
                .or()
                .eq(AuthorityUserPO::getPhone, username)
        ).nested(i->i.isNull(AuthorityUserPO::getValidTime).or().lt(AuthorityUserPO::getValidTime, LocalDateTime.now()));


        AuthorityUserPO user = selfAuthorityUserMapper.selectOne(lambdaQueryWrapper);

        if (user != null) {
            userInfo.setUsername(user.getUsername());
            userInfo.setPassword(user.getPassword());
        } else {
            throw new BadCredentialsException("账户名与密码不匹配，请重新输入");
        }

        Set<SimpleGrantedAuthority> authoritiesSet = new HashSet<>();
        // 查询用户权限
        List<String> roles = selfAuthorityUserMapper.selectRoleNameByUserId(user.getId());
        for (String roleName : roles) {
            //用户拥有的角色
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);
            authoritiesSet.add(simpleGrantedAuthority);
        }
        // 设置用户的角色
        userInfo.setAuthorities(authoritiesSet);
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userInfo;
    }
}

