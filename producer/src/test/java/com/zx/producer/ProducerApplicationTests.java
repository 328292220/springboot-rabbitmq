package com.zx.producer;

import com.zx.producer.domain.ExchangeEnum;
import com.zx.producer.domain.QueueArguments;
import com.zx.producer.domain.QueueEnum;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProducerApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("===========");
        System.out.println(QueueEnum.getQueuesNames());
        System.out.println("===========");
        System.out.println(QueueEnum.toList());
        System.out.println(ExchangeEnum.toList());
        System.out.println(QueueArguments.public_arguments);
        String name = QueueEnum.valueOf("fanout_rpts").getName();
        System.out.println(name);
    }

}
