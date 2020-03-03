package com.solaris.guava;

import com.google.common.base.Preconditions;

public class MyPrecondition {
    public static void main(String[] args) {
        String str=null;
        Preconditions.checkArgument(false);
        Preconditions.checkArgument(2>5,"err msg:%s","aa");
        Preconditions.checkNotNull(str);
    }
}
