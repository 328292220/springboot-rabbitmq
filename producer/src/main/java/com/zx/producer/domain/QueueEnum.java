package com.zx.producer.domain;


import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum QueueEnum {
    /**
     * 消息通知队列
     */
    fanout_rpts(ExchangeEnum.fanout,"queue_fanout.rpts",false,false,true,QueueArguments.public_arguments,"rpts.*","广播队列"),
    topic_rpts(ExchangeEnum.topic,"queue_topic.rpts",false,false,true,QueueArguments.public_arguments,"topic.rpts.*","通配符队列"),
    direct_rpts(ExchangeEnum.direct,"queue_direct.rpts",false,false,true,QueueArguments.public_arguments,"direct.rpts","直连队列");
    private ExchangeEnum exchangeEnum;
    //队列名称
    private String name;
    //队列是否持久化.false:队列在内存中,服务器挂掉后,队列就没了;true:服务器重启后,队列将会重新生成.注意:只是队列持久化,不代表队列中的消息持久化!!!!
    private boolean durable;
    //队列是否专属,专属的范围针对的是连接,也就是说,一个连接下面的多个信道是可见的.对于其他连接是不可见的.连接断开后,该队列会被删除.注意,不是信道断开,是连接断开.并且,就算设置成了持久化,也会删除.
    private boolean exclusive;
    //如果所有消费者都断开连接了,是否自动删除.如果还没有消费者从该队列获取过消息或者监听该队列,那么该队列不会删除.只有在有消费者从该队列获取过消息后,该队列才有可能自动删除(当所有消费者都断开连接,不管消息是否获取完)
    private boolean autoDelete;
    //队列的配置
    private Map<String, Object> arguments;
    //路由键
    private String routingKey;
    //描述
    private String describe;

    QueueEnum(ExchangeEnum exchangeEnum,String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments,String routingKey, String describe) {
        this.exchangeEnum = exchangeEnum;
        this.name = name;
        this.durable = durable;
        this.exclusive = exclusive;
        this.autoDelete = autoDelete;
        this.arguments = arguments;
        this.routingKey = routingKey;
        this.describe = describe;
    }
    public static Map<String, String> getQueuesNames() {
        return Arrays.asList(QueueEnum.values()).stream().collect(Collectors.toMap(queueEnum -> queueEnum.toString(), queueEnum -> queueEnum.getName()));
    }

    public static List<QueueEnum> toList() {
        return Arrays.asList(QueueEnum.values());
    }
}
