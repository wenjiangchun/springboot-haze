package com.haze.demo.jdk8.lamda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class LamdaAndMethodRefDemo {

    public void useLamdaExp() {
        //-------------------静态方法----------------------//
        //lamda引用
        Predicate predicate = (n -> EventOddCheck.isEvent(n));
        System.out.println(predicate.test(20));
        //方法引用
        Predicate predicate1 = EventOddCheck::isEvent;
        System.out.println(predicate1.test(25));

        //-------------------类实例方法----------------------//
        MathOperation op = new MathOperation();
        //lamda引用
        BiFunction<Integer, Integer, Integer> add1 = (a, b) -> op.add(a, b);
        System.out.println("Addtion = " + add1.apply(4, 5));

        BiFunction<Integer, Integer, Integer> sub1 = (a, b) -> op.sub(a, b);
        System.out.println("Subtraction = " + sub1.apply(58, 5));

        //方法引用
        BiFunction<Integer, Integer, Integer> add2 = op::add;
        System.out.println("Addtion = " + add2.apply(4, 5));

        BiFunction<Integer, Integer, Integer> sub2 = op::sub;
        System.out.println("Subtraction = " + sub2.apply(58, 5));



        List<String> weeks = new ArrayList<String>();
        weeks.add("Monday");
        weeks.add("Tuesday");
        weeks.add("Wednesday");
        weeks.add("Thursday");
        weeks.add("Friday");
        weeks.add("Saturday");
        weeks.add("Sunday");
        //lamda引用
        System.out.println("--------------------Using Lambda Expression----------------------");
        weeks.stream().map((s)-> s.toUpperCase()).forEach((s)->System.out.println(s));
        //方法引用
        weeks.stream().map(String::toUpperCase).forEach(System.out::println);

        //-------------------构造方法----------------------//
        //lamda引用
        BiConsumer<Integer, Integer> addtion1 = (a, b) -> new MathOperation(a, b);
        addtion1.accept(10, 20);
        //方法引用
        BiConsumer<Integer, Integer> addtion2 = MathOperation::new;
        addtion2.accept(50, 20);
    }

}
