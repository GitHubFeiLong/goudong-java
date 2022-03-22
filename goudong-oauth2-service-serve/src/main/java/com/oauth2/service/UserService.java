package com.oauth2.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2022/3/22 13:12
 */
@Service
public class UserService implements UserDetailsService {

    private List<User> users;

    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        String password = passwordEncoder.encode("123456");
        users = new ArrayList<>();
        users.add(new User("cfl", password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin")));
        users.add(new User("andy", password, AuthorityUtils.commaSeparatedStringToAuthorityList("client")));
        users.add(new User("mark", password, AuthorityUtils.commaSeparatedStringToAuthorityList("client")));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> first = this.users.stream().filter(f -> Objects.equals(f.getUsername(), username))
                .findFirst();
        if (first.isPresent()) {
            return first.get();
        }
        throw new UsernameNotFoundException(username + "不存在");
    }
    //~fields
    //==================================================================================================================

    //~construct methods
    //==================================================================================================================

    //~methods
    //==================================================================================================================
}
