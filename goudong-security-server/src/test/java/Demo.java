import com.google.common.collect.Lists;
import com.goudong.commons.utils.AssertUtil;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * @Author msi
 * @Date 2021-05-24 15:53
 * @Version 1.0
 */
public class Demo {
    public static void main(String[] args) {
        File file = new File("D:\\文档\\陈飞龙");
        System.out.println("file = " + file);
        ArrayList<String> strings = Lists.newArrayList("1", "2", "3");
        strings.stream().filter(f->f.equals("1")).forEach(p->{
            System.out.println("p = " + p);
        });
    }
}
