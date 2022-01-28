import com.goudong.commons.utils.core.AssertUtil;
import com.goudong.commons.enumerate.user.OtherUserTypeEnum;
import lombok.Data;

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
