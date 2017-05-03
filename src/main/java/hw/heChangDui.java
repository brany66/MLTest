package hw;

import java.util.Scanner;

/**
 * Created by YWJ on 2017.4.19.
 * Copyright (c) 2017 NJU PASA Lab All rights reserved.
 */
public class heChangDui {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        while (cin.hasNext()) {
            String[] str = cin.nextLine().trim().split("\\s+");
            int[] arr = new int[str.length];
            int i, count = 0, prev = 0;
            for (i = 0; i < str.length; i++)
                arr[i] = Integer.parseInt(str[i]);
            i = 1;
            while (i < str.length) {
                if (arr[i] <= arr[prev]) {
                    ++i;
                    ++count;
                    continue;
                } else
                    ++i;
            }
            System.out.println(count);
        }
    }
}
