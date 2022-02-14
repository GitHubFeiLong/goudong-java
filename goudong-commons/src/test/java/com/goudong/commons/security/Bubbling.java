package com.goudong.commons.security;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 类描述：
 * 冒泡排序
 * @author msi
 * @version 1.0
 * @date 2022/2/14 21:59
 */
public class Bubbling {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("开始输入需要冒牌排序的数据以逗号隔开：");
        String data = scanner.next();
        String[] strings = data.split(",");
        int[] nums = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            nums[i] = Integer.parseInt(strings[i]);
        }

        System.out.println("您初始的数据是：" + Arrays.toString(nums) + "\n开始进行排序");
        System.out.println("排序中....");
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = 0; j < nums.length - 1 -i; j++) {
                if (nums[j] > nums[j+1]) {
                    int t = nums[j];
                    nums[j] = nums[j+1];
                    nums[j+1] = t;
                }
            }

        }

        System.out.println("排序结束，排序结果是：" + Arrays.toString(nums));

    }

}