import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.AssertUtil;

/**
 * @Author msi
 * @Date 2021-05-10 11:07
 * @Version 1.0
 */
public class Demo {
    public static void main(String[] args) {
//        AssertUtil.hasText("", "error");
//        AssertUtil.hasText(null, "error");
//        AssertUtil.hasLength("", "error");
//        AssertUtil.hasLength(null, "error");
//        String regex  = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
//        String str = "168-1@qq.com";
//        System.out.println(str.matches(regex));;
        Result<Object> objectResult = Result.ofSuccess();
        System.out.println(1);
    }
}
