package com.fmzh.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Producer_Ticket {

    /**
     * 队列名称
     */
    private final static String QUEUE_NAME = "mywork";
    public static void main(String[] args) throws Exception {
        //连接远程rabbit-server服务器
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //定义创建一个队列
//        //修改定义队列代码（参数2 true表示持久化 false表示非持久化）
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //修改定义队列代码（参数2 true表示持久化 false表示非持久化）
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //合理消息转发
        channel.basicQos(1);
        String message = null;
        //同时发送5条消息
        for(int i=0;i<=5;i++){
            message="发送第"+i+"消息";
            //发送消息
//            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
            //修改消息发送代码（修改第三个参数 持久化文本）
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
        }

        System.out.println(" [x] Sent 5 message");
        channel.close();
        connection.close();
    }

}
