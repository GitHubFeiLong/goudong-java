package com.goudong.oauth2.service.impl;

import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.entity.AuthorityUserDO;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.BasicException;
import com.goudong.commons.po.AuthorityUserPO;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.oauth2.dao.AuthorityUserDao;
import com.goudong.oauth2.service.AuthorityUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 类描述：
 * 用户服务层
 * @Author msi
 * @Date 2021-05-02 13:53
 * @Version 1.0
 */
@Slf4j
@Service
public class AuthorityUserServiceImpl implements AuthorityUserService {

    @Resource
    private AuthorityUserDao authorityUserDao;


    /**
     * 根据 authorityUserPO对象，查询 authority_user表
     *
     * @param authorityUserDTO 用户对象
     * @return
     */
    @Override
    public List<AuthorityUserDTO> listByAuthorityUserDTO(AuthorityUserDTO authorityUserDTO) {
        AuthorityUserPO authorityUserPO = new AuthorityUserPO();
        // 转换对象
        BeanUtils.copyProperties(authorityUserDTO, authorityUserPO);
        // 查询
        List<AuthorityUserPO> authorityUserPOS = authorityUserDao.selectByAnd(authorityUserPO);

        return BeanUtil.copyList(authorityUserPOS, AuthorityUserDTO.class);
    }

    /**
     * 根据指定的用户名，生成3个可以未被注册的用户名
     * 当返回结果为空集合时，表示账号可以使用
     * @param username
     * @return
     */
    @Override
    public List<String> generateUserName(String username) {
        AssertUtil.hasText(username, "根据用户名查询用户时，用户名不能为空");
        List<String> result = new ArrayList<>();
        // 查询用户名是否存在
        AuthorityUserPO authorityUserPO = new AuthorityUserPO();
        authorityUserPO.setUsername(username);
        List<AuthorityUserPO> authorityUserPOS = authorityUserDao.selectByAnd(authorityUserPO);

        if (authorityUserPOS.isEmpty()) {
            return result;
        }

        List<String> names = authorityUserDao.selectUserNameByLikeUsername(username);

        Random random = new Random();
        do {
            int i = random.nextInt(10000);
            String item = username + i;
            // 本次生成的用户名不存在
            if(!names.contains(item)){
                names.add(item);
                result.add(item);
            }
        } while (result.size() < 3);

        return result;
    }


    /**
     * 新增用户
     *
     * @param authorityUserDTO
     * @return
     */
    @Override
    public AuthorityUserDTO createUser(AuthorityUserDTO authorityUserDTO) {

        AuthorityUserPO userPO = (AuthorityUserPO) BeanUtil.copyProperties(authorityUserDTO, AuthorityUserPO.class);

        List<AuthorityUserPO> userPOList = authorityUserDao.selectByOr(userPO);

        if (userPOList.size() > 1) {
            // 有多条，表示提交的数据有问题
            // 1. 使用postman 类似工具，提交未经校验的内容
            // 2. 注册时间过长，账号被别人注册了
            BasicException.exception(ClientExceptionEnum.BAD_REQUEST);
        }

        // 数据库没有相关用户或只有一条数据
        // 加密
        userPO.setPassword(BCrypt.hashpw(userPO.getPassword(), BCrypt.gensalt()));
        authorityUserDao.updateInsert(userPO);

        // 只有一条，根据用户选中的单选 accountRadio 进行用户信息管理（空字符串、MY_SELF、NOT_MY_SELF）
        switch (authorityUserDTO.getAccountRadio()) {
            case "":
                // 没有出现弹框
                log.info("账户未选择单选框：{}", authorityUserDTO.toString());
                break;
            case "MY_SELF":
                // 选中账号是我的，清除一些旧数据

                break;
            case "NOT_MY_SELF":
                // 选中账号不是我的

                break;
            default:
                BasicException.exception(ClientExceptionEnum.BAD_REQUEST);
        }

        return (AuthorityUserDTO) BeanUtil.copyProperties(userPO, AuthorityUserDTO.class);
    }
}
