package com.zx.producer;

import com.zx.producer.domain.ExchangeEnum;
import com.zx.producer.service.QueueMessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMQTest {
    @Autowired
    QueueMessageService queueMessageService;
    @Test
    public void send(){
        String msgId = "NO.1";
        String msg = "NO.1消息";
        queueMessageService.send(msgId,msg, ExchangeEnum.fanout);
        msgId = "NO.2";
        msg = "NO.2消息";
        queueMessageService.send(msgId,msg, ExchangeEnum.topic);
        msgId = "NO.3";
        msg = "NO.3消息";
        queueMessageService.send(msgId,msg, ExchangeEnum.direct);

    }
}
