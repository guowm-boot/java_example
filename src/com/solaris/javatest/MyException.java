package com.solaris.javatest;

/*
    try with resources
    my excetpion
    断言 assert:产生的是一个error异常 用-ea 运行参数启用断言
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.Scanner;

class MyResource implements AutoCloseable{
    public String getName() {
        return name;
    }

    private  String name="11";
    MyResource(){};
    MyResource(String aNmae){this.name=aNmae;};
    @Override
    public void close() throws Exception {
        System.out.println("auto close..."+name);
        this.name=null;
    }
}


public class MyException {
    public static void main(String[] args) {
        //简单测试
        simpleTest();

        //autoClose测试 try-with-resource
        testTryWithResource();
    }

    public static void testTryWithResource() {
        MyResource r0=new MyResource("r0");
        try (MyResource r=new MyResource("r1");
             MyResource r1=r0;
             Scanner in1=new Scanner(new FileInputStream("d:/tmp/xx1.log"),"UTF-8")) {
            System.out.println("run try...");
        }
        catch(Exception e)
        {
            System.out.println("testTryWithResource ex:"+e+e.getCause());
            System.out.println(r0.getName());
            e.getStackTrace();
        }

    }

    private static void simpleTest() {
        try {
            assert false:"my assert false";//断言
            System.out.println("1111");
            if (false){
                throw  new FileNotFoundException("exception——FileNotFoundException");
            }
            if (false){
                throw  new UnknownHostException("exception-UnknownHostException");
            }
            if (false){
                throw  new IllegalArgumentException("err input");
            }
        //一个catch块捕获多个异常
        }catch (FileNotFoundException| UnknownHostException e){
            e.printStackTrace();
            //将原异常设置为新异常的原因
            RuntimeException a=new RuntimeException();
            a.initCause(e);
            throw a;
        }
        catch (Throwable e){
            System.out.println("simpleTest ex:"+e);
        }
    }


}
