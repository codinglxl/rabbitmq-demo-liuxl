package com.fmzh.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Producer_Route {
    /**
     * 交换器名称
     */
    private static final String EXCHANGE_NAME = "X";
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //连接远程rabbit-server服务器
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //定义创建一个交换器 参数1 名称  参数2 交换器类型 参数3表示将交换器信息永久保存在服务器磁盘上 关闭rabbitmqserver也不会丢失
        channel.exchangeDeclare(EXCHANGE_NAME, "direct",true);
//        String message = args[1];
        //同时发送5条消息
        //第二个参数就是routingkey
        channel.basicPublish(EXCHANGE_NAME, "error", MessageProperties.PERSISTENT_TEXT_PLAIN, "这是错误信息".getBytes("UTF-8"));
        channel.basicPublish(EXCHANGE_NAME, "info", MessageProperties.PERSISTENT_TEXT_PLAIN, "这是程序运行信息".getBytes("UTF-8"));
        channel.basicPublish(EXCHANGE_NAME, "warning", MessageProperties.PERSISTENT_TEXT_PLAIN, "这是警告".getBytes("UTF-8"));

        System.out.println(" [x] Sent 3 message");
        channel.close();
        connection.close();
    }



}
