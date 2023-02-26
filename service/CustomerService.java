package com.redis.bloom.service;

import com.redis.bloom.entities.Customer;
import com.redis.bloom.filter.Bloomfilter;
import com.redis.bloom.mapper.CustomerMapper;
import com.redis.bloom.util.CheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wazh
 * description: service层，写有添加，查询，经过bloomfilter查询。
 * @since 2023-02-25-20:31
 */
@Slf4j
@Service
public class CustomerService {

    public static final String CACHA_KEY_CUSTOMER = "customer:";
    public static final String WHITE_NAME_LIST = "whitenamelist";
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private Bloomfilter bloomfilter;

    public int addCustomer(Customer customer) {
        int i = customerMapper.insert(customer);
        if (i > 0) {
            //写进redis缓存
            Customer value = customer;
            String key = CACHA_KEY_CUSTOMER + customer.getId();
            redisTemplate.opsForValue().set(key, value);
            log.info("{},该编号用户已被写入redis缓存", key);
            bloomfilter.init(customer.getId());//添加key到bitmap中
        }
        return i;
    }

    public Customer selectCustomerById(int id) {
        //首先查看redis
        String key = CACHA_KEY_CUSTOMER + id;
        Customer customer = (Customer) redisTemplate.opsForValue().get(key);

        if (customer == null) {
            customer = customerMapper.selectByPrimaryKey(id);

            if (customer != null) {
                //写进redis缓存
                redisTemplate.opsForValue().set(key, customer);
            }
        }
        return customer;
    }

    public Customer selectCustomerByIdAndBloomfilter(int customerId) {
        Customer customer = null;
        String key = CACHA_KEY_CUSTOMER + customerId;
        boolean exist = CheckUtil.checkKeyExistInBloom(key, WHITE_NAME_LIST);
        // bloomfilter 说存在，不一定存在，说不存在，则一定不存在
        if (exist) {

            customer = (Customer) redisTemplate.opsForValue().get(key);
            if (customer == null) {
                customer = customerMapper.selectByPrimaryKey(customerId);
                redisTemplate.opsForValue().set(key, customer);
            }

        }
        return customer;
    }

}
