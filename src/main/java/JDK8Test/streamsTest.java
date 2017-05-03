package JDK8Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by YWJ on 2017.4.18.
 * Copyright (c) 2017 NJU PASA Lab All rights reserved.
 * stream操作可以是中间操作，也可以是完结操作。
 * 完结操作会返回一个某种类型的值，
 * 而中间操作会返回流对象本身，并且你可以通过多次调用同一个流操作方法来将操作结果串起来
 */
public class streamsTest {
    public static void fun() {
        List<String> st = new ArrayList<>();
        st.add("ddd2");
        st.add("aaa2");
        st.add("bbb1");
        st.add("aaa1");
        st.add("bbb3");
        st.add("ccc");
        st.add("bbb2");
        st.add("ddd1");

        /**
         * Filter接受一个predicate接口类型的变量，并将所有流对象中的元素进行过滤。
         * 该操作是一个中间操作，因此它允许我们在返回结果的基础上再进行其他的流操作（forEach）。
         *
         * ForEach接受一个function接口类型的变量，用来执行对每一个元素的操作。
         * ForEach是一个中止操作。它不返回流，所以我们不能再调用其他的流操作。
         */
        st.stream()
                .filter((s) -> s.startsWith("a"))
                .forEach(System.out::println);

        /**
         * Sorted是一个中间操作，能够返回一个排过序的流对象的视图。
         * 流对象中的元素会默认按照自然顺序进行排序，除非你自己指定一个Comparator接口来改变排序规则。
         * sorted只是创建一个流对象排序的视图，而不会改变原来集合中元素的顺序。
         */
        System.out.println();

        st.stream()
                .sorted()
                .filter((s) -> s.startsWith("a"))
                .forEach(System.out::println);

        System.out.println();
        /**
         * map
         * match : 匹配操作有多种不同的类型，都是用来判断某一种规则是否与流对象相互吻合的。
         */
        st.stream()
                .map(String::toUpperCase)
                .sorted((a, b) -> b.compareTo(a))
                .forEach(System.out::println);

        System.out.println(st.stream().anyMatch((s) -> s.startsWith("a")));
        System.out.println(st.stream().allMatch((s) -> s.startsWith("a")));
        System.out.println(st.stream().noneMatch((s) -> s.startsWith("z")));


        /**
         * count: 是一个终结操作，它的作用是返回一个数值，用来标识当前流对象中包含的元素数量。
         */
        System.out.println();
        System.out.println(st.stream().filter((s) -> s.startsWith("b")).count());

        System.out.println();
        Optional<String> reduced = st.stream()
                .sorted()
                .reduce((a, b) -> a + "#" + b);
        reduced.ifPresent(System.out::println);
    }
}
