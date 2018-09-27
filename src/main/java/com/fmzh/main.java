package com.fmzh;

import com.fmzh.consumer.RabbitMqConsumer;
import com.fmzh.producer.RabbitMqProducer;

import java.io.IOException;


public class main {
    public static void main(String[] args) throws IOException {
        // 生产者
        RabbitMqProducer producer = new RabbitMqProducer("test2");
        // 发送消息
        producer.sendMessage("hello");
        // 消费者
        RabbitMqConsumer consumer = new RabbitMqConsumer("test2");
//        consumer.handleDelivery("f",null,null,null);
        System.out.println("fafasf");

    }
}
