package JDK8Test;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

/**
 * Created by YWJ on 2017.4.18.
 * Copyright (c) 2017 NJU PASA Lab All rights reserved.
 */
public class testBuildInInterface {
    public static void fun() {
        /** 内置函数接口
         * Predicate boolean , 处理复杂的裸机动词,只有一个参数
         * */

        //Predicates : Guava
        System.out.println("build-in interface Predicate: ");
        Predicate<String> pred = (s) -> s.length() > 0;
        System.out.println(pred.test("foo") + "\t" + pred.negate().test("foo"));
        Predicate<Boolean> nonNull = Objects::nonNull;
        Predicate<Boolean> isNull = Objects::isNull;

        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> isNotEmpty = isEmpty.negate();

        /** 内置函数接口
         * Functions: 也只有一个参数，串接多个函数
         * */
        IntPredicate isNumber = a -> a > 10 && a < 20;
        System.out.println("build-in interface Function: ");
        Function<String, Integer> toInteger = Integer::valueOf;
        System.out.println(toInteger.apply("123") + "\t" + isNumber.test(toInteger.apply("123")));
        Function<String, String> backToString = toInteger.andThen(String::valueOf);
        System.out.println(backToString.apply("123"));

        /** 内置函数接口
         * Suppliers: 产生一个给定类型的结果，无参数
         * */
        System.out.println("build-in interface Suppliers: ");
        Supplier<Person> personSupplier = Person::new;
        System.out.println(personSupplier.get().toString());

        /** 内置函数接口
         * Consumers:在一个输入参数上的操作
         * */
        System.out.println("build-in interface Consumers: ");
        Consumer<Person> greeter = (per) -> System.out.println("Hello " + per.firstName);
        greeter.accept(new Person("Luke", "Skywalker"));

        /** 内置函数接口
         * Comparator
         * */
        System.out.println("build-in interface Comparator: ");
        Comparator<Person> comparator = (pa, pb) -> pa.firstName.compareTo(pb.firstName);
        Person pa = new Person("John", "Doe");
        Person pb = new Person("Alice", "Wonderland");
        System.out.println(comparator.compare(pa, pb) + "\t"
               + comparator.reversed().compare(pa, pb));

        /**
         * Optinals
         * */
        Optional<String> opt = Optional.of("bam");
        System.out.println(opt.isPresent() + "\t" + opt.get() + "\t" + opt.orElse("fallback"));
        opt.ifPresent((s) -> System.out.println(s.charAt(0)));
    }
}
