/*
 * Copyright Alex Mylnikov 2017.
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

import org.matrixlab.pisces.metastore.core.Pair;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import java.io.EOFException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexmy
 */
public class BFilter implements Runnable {

    BlockingQueue<HashCode> queue;
    Map<Long, Pair> map;

    public BFilter(BlockingQueue<HashCode> queue, Map<Long, Pair> map) {
//        this.bloomFilter = bloomFilter;
        this.queue = queue;
        this.map = map;
    }

    @Override
    public void run() {
        try {
            updateBloomFilter(queue, map);
        } catch (IOException ex) {
            Logger.getLogger(BFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateBloomFilter(BlockingQueue<HashCode> queue, Map<Long, Pair> map) throws IOException {

        byte[] ba = queue.poll().asBytes();

        long uint = getUInt32(ba);
        Pair pair = map.get(uint);
        if (pair == null) {
            BloomFilter<byte[]> bloomFilter = BloomFilter.create(Funnels.byteArrayFunnel(), 1024 * 1024, 0.001);
            bloomFilter.put(ba);
            pair = new Pair(1, bloomFilter);
            map.put(uint, pair);
        } else {
            BloomFilter<byte[]> bloomFilter = pair.getBloomFilter();
            if (bloomFilter.put(ba)) {
                pair.setCount(pair.getCount() + 1);
                pair.setBloomFilter(bloomFilter);
            }
        }
    }

    public long getUInt32(byte[] bytes) throws EOFException, IOException {
        long value = byteAsULong(bytes[0]);

//        for (int i = 1; i < 2; i++) {
//        value = value | (byteAsULong(bytes[1]) << 2);
//        }
        return value;
    }

    public long byteAsULong(byte b) {
        return ((long) b) & 0x00000000000000FFL;
    }

}
