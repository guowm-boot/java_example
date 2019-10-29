package com.solaris.javatest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This program displays a greeting for the reader.
 * solaris 注释文档
 * @version 1.30 2014-02-27
 * @author Cay Horstmann
 */



public class Welcome
{
   static final int MyLONG=4; //整型类常量
   final int MyLONG1=4; //整型类常量

   int initField=4;
   int initBlock;

   {

      initField=3;
      initBlock=5;
   }

   static  int staticInitBlock=5;//static field初始化块
   static {
      staticInitBlock=6;
   }
   public static void main(String[] args)
   {
      String greeting = "Welcome to Core Java!";
      System.out.println(greeting);

      //初始化测试 构造函数
      Welcome obj=new Welcome();
      System.out.println("初始化测试："+obj.initField+"-"+obj.initBlock);

      for (int i = 0; i < greeting.length(); i++)
         System.out.print("=");
      System.out.println();
      //solaris add 基本类型
      System.out.println(010);//8进制 0
      System.out.println(0x10);//16进制 0x
      System.out.println(0B101);//二进制 0B
      System.out.println(10L);//长整型 L
      System.out.println(100_0000_0000L);//方便肉眼识别 100亿
      System.out.println(3.1415F); //float
      System.out.println(3.141515927D); //double 默认
      System.out.println(2.0-1.1);//二进制不能精确表示1/10，例如十进制无法精确表示1/3
      System.out.println((2.1+1.1));
      final long aLong=0; //常量
      System.out.println("2^10="+Math.pow(2,10));
      System.out.println("-10%3="+-10%3);
      System.out.println("-10%3="+StrictMath.floorMod(-10,3));
      System.out.println("-10%3="+Math.floorMod(-10,3));

      //string
      String str="mytest";
      System.out.println(str.substring(1, 1+2));
      ArrayList<String> a=new ArrayList<>();
      a.add("x1");
      a.add("x2");
      a.add("x3");
      System.out.println("join="+String.join("-", a));
      System.out.println(str.equalsIgnoreCase("MYTESt"));
      System.out.println(str.equals("mytest"));
      //string builder
      StringBuilder build= new StringBuilder();//StringBuffer是线程安全的类
      build.append(1);
      build.append("2");
      String strBuild=build.toString();
      System.out.println("this is append mode :"+strBuild);

      //code point
      String complexStr="\ud546我在吃饭";

      System.out.println(complexStr+" codePointCount="+complexStr.codePointCount(0,complexStr.length()));
      System.out.println(complexStr+" length="+complexStr.length());

      //print
      System.out.printf("hello:%s","hello\n");
      System.out.printf("print double 8.3=%.5f\n",1.0/3);

      here:
      {
         int loopNun=0;
         c1:
         for (int i=0;i<10;i++){
            for (int j = 0; j < 10; j++) {
               loopNun++;
               if (j== 2){
                  continue  c1;
               }
            }
         }
         System.out.println("continue xx,loopNum:"+loopNun);
         if (true) {
            break here;
         }
         System.out.println("not run");
      }

      //大数
      BigDecimal bd=BigDecimal.valueOf(10.5);
      BigDecimal bd1=BigDecimal.valueOf(50.2);
      System.out.println("大整数除法，四舍五入："+bd.divide(bd1, RoundingMode.HALF_UP));

      //数组
      {
         String[] tmp=new String[1];
         String[] tmp1={"1","2"};
         String[] tmp2=new String[]{"1","2","3","4"};
         String[] tmp3=Arrays.copyOf(tmp2,tmp2.length);
         Arrays.sort(tmp3);
         System.out.println("数组toString:"+Arrays.toString(tmp3));
         System.out.println("binarySearch result:"+Arrays.binarySearch(tmp3, "5"));
         Arrays.fill(tmp3,"test");
         for (String i:tmp) {
            System.out.println("arrays,length:"+tmp.length+" value:"+i);
         }
      }



      System.exit(-2);// 程序退出码

   }
}
