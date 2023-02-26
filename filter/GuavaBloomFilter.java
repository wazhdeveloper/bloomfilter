package com.redis.bloom.filter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GuavaBloomFilter {

    private static final Integer _W = 10000;
    private static final Integer SIZE = 100 * _W;
    private static double fpp = 0.03;
    @Resource
    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), SIZE, fpp);

    public void guavaBloomFilter() {
        for (int i = 1; i < SIZE; i++) {
            bloomFilter.put(i);
        }
        List<Integer> list = new ArrayList<>(10 * _W);
        for (int i = SIZE + 1; i < SIZE + (10 * _W); i++) {
            if (bloomFilter.mightContain(i)) {
                list.add(i);
                log.info("数字{}被误判，这是第{}个被误判的数字", i, list.size());
            }
        }
        log.info("流程结束，总共{}个数据被误判！", list.size());
    }

}
