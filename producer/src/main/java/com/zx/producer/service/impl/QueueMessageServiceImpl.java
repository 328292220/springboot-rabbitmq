package com.zx.producer.service.impl;

import com.zx.producer.domain.ExchangeEnum;
import com.zx.producer.domain.QueueEnum;
import com.zx.producer.service.QueueMessageService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class QueueMessageServiceImpl implements QueueMessageService {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Override
    public void send(String msgId, String msg, ExchangeEnum fanout) {
        //创建消费对象，并指定全局唯一ID(这里使用UUID，也可以根据业务规则生成，只要保证全局唯一即可)
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setMessageId(msgId);
        messageProperties.setContentType("text/plain");
        messageProperties.setContentEncoding("utf-8");
        Message message = new Message(msg.getBytes(), messageProperties);
        List<QueueEnum> collect = QueueEnum.toList().stream().
                filter(queueEnum -> queueEnum.getExchangeEnum().getExchangeName().equals(fanout.getExchangeName())).collect(Collectors.toList());
        rabbitTemplate.convertAndSend(fanout.getExchangeName(),collect.get(0).getRoutingKey(),message);
    }
}
