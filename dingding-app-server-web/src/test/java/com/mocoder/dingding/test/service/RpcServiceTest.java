package com.mocoder.dingding.test.service;

import com.mocoder.dingding.rpc.SmsServiceWrap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: likun7
 * Date: 14-11-19
 * Time: 上午11:52
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-config.xml"})
public class RpcServiceTest {
    @Resource
    private SmsServiceWrap smsServiceWrap;

    @Test
    public void sentSms(){
        smsServiceWrap.sentLoginValidCodeSms("15652301160","1234");
    }

}