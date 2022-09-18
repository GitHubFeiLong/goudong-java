package string.regex;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/9/17 19:21
 */
public class RegexTest {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Test
    void test1() {
        String str = "Java now has regular expressions";
        assertFalse(Pattern.matches("^Java", str));
        assertFalse(Pattern.matches("\\Breg.*", str));
        assertFalse(Pattern.matches("n.w\\s+h(a|i)s", str));
        assertFalse(Pattern.matches("s?", str));
        assertFalse(Pattern.matches("s*", str));
        assertFalse(Pattern.matches("s+", str));
        assertFalse(Pattern.matches("s{4}", str));
        assertFalse(Pattern.matches("s{1}", str));
        assertFalse(Pattern.matches("s{0,3}", str));
    }

    @DisplayName("Matcher.find()")
    @Test
    void test2() {
        String str = "Evening is full of the linnet's wings";

        String re = "\\w+";
        Pattern pattern = Pattern.compile(re);

        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            System.out.format("group:%s, start: %d, end :%d \n", matcher.group(), matcher.start(), matcher.end());
        }

        System.out.println("===============");
        int i = 11;
        while (matcher.find(i) && i <= str.length()) {
            System.out.format("i = %d, group:%s, start: %d, end :%d \n", i, matcher.group(), matcher.start(), matcher.end());
            i++;
        }
    }

    @DisplayName("group")
    @Test
    void test3() {
        Matcher matcher = Pattern.compile("([A-Z](\\d)+){1}").matcher("A6");
        if (matcher.matches()) {
            System.out.println(matcher.group()); // A6
            System.out.println(matcher.group(0)); // A6
            System.out.println(matcher.group(1)); // A6
            System.out.println(matcher.group(2)); // 6
        }
    }

    @DisplayName("split")
    @Test
    void test4() {
        Pattern pattern = Pattern.compile("in");

        String str = "Thinking in java";

        // [Th, k, g ,  java]
        System.out.println(Arrays.toString(pattern.split(str)));

        // limit将字符串分割成多少个3，表示切割成3个字符串
        System.out.println(Arrays.toString(Pattern.compile(" ").split(str, 3)));
    }
}
