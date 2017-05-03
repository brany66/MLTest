package JDK8Test;

import java.util.*;


/**
 * Created by YWJ on 2017.4.18.
 * Copyright (c) 2017 NJU PASA Lab All rights reserved.
 */

/**
 * 每一个lambda都能够通过一个特定的接口，与一个给定的类型进行匹配。一个所谓的函数式接口必须要有且仅有一个抽象方法声明。
 * 每个与之对应的lambda表达式必须要与抽象方法的声明相匹配。由于默认方法不是抽象的，因此你可以在你的函数式接口里任意添加默认方法。
 * @param <F>
 * @param <T>
 */
@FunctionalInterface
interface Converter<F, T> {
    T convert(F form);
}

public class testJDK8 {

    public static void main(String[] args) {

        System.out.println("interface have default implementation: ");
        /** Method 1 */
        Formula tmp = new Formula() {
            @Override
            public double calculate(int a) {
                return sqrt(a * 100);
            }
        };

        System.out.println(tmp.calculate(100) + "\t" + tmp.sqrt(16));

        System.out.println("lambda expression: ");
        /** Method 2 */
        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");

/*        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return b.compareTo(a);
            }
        });*/
        Collections.sort(names, (String a, String b) -> b.compareTo(a));
        System.out.println(names);

        System.out.println("function interface : method & constructor : ");
        /** Method 3 */
        Converter<String, Integer> converter = Integer::valueOf;
                //(from) -> Integer.valueOf(from);
        System.out.println(converter.convert("123"));

        /** Method 4
         * ::
         * */
        Smoothing sm = new Smoothing();
        Converter<String, String> convert = sm::startWith;
        System.out.println(convert.convert("Java"));

        /** Method 5 :: constructor */
        PersonFactory<Person> p = Person::new;
        Person person = p.create("Peter", "Parker");
        System.out.println(person.toString());

        /** Method 6 */
        int num = 1; //final
        Converter<Integer, String> stringConvert = (from) -> String.valueOf(from + num);
        System.out.println(stringConvert.convert(2));

        // testBuildInInterface.fun();

        // streamsTest.fun();

       // parallelStreamTest.fun();
        dateTest.fun();
        /**
         * Integer Short Byte Character Long
         * Float Double
         * Boolean
         * */

        //Integer.valueOf(3); [-128, 127]
        // Short.valueOf("3");
        // Byte.valueOf()
        // Double.valueOf(100.0);
        // Boolean.valueOf(false);
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        Long h = 2L;

        System.out.println(c==d);
        System.out.println(e==f);

        System.out.println(c==(a+b));
        System.out.println(c.equals(a+b));

        System.out.println(g==(a+b));
        System.out.println(g.equals(a+b));
        System.out.println(g.equals(a+h));
    }

}