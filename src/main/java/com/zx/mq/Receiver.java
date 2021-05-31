package com.zx.mq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "enjoy")
public class Receiver {
    @RabbitHandler
    public void process(String msg){
        System.out.println("receive msg:"+msg);
    }
}
