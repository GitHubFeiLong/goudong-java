package com.goudong.modules.simple.thread.p5;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.checkerframework.checker.units.qual.A;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public class LoadCacheExample {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) throws ExecutionException {
        LoadingCache<Long, AtomicLong> counter = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .build(new CacheLoader<Long, AtomicLong>() {
                    @Override
                    public AtomicLong load(Long key) throws Exception {
                        return new AtomicLong(0);
                    }
                });

        long limit = 1000;

        while(true) {
            long s = System.currentTimeMillis() / 1000;

            if (counter.get(s).incrementAndGet() > 1000) {
                System.out.println("限流了");
            }
            System.out.println("处理业务...");
        }
    }
}
