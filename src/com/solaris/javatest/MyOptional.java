package com.solaris.javatest;

import java.util.Optional;
import java.util.function.Supplier;

public class MyOptional{
    public static void main(String[] args) {
        create();
        get();
        map();
    }

    /*
        创建Optional实例，3个方法
        ①empty()
        ②of()，如果输入Null会抛出异常
        ③ofNullable(),可以输入null
     */
    public static void create() {
        Optional<String> emptyOpt = Optional.empty();
        Optional<String> strOpt = Optional.of("1");
        try {
            strOpt = Optional.of(null);//抛出异常
            strOpt = Optional.of(null);//抛出异常
        } catch (Exception e) {
        }
        strOpt = Optional.ofNullable(null);
    }

    /*
        获取值
        ①get(),为null会抛出异常NoSuchElementException
        ④orElse()，返回值，如果为空返回输入的默认值
        ④orElseGet()，返回值，如果为空返回提供的lambda表达式
        ④orElseThrow()，返回值，如果为空返回提供的lambda表达式来抛出异常
        ②isPresent()，返回是否有值
        ③ifPresent(),如果非null，则执行lambda表达式

     */
    public static void get() {
        Optional<String> strOpt = Optional.of("1");
        Optional<String> emptyOpt = Optional.ofNullable(null);
        String str = strOpt.get();
        assert(strOpt.isPresent());
        assert(!emptyOpt.isPresent());
        emptyOpt.ifPresent(e-> System.out.println(e));
        assert (strOpt.orElse("2").equals("1"));
        assert (emptyOpt.orElse("3").equals("3"));
        assert (emptyOpt.orElseGet(()->"4").equals("4"));
        try {
            emptyOpt.orElseThrow(()->new IllegalArgumentException("1"));
        }catch (Exception e){
            System.out.println("succ throw");
        }
    }

    /*
        转换map
        ①map(),输入map函数，返回新的实例。如果旧实例为空，则不执行。
        ②flatMap()，输入map函数（返回optional实例），其他类似于map
        ③filter()，输入filter函数，如果断言true，则返回值，否则返回empty
     */
    public static void map() {
        Optional<String> strOpt = Optional.of("1");
        Optional<String> emptyOpt = Optional.ofNullable(null);
        assert strOpt.map(s->s+"2").get().equals("12");
        strOpt.flatMap(s->Optional.of(s+"2"));
        assert  strOpt.filter(s->s.equals("1")).isPresent();
    }
}
