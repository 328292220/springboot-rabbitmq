package com.zx.producer.config;

import com.zx.producer.domain.ExchangeEnum;
import com.zx.producer.domain.QueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Map;

@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.host}")
    private String addresses;

    @Value("${spring.rabbitmq.port}")
    private String port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Value("${spring.rabbitmq.publisher-confirm-type}")
    private CachingConnectionFactory.ConfirmType confirmType;

    @Bean("queuesNames")
    public Map<String, String> queuesNames() {
        return QueueEnum.getQueuesNames();
    }
    /**
     * @return java.lang.Object
     * @program com.gzqapi.rabbitmq.config.RabbitMQConfig.createExchange();
     * @description 动态创建交换机
     * @param: this is no-parameter method.
     * @author zhiqiang94@vip.qq.com
     * @create 2020/4/16 0016 9:28
     */
    @Bean("createExchange")
    public Object createExchange() {
        // 遍历交换机枚举
        ExchangeEnum.toList().forEach(exchangeEnum -> {
                    // 声明交换机
                    Exchange exchange;
                    // 根据交换机模式 生成不同的交换机
                    switch (exchangeEnum.getType()) {
                        case fanout:
                            exchange = ExchangeBuilder.fanoutExchange(exchangeEnum.getExchangeName()).durable(exchangeEnum.isDurable()).build();
                            break;
                        case direct:
                            exchange = ExchangeBuilder.directExchange(exchangeEnum.getExchangeName()).durable(exchangeEnum.isDurable()).build();
                            break;
                        case topic:
                        default:
                            exchange = ExchangeBuilder.topicExchange(exchangeEnum.getExchangeName()).durable(exchangeEnum.isDurable()).build();
                            break;
                    }
                    // 将交换机注册到spring bean工厂 让spring实现交换机的管理
                    if (exchange != null) {
                        SpringContextUtil.registerBean(exchangeEnum.toString() + "_exchange", exchange);
                    }
                }
        );
        // 不返回任何对象 该方法只用于在spring初始化时 动态的将bean对象注册到spring bean工厂
        return null;
    }
    /**
     * @return java.lang.Object
     * @program com.gzqapi.rabbitmq.config.RabbitMQConfig.createQueue();
     * @description 动态创建队列
     * @param: this is no-parameter method.
     * @author zhiqiang94@vip.qq.com
     * @create 2020/4/16 0016 9:29
     */
    @Bean("createQueue")
    public Object createQueue() {
//        durable: false, //队列是否持久化.false:队列在内存中,服务器挂掉后,队列就没了;true:服务器重启后,队列将会重新生成.注意:只是队列持久化,不代表队列中的消息持久化!!!!
//        exclusive: false, //队列是否专属,专属的范围针对的是连接,也就是说,一个连接下面的多个信道是可见的.对于其他连接是不可见的.连接断开后,该队列会被删除.注意,不是信道断开,是连接断开.并且,就算设置成了持久化,也会删除.
//        autoDelete: true, //如果所有消费者都断开连接了,是否自动删除.如果还没有消费者从该队列获取过消息或者监听该队列,那么该队列不会删除.只有在有消费者从该队列获取过消息后,该队列才有可能自动删除(当所有消费者都断开连接,不管消息是否获取完)
//        arguments: null //队列的配置
        // 遍历队列枚举 将队列注册到spring bean工厂 让spring实现队列的管理
        QueueEnum.toList().forEach(queueEnum -> SpringContextUtil.registerBean(queueEnum.toString() + "_queue", new Queue(queueEnum.getName(), queueEnum.isDurable(), queueEnum.isExclusive(), queueEnum.isAutoDelete(), queueEnum.getArguments())));
        // 不返回任何对象 该方法只用于在spring初始化时 动态的将bean对象注册到spring bean工厂
        return null;
    }
    /**
     * @return java.lang.Object
     * @program com.gzqapi.rabbitmq.config.RabbitMQConfig.createBinding();
     * @description 动态将交换机及队列绑定
     * @param: this is no-parameter method.
     * @author zhiqiang94@vip.qq.com
     * @create 2020/4/16 0016 9:29
     */
    @Bean("createBinding")
    public Object createBinding() {
        // 遍历队列枚举 将队列绑定到指定交换机
        QueueEnum.toList().forEach(queueEnum -> {
                    // 从spring bean工厂中获取队列对象（刚才注册的）
                    Queue queue = SpringContextUtil.getBean(queueEnum.toString() + "_queue", Queue.class);
                    // 声明绑定关系
                    Binding binding;
                    // 根据不同的交换机模式 获取不同的交换机对象（注意：刚才注册时使用的是父类Exchange，这里获取的时候将类型获取成相应的子类）生成不同的绑定规则
                    switch (queueEnum.getExchangeEnum().getType()) {
                        case fanout:
                            FanoutExchange fanoutExchange = SpringContextUtil.getBean(queueEnum.getExchangeEnum().toString() + "_exchange", FanoutExchange.class);
                            binding = BindingBuilder.bind(queue).to(fanoutExchange);
                            break;
                        case topic:
                            TopicExchange topicExchange = SpringContextUtil.getBean(queueEnum.getExchangeEnum().toString() + "_exchange", TopicExchange.class);
                            binding = BindingBuilder.bind(queue).to(topicExchange).with(queueEnum.getRoutingKey());
                            break;
                        case direct:
                        default:
                            DirectExchange directExchange = SpringContextUtil.getBean(queueEnum.getExchangeEnum().toString() + "_exchange", DirectExchange.class);
                            binding = BindingBuilder.bind(queue).to(directExchange).with(queueEnum.getRoutingKey());
                            break;
                    }
                    // 将绑定关系注册到spring bean工厂 让spring实现绑定关系的管理
                    if (binding != null) {
                        SpringContextUtil.registerBean(queueEnum.toString() + "_binding", binding);
                    }
                }
        );
        // 不返回任何对象 该方法只用于在spring初始化时 动态的将bean对象注册到spring bean工厂
        return null;
    }


    @Bean
    public ConnectionFactory connectionFactory() {

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(addresses+":"+port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        /** 如果要进行消息回调，则这里必须要设置为true */
        connectionFactory.setPublisherConfirmType(confirmType);
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate newRabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMandatory(true);
        template.setConfirmCallback(confirmCallback());
        template.setReturnCallback(returnCallback());
        return template;
    }

    //===============消费者确认==========
//    @Bean
//    public SimpleMessageListenerContainer messageContainer() {
//        SimpleMessageListenerContainer container
//                = new SimpleMessageListenerContainer(connectionFactory());
//        container.setQueues(userQueue());
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        container.setMessageListener(userReceiver);
//        return container;
//    }

    //===============生产者发送确认==========
    //===============生产者发送确认==========
    @Bean
    public RabbitTemplate.ConfirmCallback confirmCallback(){
        return new RabbitTemplate.ConfirmCallback(){
            @Override
            public void confirm(CorrelationData correlationData,
                                boolean ack, String cause) {
                if (ack) {
                    System.out.println("发送者确认发送给mq成功");
                } else {
                    //处理失败的消息
                    System.out.println("发送者发送给mq失败,考虑重发:"+cause);
                }
            }
        };
    }

    @Bean
    public RabbitTemplate.ReturnCallback returnCallback(){
        return new RabbitTemplate.ReturnCallback(){

            @Override
            public void returnedMessage(Message message,
                                        int replyCode,
                                        String replyText,
                                        String exchange,
                                        String routingKey) {
                System.out.println("无法路由的消息，需要考虑另外处理。");
                System.out.println("Returned replyText："+replyText);
                System.out.println("Returned exchange："+exchange);
                System.out.println("Returned routingKey："+routingKey);
                String msgJson  = new String(message.getBody());
                System.out.println("Returned Message："+msgJson);
            }
        };
    }
}
