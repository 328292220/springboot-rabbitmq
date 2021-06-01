package com.zx.producer.domain;

import java.util.HashMap;
import java.util.Map;

public class QueueArguments {
    public static Map<String,Object> public_arguments;
    static {
        Map<String, Object> arguments = new HashMap<>();
        //消息条数限制,该参数是非负整数值。限制加入queue中消息的条数。先进先出原则，超过10条后面的消息会顶替前面的消息。
        arguments.put("x-max-length", 10);
        //消息容量限制,该参数是非负整数值。该参数和x-max-length目的一样限制队列的容量，但是这个是靠队列大小（bytes）来达到限制。
        arguments.put("x-max-length-bytes", 1024);

        /**
         * 消息存活时间,该参数是非负整数值.创建queue时设置该参数可指定消息在该queue中待多久，
         * 可根据x-dead-letter-routing-key和x-dead-letter-exchange生成可延迟的死信队列。
         */
        arguments.put("x-message-ttl", 10000);

        /**
         * 消息优先级,创建queue时arguments可以使用x-max-priority参数声明优先级队列 。该参数应该是一个整数，表示队列应该支持的最大优先级。
         * ​​建议使用1到10之间。目前使用更多的优先级将消耗更多的资源（Erlang进程）。
         * 设置该参数同时设置死信队列时或造成已过期的低优先级消息会在未过期的高优先级消息后面执行。
         * 该参数会造成额外的CPU消耗。
         */
        arguments.put("x-max-priority", 5);
        /**
         * 存活时间,创建queue时参数arguments设置了x-expires参数，该queue会在x-expires到期后queue消息，
         * 亲身测试直接消失（哪怕里面有未消费的消息）。
         */
        arguments.put("x-expires", 60000);
        /**
         * 创建queue时参数arguments设置了x-dead-letter-routing-key和x-dead-letter-exchange，
         * 会在x-message-ttl时间到期后把消息放到x-dead-letter-routing-key和x-dead-letter-exchange指定的队列中达到延迟队列的目的。
         */
        arguments.put("x-dead-letter-exchange", "TopExchangeName");
        arguments.put("x-dead-letter-routing-key", "ttl.*.value");//这里的routing-key也可以是队列名称，当消息过期后会转发到这个exchange对应的routing-key，达到延时队列效果
        public_arguments = arguments;
    }
}
