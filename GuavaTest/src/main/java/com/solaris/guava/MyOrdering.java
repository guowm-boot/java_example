package com.solaris.guava;

import com.google.common.base.MoreObjects;
import com.google.common.base.Throwables;
import com.google.common.collect.*;
import org.omg.CORBA.IntHolder;

import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;

public class MyOrdering {
    public static void main(String[] args) {
//        {
//            List<Integer> intList = Lists.newArrayList(1, 11, 111, 222, 3333, 444, 5);
//            System.out.println(Ordering.natural().sortedCopy(intList));
//            System.out.println(Ordering.usingToString().sortedCopy(intList));
//            System.out.println(Ordering.arbitrary().sortedCopy(intList));
//            System.out.println(Ordering.arbitrary().nullsFirst().sortedCopy(intList));
//        }
        {
            Comparator<String> strComp = (a, b) -> a.length() - b.length();
            List<Human> humanList= Lists.newArrayList(new Human("solaris",34),
                    new Human("betty111",34),
                    new Human("kin",12),new Human("ask",59));
            humanList.sort((c1,c2)->c1.getAge()-c2.getAge());
            humanList.sort(Comparator.comparing(System::identityHashCode));
            System.out.println("identityHashCode:"+humanList);
            humanList.sort(Comparator.comparing(Object::toString));
            humanList.sort(Comparator.comparing(Human::getName));
            humanList.sort(Comparator.comparing(Human::getAge));
            humanList.sort(Comparator.comparing(Human::getAge).reversed());
            humanList.sort(Comparator.comparing(Human::getAge).reversed()
                    .thenComparing(Human::getName).reversed());
            humanList.add(null);
            humanList.sort(Comparator.nullsLast(Comparator.comparing(Human::getAge)
                    .thenComparing(s->s.getName().length())));
            System.out.println(humanList);
        }
        testCollectionOfCollection();
    }

    private static void testCollectionOfCollection() {//比较集合的集合
        List<List<Human>> humanList= Lists.newArrayList(
                Lists.newArrayList(new Human("a",31),
                        new Human("b",22)
                        ,new Human("c",5)),
                Lists.newArrayList(new Human("a",31),
                        new Human("b",22)
                        ,new Human("c",30)),
                Lists.newArrayList(new Human("a",31),
                        new Human("b",22))
        );
        Comparator<Iterable<Human>> comp1= Comparators.lexicographical(
                Comparator.comparing(Human::getAge).thenComparing(Human::getName));
        humanList.sort(comp1);
        System.out.println(humanList);

        //集合的最小值，最大值
        Collections.min(humanList,comp1);
        System.out.println(MoreObjects.toStringHelper(Collections.class).
                add("xx", 40)
                .add("yy","222")
        );
    }

}

class Human implements Comparable {
    private String name;
    private int age;

    public Human() {
        super();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("")
                .omitNullValues()
                .add("name",name)
                .add("age",age)
                .toString();
    }

    public Human(final String name, final int age) {
        super();
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }

    @Override
    public int compareTo(Object that) {
        Human o=(Human)that;
        return ComparisonChain.start()
                .compare(this.name, o.name)
                .compare(this.age, o.age)
                .result();
    }
}