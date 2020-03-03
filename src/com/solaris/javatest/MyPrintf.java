package com.solaris.javatest;

import org.junit.Test;

//printf String.format()
public class MyPrintf {
    @Test
    public void testMain() {
        testNOAgur();
        testFlags();
        testPrecision();
        testArgument_index();
    }

    @Test
    public void testArgument_index() {
        System.out.printf("%1$d %1$d %2$d %<d  %n",1,2);//1 1 2 2
    }

    @Test
    public void testNOAgur() {//不带入参
        //%[flags][width]conversion
        System.out.printf("%n");
    }

    @Test
    public void testFlags() {
        System.out.printf("左对齐%-10s %n","var");// -
        System.out.printf("有符号%+d %n",10);// +
        System.out.printf("正数前有空格 % d%n",5);// ' '
        System.out.printf("用0填充%05d %n",5);// 0
        System.out.printf("数字分隔符%,d %n",1234);// ,

        //组合 有符号、用0填充、分隔符
        System.out.printf("flag组合%+0,11d %n",12345);//%+0,
    }

    @Test
    public void testPrecision() {//精度
        System.out.printf("%5.7s %n","123456789");//1234567
        System.out.printf("%5.7s %n","123456");//1234567
        {
            System.out.printf("%.4g %n",12345.12345);//1.235e+04
            System.out.printf("%.5g %n",12345.12345);//12345
            System.out.printf("%.6g %n",12345.12345);//12345.1

            System.out.printf("%.4f %n",12345.12345);//12345.1235
            System.out.printf("%.5f %n",12345.12345);//12345.12345
            System.out.printf("%.6f %n",12345.12345);//12345.123450
        }
    }


}
