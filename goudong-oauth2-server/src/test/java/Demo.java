import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.commons.utils.BeanUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author msi
 * @Date 2021-05-10 11:07
 * @Version 1.0
 */
@Data
public class Demo {
    private String name;
    public static void main(String[] args)  {
        List<Test> list1 = new ArrayList<>();
        list1.add(new Test("t1"));
        list1.add(new Test("t2"));
        list1.add(new Test("t3"));
        list1.add(new Test("t4"));

        List<Demo> demos = new ArrayList<>();

        for (Test test : list1) {
            Demo d = new Demo();
            BeanUtils.copyProperties(test, d);
            demos.add(d);
        }
        System.out.println(demos.toString());
    }
}
