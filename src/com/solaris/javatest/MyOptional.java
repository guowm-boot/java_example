package com.solaris.javatest;

import java.util.Optional;

public class MyOptional {
    public static void main(String[] args) {
        Optional<String> emptyOpt = Optional.empty();
        emptyOpt=Optional.of("1");
        emptyOpt=Optional.ofNullable(null);
        //emptyOpt.get();
    }

    //创建Optional实例
    public static void create() {

    }
}
