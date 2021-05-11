package com.goudong.oauth2.service.impl;

import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.entity.AuthorityUserDO;
import com.goudong.commons.po.AuthorityUserPO;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.oauth2.dao.AuthorityUserDao;
import com.goudong.oauth2.service.AuthorityUserService;
import org.springframework.beans.BeanUtils;
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
        List<AuthorityUserPO> authorityUserPOS = authorityUserDao.select(authorityUserPO);

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
        List<AuthorityUserPO> authorityUserPOS = authorityUserDao.select(authorityUserPO);

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
     * @param authorityUserDO
     * @return
     */
    @Override
    public AuthorityUserDO createUser(AuthorityUserDO authorityUserDO) {
        authorityUserDao.insert(authorityUserDO);
        return authorityUserDO;
    }
}
