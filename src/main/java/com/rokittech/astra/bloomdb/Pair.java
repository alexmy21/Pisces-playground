/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rokittech.astra.bloomdb;

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
