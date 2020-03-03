package com.solaris.guava;

//stream流操作

import com.google.common.collect.Lists;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class MyStream {
    public static String[] strArray = {"gently", "down", "the", "stream"};

    public static void main(String[] args) {
        intStream();//流里每个元素都是整形
        doOrder();//执行顺序,纵向执行
        multiInvoke();//stream 终端操作后stream流无效的解决方案
        reduce();//reduce的例子
        peekExample();//测试peek是否能改变源数据。能够改变源集合每个元素引用对应的对象，但是不能改变源集合。
    }

    private static void peekExample() {
        //可以改变源元素引用对应的对象
        System.out.println();
        System.out.println("---begin peekExample");
        List<StringBuilder> buildList = Lists.newArrayList();
        buildList.add(new StringBuilder().append("1"));
        buildList.add(new StringBuilder().append("2"));
        buildList.add(new StringBuilder().append("3"));
        buildList.stream().peek(e -> {
            e.append("-");
        }).findAny();
        ;
        System.out.println("list:" + buildList);
    }

    private static void reduce() {
        System.out.println();
        System.out.println("---begin reduce");
        //1.普通reduce(初始值可能会被使用多次)
        Supplier<Stream<String>> strSupplier = () ->
                Stream.of("A", "B", "B", "C", "D", "E", "E", "E", "F");
        String concat = strSupplier.get().parallel().reduce("-",
                (l, r) -> {
//                    System.out.println("reduce: l:[" + l + "] r:[" + r + "] t:" +
//                            Thread.currentThread().getName());
                    return l + r;
                }
                , (u1, u2) -> {
//                    System.out.println("reduce combiner: l:[" + u1 + "] r:[" + u2 + "]");
                    return u1 + "/" + u2;
                }
        );
        System.out.println("result:" + concat);

        //2.collcet
        System.out.println("all length:" + strSupplier.get()
                .collect(Collectors.summingInt(String::length)));
        strSupplier.get().collect(Collectors.summarizingInt(String::length)).getSum();
    }

    @Test
    public  void toCollection() {
        Supplier<Stream<String>> stream = () -> {
            return Stream.of("a", "b", "c");
        };

        //1. Stream->数组
        Object[] objects = stream.get().toArray();
        String[] strings = stream.get().toArray(String[]::new);
        //2. Stream->集合
        List<String> list1 = stream.get().collect(Collectors.toList());
        List<String> list2 = stream.get().collect(Collectors.toCollection(LinkedList::new));
        Set set = stream.get().collect(toSet());
        Stack stack = stream.get().collect(Collectors.toCollection(Stack::new));
        //3.Stream->String,连在一起
        String str = stream.get().collect(Collectors.joining("-", "begin[", "]end"));
        System.out.println("join:" + str);
    }

    private static void multiInvoke() {
        //使用Supplier
        Supplier<Stream<String>> streamSupplier =
                () -> Stream.of("d2", "a2", "b1", "b3", "c")
                        .filter(s -> s.startsWith("a"));

        streamSupplier.get().anyMatch(s -> true);   // ok
        streamSupplier.get().noneMatch(s -> true);  // ok
    }

    private static void doOrder() {
        //每个元素执行完所有指令，再移动到下一个元素。
        //而不是一个指令执行所有元素，再执行下个指令的所有元素
        System.out.println();
        System.out.println("---begin doOrder");
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return true;
                })
                .forEach(s -> System.out.println("forEach: " + s));
    }

    private static void intStream() {
        System.out.println();
        System.out.println("---begin intStream");
        IntStream s = IntStream.rangeClosed(1, 100);
        //System.out.println(s.max().orElse(0));
        System.out.println(s.average());

        Stream.of("a1", "a2", "a3")
                .map(str -> str.substring(1))
                .mapToInt(Integer::parseInt)
                .max()
                .ifPresent(System.out::println);
    }

    @Test
    public void toMap() {
        Supplier<Stream<String>> strSupplier = () ->
                Stream.of("A", "B", "B", "C", "D", "E", "E", "E", "F");

        //1 普通的
        Map<String, Integer> tmpMap = strSupplier.get()
                .collect(Collectors.toMap(String::toString, String::length, (old, now) -> old));

        //2 value是自身
        Map<Integer, String> idToStr = strSupplier.get()
                .collect(Collectors.toMap(Object::hashCode, Function.identity(), (old, now) -> old));

        //3 to已有map
        Map<Integer, String> oldMap = new HashMap<>();
        oldMap.put(-1, "wahaha");
        strSupplier.get()
                .collect(Collectors.toMap(Object::hashCode, Function.identity()
                        , (old, now) -> old, () -> oldMap));
        System.out.println(oldMap);

        //4 to new map
        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales())
                .filter(s -> !s.getDisplayLanguage().isEmpty());
        Map<String, String> languageNames = locales.collect(Collectors.toConcurrentMap(
                Locale::getDisplayLanguage,
                Locale::getDisplayName,
                (existingValue, newValue) -> existingValue + " and " + newValue,//map的key值有重复的处理
                ConcurrentHashMap::new)
        );
        System.out.println("num:" + languageNames.size() + " " + languageNames);

        //5 value比较复杂，如list/set
        Map<String, List<Locale>> countryToLocales = Stream.of(Locale.getAvailableLocales()).collect(
                Collectors.groupingBy(Locale::getDisplayCountry));
        System.out.println("map->list:" + countryToLocales);

        Map<String, Set<Locale>> groupResultSet = Stream.of(Locale.getAvailableLocales()).collect(
                Collectors.groupingBy(Locale::getDisplayCountry, toSet()));

        //6 partitioningBy 与groupBy的区别在于，此key为bool型
        Map<Boolean, List<Locale>> partitioningByResult = Stream.of(Locale.getAvailableLocales()).collect(
                Collectors.partitioningBy(e -> e.getDisplayCountry().equals("中国")));
        System.out.println("partitioningBy :" + partitioningByResult.get(true));

    }

    @Test
    public void simpleExample() {
        //1.demo
        List<String> myList = Arrays.asList("a1", "a2", "b1", "c2", "c1");
        myList.stream()
                .filter(s -> s.startsWith("c"))
                .map(String::toUpperCase)
                .sorted()
                .forEach(System.out::println);
        //2.flatMap
        Stream<List<Integer>> inputStream = Stream.of(
                Arrays.asList(1),
                Arrays.asList(2, 3),
                Arrays.asList(4, 5, 6)
        );
        Stream<Integer> outputStream = inputStream.
                flatMap((childList) -> childList.stream());
        System.out.println("flatMap:" + outputStream.collect(Collectors.toList()));
        //3.parallelStream peek
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        numbers.parallelStream().peek(num ->
                System.out.println(Thread.currentThread().getName() + ">>" + num));
    }

    @Test
    public void create() throws Exception {//create stream
        Stream<String> strStream = null;

        //empty stream
        strStream = Stream.empty();

        //source is string
        String myTestStr = "this is test";
        IntStream intStream = myTestStr.codePoints();
        System.out.println("strings:"+intStream.boxed().collect(toList()));

        //source is Array
        strStream = Stream.of(strArray);
        strStream = Arrays.stream(strArray);
        strStream = Arrays.stream(strArray, 0, strArray.length);

        //Stream.generate方法,生成重复的
        Stream<Double> randoms = Stream.generate(Math::random).limit(100);
        randoms = Stream.generate(() -> 0.1).limit(100);
        System.out.println("random num:" + randoms.count());

        //Stream.iterate 生成有规律的
        System.out.println(
                Stream.iterate(0, n -> n + 1)
                        .limit(5).mapToInt(n -> n).max().getAsInt());

        //其他类能生成Stream
        Stream<String> words = Pattern.compile("[\\P{L}]+").splitAsStream("solaris");
        Stream<String> lines = Files.lines(Paths.get("C:\\Windows\\System32\\drivers\\etc\\hosts"));
        //lines.filter(e->!e.contains("#") && !e.isEmpty()).forEach(e->System.out.println(e));
    }

    @Test
    public void transform() {//map、flatMap
        //map
        Stream<String> stream = Arrays.stream(strArray).map(String::toUpperCase);
        //GENTLY-DOWN-THE-STREAM
        System.out.println("map:"+stream.collect(Collectors.joining("-")));

        //flatMap
        Stream<String> flatStream= Arrays.stream(strArray).flatMap(
                e->{
                    List<String> list=new ArrayList<>();
                    for (int index = 0; index < e.length(); index++) {
                        list.add(e.substring(index, index + 1));
                    }
                    return list.stream();
                }
        ).distinct();
        //g-e-n-t-l-y-d-o-w-h-s-r-a-m
        System.out.println("flatMap:"+flatStream.collect(Collectors.joining("-")));
    }

    @Test
    public void combine(){//合并两个Stream
        String[] a={"1","2","3"};
        String[] b={"4","5","6"};
        Stream<String> stream = Stream.concat(Arrays.stream(a), Arrays.stream(b));
        String sink = stream.skip(1).limit(3).collect(Collectors.joining("-"));
        System.out.println(sink);//2-3-4
    }

    @Test
    public void mergeMap() {//mapA->mapB
        HashMap<String, String> sourceMap = new HashMap<>();
        sourceMap.put("1", "time");
        sourceMap.put("2", "solaris");
        sourceMap.put("3", "suck");
        sourceMap.put("4", "end");
        HashMap<String, String> sinkMap = new HashMap<>();
        sourceMap.entrySet().stream()
                .filter(e -> e.getValue().contains("s"))
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue(),
                        (oldvalue, newValue) -> newValue,
                        () -> sinkMap
                ));
        System.out.println("sink map" + sinkMap);
    }



    @Test
    public void toSummary() {
        IntSummaryStatistics statistics = IntStream.rangeClosed(1, 100).boxed()
                .collect(Collectors.summarizingInt(n -> n));
        System.out.println(statistics.getCount());
        System.out.println(statistics.getSum());
        System.out.println(statistics.getAverage());
        System.out.println(statistics.getMax());
    }

    @Test
    public void groupBy(){
        List<String> myList = Arrays.asList("a1", "a2", "b1", "c2","c2", "c1","c34");
        //groupingBy
        Map<String, Long> strCountMap = myList.stream()
                .collect(Collectors.groupingBy(String::toString, Collectors.counting()));
        System.out.println("groupBy:" + strCountMap);

        //partitioningBy 分成2个部分
        Map<Boolean, Long> partitioningByMap = myList.stream()
                .collect(Collectors.partitioningBy(e -> e.length() > 2, Collectors.counting()));
        System.out.println("partitioningBy:" + partitioningByMap);

        //value结构更复杂，处理downStream
        Map<String, Set<String>> strCountMap1 = myList.stream()
                .collect(Collectors.groupingBy(e->e.substring(0,1), toSet()));
        System.out.println("downStream1:" + strCountMap1);

        Map<String, Integer> strCountMap2 = myList.stream()
                .collect(Collectors.groupingBy(e->e.substring(0,1), summingInt(String::length)));
        System.out.println("downStream2:" + strCountMap2);
    }
}

class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return name;
    }
}

