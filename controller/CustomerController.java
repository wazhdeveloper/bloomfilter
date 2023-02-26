package com.redis.bloom.controller;

import com.redis.bloom.entities.Customer;
import com.redis.bloom.filter.Bloomfilter;
import com.redis.bloom.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;

/**
 * @author wazh
 * @description
 * @since 2023-02-26-9:03
 */
@RestController
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private Bloomfilter bloomfilter;

    @RequestMapping(value = "/addCustomer", method = RequestMethod.POST)
    public int addCustomer() {
        for (int i = 0; i < 2; i++) {
            Customer customer = new Customer();

            customer.setCname("customer" + i);
            customer.setAge(new Random().nextInt(30) + 1);
            customer.setPhone("1381111XXXX");
            customer.setSex((byte) new Random().nextInt(2));
            customer.setBirth(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

            customerService.addCustomer(customer);
        }
        return 0;
//        //查看是否存在该用户
//        Customer customer1 = customerService.selectCustomerByIdAndBloomfilter(customer.getId());
//        if (customer1 == null) {
//            //写进bloomfilter，mysql，redis
//            bloomfilter.init(customer.getId());
//            int i = customerService.addCustomer(customer);
//            return i;
//        }
//        log.info("很抱歉，编号：{} 用户已存在，请重新注册", customer.getId());
//        return 0;
    }

    @RequestMapping(value = "/selectById/{id}", method = RequestMethod.GET)
    public Customer selectCustomerById(@PathVariable int id) {
        return customerService.selectCustomerById(id);
    }

    @RequestMapping(value = "/selectBuIdWithBloomfilter/{id}", method = RequestMethod.GET)
    public Customer selectCustomerByIdWithBloomfilter(@PathVariable int id) {
        return customerService.selectCustomerByIdAndBloomfilter(id);
    }

}
