package com.solaris.guava;

import com.google.common.collect.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MyCollection {
    public static void main(String[] args) {
        testMultiSet();
        testMultiMap();
        testBiMap();//双向map
        testTable();//表格，3纬
        testClassToInstanceMap();//key为类型
        testRangeSet();
        testRangeMap();
    }

    static void testRangeMap() {

        RangeMap<Integer, String> rangeMap = TreeRangeMap.create();
        rangeMap.put(Range.closed(1, 10), "foo"); // {[1, 10] => "foo"}
        rangeMap.put(Range.open(3, 6), "bar"); // {[1, 3] => "foo", (3, 6) => "bar", [6, 10] => "foo"}
        rangeMap.put(Range.open(10, 20), "foo"); // {[1, 3] => "foo", (3, 6) => "bar", [6, 10] => "foo", (10, 20) => "foo"}
        rangeMap.remove(Range.closed(5, 11)); // {[1, 3] => "foo", (3, 5) => "bar", (11, 20) => "foo"}
    }

    static void testRangeSet() {
        System.out.println();
        System.out.println("---begin testRangeSet");
        RangeSet<Integer> rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closed(1, 10)); // {[1, 10]}
        rangeSet.add(Range.closedOpen(11, 15)); // disconnected range: {[1, 10], [11, 15)}
        rangeSet.add(Range.closedOpen(15, 20)); // connected range; {[1, 10], [11, 20)}
        rangeSet.add(Range.openClosed(0, 0)); // empty range; {[1, 10], [11, 20)}
        rangeSet.remove(Range.open(5, 10)); // splits [1, 10]; {[1, 5], [10, 10], [11, 20)}
        System.out.println("contains:"+rangeSet.contains(5));
        System.out.println("span:"+rangeSet.span()+" range:"+rangeSet);

        //对整形数据判断连续
        rangeSet.clear();
        rangeSet.add(Range.closed(1, 10).canonical(DiscreteDomain.integers())); // {[1, 10]}
        rangeSet.add(Range.closedOpen(11, 15).canonical(DiscreteDomain.integers())); // disconnected range: {[1, 10], [11, 15)}
        System.out.println(rangeSet.asRanges());
        System.out.println("补集:" + rangeSet.complement());
        System.out.println(rangeSet.rangeContaining(3));
    }

    static void testClassToInstanceMap() {
        System.out.println();
        System.out.println("---begin testClassToInstanceMap");
        ClassToInstanceMap<Object> numberDefaults = MutableClassToInstanceMap.create();
        numberDefaults.putInstance(Integer.class, Integer.valueOf(0));
        numberDefaults.putInstance(Integer.class, 1);
        numberDefaults.putInstance(int.class, 1);
        numberDefaults.putInstance(double.class, 0.2);
        numberDefaults.putInstance(Long.class, 3L);
        numberDefaults.putInstance(Human.class, new Human("maru", 20));
        System.out.println(numberDefaults);
    }

    static void testTable() {
        System.out.println();
        System.out.println("---begin testTable");
        String v1 = "v1", v2 = "v2", v3 = "v3";
        Table<String, String, Integer> weightedGraph = HashBasedTable.create();
        weightedGraph.put(v1, v2, 4);
        weightedGraph.put(v1, v3, 20);
        weightedGraph.put(v2, v3, 5);

        System.out.println(weightedGraph.row(v1));// returns a Map mapping v2 to 4, v3 to 20
        System.out.println(weightedGraph.column(v3));// returns a Map mapping v1 to 20, v2 to 5
    }

    static void testBiMap() {
        System.out.println();
        System.out.println("---begin testBiMap");
        BiMap<String, Integer> nameAgeMap = HashBiMap.create();
        nameAgeMap.put("3", 12);
        nameAgeMap.forcePut("4", 12);
        nameAgeMap.forcePut("4", 11);
        System.out.println(nameAgeMap);
    }

    static void testMultiMap() {
        System.out.println();
        System.out.println("---begin testMultiMap");
        // creates a ListMultimap with tree keys and array list values
        ListMultimap<String, Integer> treeListMultimap =
                MultimapBuilder.treeKeys().arrayListValues().build();

        // creates a SortedSetMultimap
        SortedSetMultimap<String, Integer> hashEnumMultimap =
                MultimapBuilder.hashKeys().treeSetValues().build();

        treeListMultimap.put("1", 2);
        treeListMultimap.put("1", 2);
        treeListMultimap.put("1", 2);
        System.out.println(treeListMultimap);//{1=[2, 2, 2]}

        //简单的方法判断key是否在multimap中存在
        System.out.println("is key in map:" + treeListMultimap.asMap().get("4"));
    }

    static void testMultiSet() {
        System.out.println();
        System.out.println("---begin testMultiSet");
        Multiset<String> mset;
        mset = TreeMultiset.create();
        mset = HashMultiset.create(Lists.newArrayList("2"));
        mset.add("2", 10);
        mset.addAll(Arrays.asList("2", "4", "6", "6", "8"));
        System.out.println("element:" + mset.elementSet());
        System.out.println("entrySet:" + mset.entrySet());
        System.out.println("size:" + mset.size() + " '2' count:" + mset.count("2"));

        SortedMultiset<String> sortSet = TreeMultiset.create(mset);
        System.out.println("SortedMultiset subMultiset:" + sortSet.subMultiset("2", BoundType.CLOSED,
                "7", BoundType.CLOSED));
    }
}
