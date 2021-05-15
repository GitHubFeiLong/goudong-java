import com.google.common.collect.Lists;
import com.goudong.commons.dto.AuthorityRoleDTO;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.utils.JwtTokenUtil;
import lombok.Data;

import java.util.Date;

/**
 * @Author msi
 * @Date 2021-05-10 11:07
 * @Version 1.0
 */
@Data
public class Demo {
    private String name;
    public static void main(String[] args)  {
        AuthorityUserDTO authorityUserDTO1 = new AuthorityUserDTO();
        authorityUserDTO1.setUuid("uuid");
        authorityUserDTO1.setUsername("setUsername");
        authorityUserDTO1.setPassword("setPassword");
        authorityUserDTO1.setEmail("setEmail");
        authorityUserDTO1.setPhone("setPhone");
        authorityUserDTO1.setNickname("setNickname");
        authorityUserDTO1.setRemark("setRemark");
        authorityUserDTO1.setValidTime(new Date());
        authorityUserDTO1.setIsDelete(false);
        authorityUserDTO1.setUpdateTime(new Date());
        authorityUserDTO1.setCreateTime(new Date());
        authorityUserDTO1.setQqOpenId("setQqOpenId");
        authorityUserDTO1.setAccountRadio("setAccountRadio");
        authorityUserDTO1.setAuthorityRoleDTOS(Lists.newArrayList(new AuthorityRoleDTO()));


        String s = JwtTokenUtil.generateToken(authorityUserDTO1, 2);
        System.out.println("s = " + s);

        AuthorityUserDTO authorityUserDTO = JwtTokenUtil.resolveToken(s);
        System.out.println("authorityUserDTO = " + authorityUserDTO.toString());
    }
}
