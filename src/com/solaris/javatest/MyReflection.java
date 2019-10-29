package com.solaris.javatest;

//反射

class ReflectionExample {
    int a = 0;
    int b = 0;

    ReflectionExample() {
    }

    ReflectionExample(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public String toString() {
        return "" + a + " " + b;
    }

}

public class MyReflection {
    public static void main(String[] args) {
        ReflectionExample obj = new ReflectionExample();

        System.out.println(obj.getClass());
        try {
            Class cl = obj.getClass();
            ReflectionExample construcObj=(ReflectionExample) cl.getConstructor().newInstance();
            String className = "v1ch02.Welcome.ReflectionExample";
            Object objTmp = Class.forName(className).newInstance();
            ReflectionExample obj1 = (ReflectionExample) objTmp;
            System.out.println("newInstance:" + obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
