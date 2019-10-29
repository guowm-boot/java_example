package com.solaris.javatest;

//多参数
public class MultiParam {

    int getMax(int... v){
        int ret=0;
        for (int i :v) {
            ret=i>ret?i:ret;
        }
        return  ret;
    }

    public static void main(String... args) {//多参数的main
        MultiParam obj=new MultiParam();
        System.out.println(obj.getMax(1,2,4,4,5));
    }
}
