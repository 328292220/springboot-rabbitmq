package com.zx;

import com.zx.mq.Sender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class RabbitmqTest {
    @Resource
    private Sender sender;
    @Test
    public void testRabbitmq(){
        sender.send();
    }
}
