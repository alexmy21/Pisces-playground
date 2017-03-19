/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.matrixlab.pisces.metastore;

/**
 *
 * @author alexmy
 */
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-5-10
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {

        // Common Hash 
        HashFunction hf = Hashing.murmur3_128(0);

        BlockingQueue<HashCode> queue = createQueue(hf, 1500000);
        long start = System.currentTimeMillis();
        System.out.println("Queue creating time: " + (System.currentTimeMillis() - start));
        System.out.println("Queue is done!");
        
        // Bloom Filter buckets
        Map<Long, Pair> bloomMap = new HashMap<>();

        int threads = 300;
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        start = System.currentTimeMillis();
        processBloomFilter(threads, queue, bloomMap, executor);

        System.out.println("Queue processing time: " + (System.currentTimeMillis() - start));

        System.exit(0);
    }

    /**
     * 
     * @param hf
     * @param size
     * @return 
     */
    public static BlockingQueue<HashCode> createQueue(HashFunction hf, int size) {
        
        BlockingQueue<HashCode> queue = new LinkedBlockingQueue<>();
        // create random object
        Random randomno = new Random();
        
        for (int i = 0; i < size; ++i) {
            HashCode codeFriend = hf.hashLong(randomno.nextLong());
            queue.add(codeFriend);
        }
        return queue;
    }

    /**
     * 
     * @param threads
     * @param queue
     * @param bloomMap
     * @param executor 
     */
    public static void processBloomFilter(int threads, BlockingQueue<HashCode> queue, Map<Long, Pair> bloomMap, ExecutorService executor) {
        
        List<Callable<String>> callableTasks = new ArrayList<>();
        // Build task pool
        for (int i = 0; i < threads; i++) {      
            callableTasks.add((Callable<String>) () -> {
                callableTask(queue, bloomMap);
                return null;
            });
        }
        // Execute all threads
        try {
            List<Future<String>> futures = executor.invokeAll(callableTasks);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Print out results
        bloomMap.keySet().forEach((key) -> {
            Pair pair = bloomMap.get(key);
            System.out.println("Key: " + key + "; count: " + pair.getCount());
        });
    }

    /**
     * 
     * @param queue
     * @param map
     * @return 
     */
    public static String callableTask(BlockingQueue<HashCode> queue, Map<Long, Pair> map) {
        while (!queue.isEmpty()) {
            BFilter bf = new BFilter(queue, map);
            bf.run();
        }
        return "Task execution" + queue.size();
    }
}
