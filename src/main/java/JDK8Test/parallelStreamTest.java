package JDK8Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by YWJ on 2017.4.18.
 * Copyright (c) 2017 NJU PASA Lab All rights reserved.
 */
public class parallelStreamTest {
    public static void fun() {

        int max = 1000000;
        List<String> values = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            UUID uuid = UUID.randomUUID();
            values.add(uuid.toString());
        }
        /** 顺序排序 */
        long t0 = System.nanoTime();
        System.out.println(values.stream().sorted().count());
        double mill_Seconds = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t0) / 1000.0 ;
        System.out.println(String.format("sequential sort took : %.2f s", mill_Seconds));

        /** 并行排序 */
        long t1 = System.nanoTime();
        System.out.println(values.parallelStream().sorted().count());
        double mill_Seconds1 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t1) / 1000.0;
        System.out.println(String.format("parallel sort took : %.2f s, \tparallel sort speedup is : %.2f", mill_Seconds1, mill_Seconds / mill_Seconds1));


        System.out.println();
        Map<Integer, String> map = new HashMap<>();
        // putIfAbsent避免我们将null写入
        for (int i = 0; i < 10; i++)
            map.putIfAbsent(i, "val " + i);
        map.forEach((id, val) -> System.out.println(val));

        map.computeIfPresent(3, (num, val) -> val + num);
        System.out.println(map.get(3));
        map.computeIfPresent(9, (num, val) -> null);
        System.out.println(map.containsKey(9));
        map.computeIfAbsent(23, num -> "val" + num);
        System.out.println(map.containsKey(23));

        map.computeIfAbsent(3, num -> "bam");
        System.out.println(map.get(3));

        map.remove(3, "val 3");
        map.remove(3, "val 33");
        System.out.println(map.get(3));
        System.out.println(map.getOrDefault(42, "not found"));

        map.merge(9, "val 9", (a, b) -> a.concat(b));
        System.out.println(map.get(9));
        map.merge(9, "contact", (a, b) -> a.concat(b));
        System.out.println(map.get(9));
    }
}
