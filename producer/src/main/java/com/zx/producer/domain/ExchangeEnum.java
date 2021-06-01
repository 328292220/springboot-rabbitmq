package com.zx.producer.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * ExchangeEnum
 * 说明：RabbitMQ交换器枚举
 *
 * 作用：声明了项目需要使用到的交换机
 *
 * 操作方式： 按需增删
 *
 * 备注：由于fanout类型的交换机会忽略路由键直接下发所有的路由器，故每个不同的广播都应新建一个交换机
 */
@Getter
public enum ExchangeEnum {
    /**
     * 消息通知队列
     */
    fanout("exchange_fanout",ExchangeTypeEnum.fanout,true,"广播交换器"),
    topic("exchange_topic",ExchangeTypeEnum.topic,true,"通配交换器"),
    direct("exchange_direct",ExchangeTypeEnum.direct,true,"直连交换器");
    String exchangeName;
    ExchangeTypeEnum type;
    boolean durable;
    String desc;
    ExchangeEnum(String exchangeName,ExchangeTypeEnum type,boolean durable, String desc) {
        this.exchangeName = exchangeName;
        this.type = type;
        this.durable = durable;//消息持久化
        this.desc = desc;
    }

    public static List<ExchangeEnum> toList() {
        return Arrays.asList(ExchangeEnum.values());
    }
}
