package com.goudong.junit5.demo.service;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/9/21 12:56
 */
public class ComparatorTest {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Test
    void testCompare() {
        List<Integer> list = Lists.newArrayList(1, 10, 5, 6, 2, 8, 3, 9);

        List<Integer> collect = list.stream()
                .sorted(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1 - o2;
                    }
                }).collect(Collectors.toList());

        // List<Integer> collect = list.stream()
        //         .sorted((o1, o2) -> o1-o2).collect(Collectors.toList());

        // List<Integer> collect = list.stream()
        //         .sorted().collect(Collectors.toList());

        System.out.println("collect = " + collect);
    }

    @Test
    void testArraysSort() {
        int[] ints = new int[]{1, 3 ,5, 2, 9, 5, 6, 8, 3};
        Arrays.sort(ints);
        System.out.println("Arrays.toString(ints) = " + Arrays.toString(ints));
    }
    @Test
    void testIntegerCompare() {
        Integer i1 = 1, i2 = 2, i3 = 3;
        // 1 比 2 小 返回 -1
        assertEquals(i1.compareTo(i2), -1);
        // 2 与 2 相等 返回 0
        assertEquals(i2.compareTo(i2), 0);
        // 3 比 2 大 返回 1
        assertEquals(i3.compareTo(i2), 1);

        ArrayList<Integer> integers = Lists.newArrayList(i3, i1, i2);
        // 默认是添加到集合顺序存储
        System.out.println("integers = " + integers); // integers = [3, 1, 2]

        // 排序，因为Integer实现了Comparable接口，并重写了compareTo方法，因此可以正确使用
        Collections.sort(integers);

        // 默认排序是升序
        System.out.println("integers = " + integers); // integers = [1, 2, 3]

        // 将集合进行倒序（跟比较器无关）
        Collections.reverse(integers);
        System.out.println("integers = " + integers); // integers = [3, 2, 1]

        // // 放弃Integer的排序规则，使用自定义比较器，这里我使用降序
        // Collections.sort(integers, (o1, o2) -> o2 - o1);
        // System.out.println("integers = " + integers); // integers = [3, 2, 1]
        //
        // // 使用集合中对象实现Comparable重写compareTo的比较器
        // integers.stream().sorted().forEach(System.out::println); // 1, 2, 3
        // integers.stream().sorted((o1, o2)-> o2 -o1).forEach(System.out::println); // 3, 2, 1
    }

    @Test
    void test1() throws ParseException {

        List users = Lists.newArrayList(
                new User("张三", 12, 350, DateUtils.parseDate("2010-06-20", "yyyy-MM-dd")),
                new User("李四", 15, 200, DateUtils.parseDate("2007-11-20", "yyyy-MM-dd")),
                new User("王麻子", 13, 300, DateUtils.parseDate("2009-08-03", "yyyy-MM-dd"))
        );
        // users = [name='张三', name='李四', name='王麻子']
        System.out.println("users = " + users);

        // 使用默认排序规则（年龄升序）
        Collections.sort(users);
        // users = [name='张三', name='王麻子', name='李四']
        System.out.println("users = " + users);

        // o1的钱比o2的钱少，返回负数，结果是升序
        Collections.sort(users, (o1, o2)->((User)o1).getMoney() - ((User)o2).getMoney());
        // users = [name='李四', name='王麻子', name='张三']
        System.out.println("users = " + users);

        // 根据生日排序，这里使用了Date的默认排序规则（时间升序），所以o1.compareTo(o2)最后是升序
        Collections.sort(users, (o1, o2)->((User)o1).getBirthday().compareTo(((User)o2).getBirthday()));
        // users = [name='李四', name='王麻子', name='张三']
        System.out.println("users = " + users);

        // 根据生日排序，这里使用了Date的默认排序规则（时间升序），所以o2.compareTo(o1)最后是降序
        Collections.sort(users, (o1, o2)->((User)o2).getBirthday().compareTo(((User)o1).getBirthday()));
        // users = [name='张三', name='王麻子', name='李四']
        System.out.println("users = " + users);

        // java8
        // 乱序
        Collections.shuffle(users);
        // users = [name='李四', name='王麻子', name='张三']
        System.out.println("users = " + users);
        // 使用默认排序， name='张三 'name='王麻子 'name='李四'
        users.stream().sorted().forEach(System.out::print);
        // 使用自定义排序，o2的钱比o1的钱多时，返回正数，及降序 name='张三'name='王麻子'name='李四'
        users.stream().sorted(((o1, o2) -> ((User)o2).getMoney() - ((User)o1).getMoney())).forEach(System.out::print);

    }

    /**
     * 类描述：
     * 自定义用户对象，实现了Comparable
     * @author cfl
     * @date 2022/9/21 22:46
     * @version 1.0
     */
    @Getter
    @Setter
    @AllArgsConstructor
    static class User implements Comparable{
        String name;

        Integer age;

        Integer money;

        Date birthday;

        /**
         * 重写compareTo方法，根据用户年龄进行升序排序
         * @param o the object to be compared.
         * @return -1 升序； 1 降序
         */
        @Override
        public int compareTo(Object o) {
            return this.age < ((User)o).age ? -1 : (this.age == ((User)o).age ? 0 : 1);
        }

        @Override
        public String toString() {
            return "name='" + name + "'";
        }
    }
}