package com.solaris.javatest;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.*;


class Employee {
    public Employee(String name, int i) {
        this.name=name;
        this.age=age;
    }

    Employee(){};

    Employee(String name){
        this(name,30);
    };
    String name="";
    int    age=22;
}


//集合
public class MyCollection {
    public static void main(String[] args) {

        testConcurrent();


        List<String> strList=new ArrayList<>();
        strList.add("1");
        strList.add("2");
        strList.add("3");
        strList.add("4");
        //简单的方法遍历
        strList.iterator().forEachRemaining(e-> System.out.println(e));
        strList.removeIf(e->Objects.equals(e,"2"));
        System.out.println("after removeIf:"+strList);
        String[] strArr= strList.toArray(new String[0]);
        System.out.println("to toArray:"+Arrays.toString(strArr));
        //求交集
        List<String> bList=new ArrayList<>();
        bList.add("3");
        bList.add("8");
        String before=strList.toString();
        strList.retainAll(bList);
        System.out.println("#求交集--etainAll()=before:"+before+" other:"+bList+" after:"+strList);

        //map
        Map<String, Employee> employeeMap=new HashMap<>();
        employeeMap.put("solaris",new Employee("solaris",30));
        Map<String, Integer> strIntMap=new HashMap<>();
        strIntMap.merge("kin",1,Math::addExact);
        strIntMap.merge("kin",2, Math::subtractExact);
        strIntMap.merge("kin",10, (x,y)->x*y);
        strIntMap.merge("solaris",32,Integer::sum);
        strIntMap.merge("ken",35,Integer::sum);
        System.out.println("#map 合并结果"+strIntMap);
        employeeMap.forEach((k,v)-> System.out.println(k));


        System.out.println("#map->key: set:"+strIntMap.keySet());
        System.out.println("#map->value collection:"+strIntMap.values());
        System.out.println("#map->entry set:"+strIntMap.entrySet());

        //弱引用Map
        WeakHashMap<String,Integer> weakMap=new WeakHashMap(strIntMap);
    }

    static void  testConcurrent(){
        try {
            ArrayList<String> strList=new ArrayList<>();
            strList.add("1");
            strList.add("2");
            strList.add("3");
            ListIterator<String> it1=strList.listIterator();
            ListIterator<String> it2=strList.listIterator();
            it1.next();
            it1.nextIndex();
            it1.remove();
            it2.next();
        }
        catch (Exception e){
            System.out.println("concurrent ex:"+e);
        }
    }
}


class TestTreeSet{
    public static void test() {
        SortedSet<String> strSet=new TreeSet<>();
        strSet.add("1");
        strSet.add("2");
        strSet.add("3");
        strSet.add("22");
    }
}