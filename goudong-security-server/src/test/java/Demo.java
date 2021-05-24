import com.goudong.commons.utils.AssertUtil;
import org.springframework.util.StringUtils;

/**
 * @Author msi
 * @Date 2021-05-24 15:53
 * @Version 1.0
 */
public class Demo {
    public static void main(String[] args) {
//        String keyTemplate = "1";
//        AssertUtil.isTrue(StringUtils.hasText(keyTemplate), "redis key template 字符串不能为空");
        String[] strs = null;
        AssertUtil.notEmpty(strs,  "error");
    }
}
