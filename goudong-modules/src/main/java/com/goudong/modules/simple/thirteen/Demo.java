package com.goudong.modules.simple.thirteen;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类描述：
 *
 * @author chenf
 */
public class Demo {
    //~fields
    //==================================================================================================================
    public static final String POEM =
            "Twas brillig. and the slithy toves\n" +
            "Did gyre and gimble in the wabe.\n" +
            "All mimsy were the borogoves.\n" +
            "And the mome raths outgrahe.\n\n"+
            "Beware the jabberwock, my son.\n"+
            "The  jaws that bite, the claws that catch.\n" +
            "Beware the Jubjub bird, and shun\n" +
            "The frumious Bandersnatch.";
    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        Matcher m = Pattern.compile("(?m)(\\S+)\\s+((\\S+)\\s+(\\S+))$").matcher(POEM);
        while (m.find()){
            for(int j = 0; j <= m.groupCount(); j++)
                System.out.print("[" + m.group(j) + "]");
            System.out.println();
        }
    }
}
