package com.zx.producer.service;

import com.zx.producer.domain.ExchangeEnum;

public interface QueueMessageService {
    void send(String msgId, String msg, ExchangeEnum fanout);
}
