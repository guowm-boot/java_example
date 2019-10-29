package com.solaris.javatest;

import java.io.File;
import java.io.FilenameFilter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MyCompletableFuture {
    static void completedFutureExample() {
        CompletableFuture cf = CompletableFuture.completedFuture("message");
        assert (cf.isDone());
        assert ("message"==cf.getNow(null));
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int i = 1 / 0;
            return 100;
        });
        //future.join();
        future.get();
    }
}
