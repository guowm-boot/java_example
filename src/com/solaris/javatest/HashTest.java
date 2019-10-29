package com.solaris.javatest;

import java.util.Arrays;
import java.util.Objects;

public class HashTest {
    private int a=0;
    private int b=0;
    private int c=0;
    private int[]   d={1,2,4,5};

    public static void main(String[] args) {
        HashTest obj=new HashTest();
        String str1="1111";
        String str2="22222";
        System.out.println("hash:"+str1.hashCode()+" "+str2.hashCode());
        System.out.println("object hash:"+new HashTest().hashCode());
        System.out.println(obj.d);
        System.out.println(Arrays.toString(obj.d));
    }

    @Override
    public int hashCode() {
        return Objects.hash(a,b,c,d);
    }
}
