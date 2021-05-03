import com.goudong.oauth2.enumerate.OtherUserTypeEnum;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-05-03 15:56
 * @Version 1.0
 */
public class Demo {
    public static void main(String[] args) throws UnsupportedEncodingException {
//        OtherUserTypeEnum hh = OtherUserTypeEnum.valueOf("QQ");

        String str = "http://localhost:8080/bindPage.html?current=1620034132013&openId=25F4E89692541EB4F6BAFCDB747F03AB&nickname=┍ ⃢\uD83D\uDC41︵\uD83D\uDC41⃢　&headPortrait30=http://qzapp.qlogo.cn/qzapp/101932233/25F4E89692541EB4F6BAFCDB747F03AB/30&userType=QQ";
        System.out.println("str = " + str);
        System.out.println("str = " + URLEncoder.encode("┍ ⃢\uD83D\uDC41︵\uD83D\uDC41⃢　","UTF-8"));
    }
}
