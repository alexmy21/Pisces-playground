/* Copyright Alex Mylnikov 2017.
 *
 * This program is distributed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.matrixlab.pisces.metastore;

/**
 *
 * @author alexmy
 */
import org.matrixlab.pisces.metastore.core.Pair;
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

public class Main {
    
    BlockingQueue<HashCode> queue;
    HashFunction hf;
    
    public Main(){
        this.hf = Hashing.murmur3_128(0);
        this.queue = createQueue(1500000);        
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        Main main = new Main();
        
        long start = System.currentTimeMillis();
        System.out.println("Queue creating time: " + (System.currentTimeMillis() - start));
        System.out.println("Queue is done!");
        
        // Bloom Filter buckets
        Map<Long, Pair> bloomMap = new HashMap<>();

        int threads = 300;
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        start = System.currentTimeMillis();
        main.processBloomFilter(threads, main.queue, bloomMap, executor);

        System.out.println("Queue processing time: " + (System.currentTimeMillis() - start));

        System.exit(0);
    }

    /**
     * 
     * @param size
     * @return 
     */
    public final BlockingQueue<HashCode> createQueue(int size) {
        
        this.queue = new LinkedBlockingQueue<>();
        // create random object
        Random randomno = new Random();
        
        for (int i = 0; i < size; ++i) {
            HashCode hashCode = hf.hashLong(randomno.nextLong());
            queue.add(hashCode);
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
    public void processBloomFilter(int threads, BlockingQueue<HashCode> queue, Map<Long, Pair> bloomMap, ExecutorService executor) {
        
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
    public String callableTask(BlockingQueue<HashCode> queue, Map<Long, Pair> map) {
        while (!queue.isEmpty()) {
            BFilter bf = new BFilter(queue, map);
            bf.run();
        }
        return "Task execution" + queue.size();
    }
}
