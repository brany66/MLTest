package hw;

import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by YWJ on 2017.4.19.
 * Copyright (c) 2017 NJU PASA Lab All rights reserved.
 */
public class test {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        while (cin.hasNext()) {
//            String[] str = cin.nextLine().trim().split("\\s+");
//            System.out.println(str[str.length - 1].length());
            /*String str = cin.nextLine();
            int count = 0, i = str.length() - 1;
            while (str.charAt(i) == ' ') --i;
            while (str.charAt(i) != ' ') {
                ++count;
                --i;
            }
            System.out.println(count);*/
            char[] chars = cin.nextLine().toCharArray();
            Arrays.sort(chars);
            System.out.println(chars);
        }
    }
}
