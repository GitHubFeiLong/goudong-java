import com.google.common.collect.Lists;
import com.goudong.commons.dto.AuthorityRoleDTO;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.commons.utils.JwtTokenUtil;
import com.goudong.oauth2.enumerate.OtherUserTypeEnum;
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
        String name = OtherUserTypeEnum.QQ.name();
        AssertUtil.isEnum(null, OtherUserTypeEnum.class, "错误的成员");
    }
}
