package com.solaris.guava;

import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class MyCache {
    public static void main(String[] args) throws Exception{
        createCache();//创建缓存
        createExpireCache();
    }

    private static void createExpireCache() throws Exception {
        LoadingCache<String, String> graphs = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.SECONDS)
//                .ticker(new Ticker() {
//                    @Override
//                    public long read() {
//                        return systemTicker().read()-3*1000*1000*1000;
//                    }
//                })
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) {
                        return "null";
                    }
                });
        graphs.put("1","1");
        Thread.sleep(2000);
        System.out.println(graphs.get("1"));//"null"
    }

    private static void createCache() {
        LoadingCache<String, String> graphs = CacheBuilder.newBuilder()
                //.maximumSize(10) //最大条目
                .maximumWeight(10) //最大权重
                .weigher(
                        (String k, String v) -> k.length() + v.length()
                )
                .build(
                        new CacheLoader<String, String>() {
                            public String load(String key) {
                                return "";
                            }
                        }
                );
        graphs.asMap();
    }
}
