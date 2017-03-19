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

import com.google.common.hash.BloomFilter;

/**
 *
 * @author alexmy
 */
public class Pair {
    
    public Pair(int count, BloomFilter<byte[]> bloomFilter){
        this.count = count;
        this.bloomFilter = bloomFilter;
                
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the bloomFilter
     */
    public BloomFilter<byte[]> getBloomFilter() {
        return bloomFilter;
    }

    /**
     * @param bloomFilter the bloomFilter to set
     */
    public void setBloomFilter(BloomFilter<byte[]> bloomFilter) {
        this.bloomFilter = bloomFilter;
    }
    private int count;
    private BloomFilter<byte[]> bloomFilter;
}
