package com.goudong.user;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/9/15 20:07
 */
public class JunitDemoTest {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @ParameterizedTest
    @ValueSource(strings = {"hello", "world"})
    void testParameter(String str) {

    }

    static Stream initData1() {
        return Stream.of("hello1", "world1", "junit51");
    }
    static Stream initData2() {
        return Stream.of("hello2", "world2", "junit52");
    }
    @ParameterizedTest
    @MethodSource(value = {"initData1", "initData2"})
    void testMethod(){}

    // static Stream data() {
    //     return Stream.of(
    //             new User("张三", 18),
    //             new User("李四", 18));
    // }
    static List data() {
        ArrayList<Object> list = new ArrayList<>();
        list.add(new User("张三", 18));
        list.add(new User("李四", 18));
        return list;
    }
    @ParameterizedTest
    @MethodSource(value = {"data"})
    void testMethod1(User user){
        System.out.println("user = " + user);
    }

    static class User {
        String name;

        int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    @ParameterizedTest
    @CsvSource(value = {"1,中国", "2,美国"})
    void testCsv(int id, String country) {
        System.out.println("id = " + id + ", country = " + country);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/user.csv")
    void testCsvFile(int id, String name) {
        System.out.println("id = " + id + ", name = " + name);
    }

    @ParameterizedTest
    @CsvSource(value = {"0/1", "1/0"})
    void testCsv2(@ConvertWith(value = PointConvert.class) Point point) {
        System.out.println("point = " + point);
    }

    @ParameterizedTest
    @CsvSource({"true, 3.14159265359, JUNE, 2017, 2017-06-21T22:00:00"})
    void testDefaultConverters(
            boolean b, double d, Summer s, Year y, LocalDateTime dt) { }

    enum Summer {
        JUNE, JULY, AUGUST, SEPTEMBER;
    }


}
