package com.fmzh.consumer;

import com.rabbitmq.client.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Receiver_Route {
    /**
     * 交换器名称
     */
    private static final String EXCHANGE_NAME = "X";
    /**
     * 用一份代码模拟两个程序
     * 参数1：表示订阅者程序类型 有两个值 C1表示需要写入文件系统 C2 表示打印
     * 参数n：订阅者接受消息的routingkey 可以接受多个
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        //消费者也需要定义队列 有可能消费者先于生产者启动
        channel.exchangeDeclare(EXCHANGE_NAME, "direct",true);
        channel.basicQos(1);
        //产生一个随机的队列 该队列用于从交换器获取消息
        String queueName = channel.queueDeclare().getQueue();
        //参数设置了多个routingkey 就可以绑定到多个 如果在当前交换机这些routingkey上发消息 都可以接受
        for(int i=0;i<args.length;i++){
            channel.queueBind(queueName, EXCHANGE_NAME, args[i]);
        }
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        //定义回调抓取消息
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                String routingKey=envelope.getRoutingKey();
                //如果是错误消息 将他写入到文件系统中
                if("error".equals(routingKey) && "C1".equals(args[0])){
                    FileUtils.writeStringToFile(new File("F:/myerror.log"), message,"UTF-8");
                }
                System.out.println(routingKey+":"+message);
                //参数2 true表示确认该队列所有消息  false只确认当前消息 每个消息都有一个消息标记
                channel.basicAck(envelope.getDeliveryTag(), false);

            }
        };
        //参数2 表示手动确认
        channel.basicConsume(queueName, false, consumer);
    }
}
