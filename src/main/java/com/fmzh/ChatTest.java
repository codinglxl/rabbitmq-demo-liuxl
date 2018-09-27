package com.fmzh;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;

public class ChatTest {
    private final static String QUEUE_NAME = "queue_chat"; //
    private final static String EXCHANGE_NAME = "q_chat";
    public static void main(final String[] args) throws Exception{
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMQ地址
        factory.setHost("localhost");//连接地址
        factory.setUsername("guest");//用户名
        factory.setPassword("guest");//密码
        factory.setPort(5672);//端口号
        //创建一个新的连接
        final Connection connection = factory.newConnection();
        //发送消息线程
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                //创建一个频道
                Channel channel = null;
                try {
                    channel = connection.createChannel();
                    //声明要关注的频道
                    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
                    //channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                while(true) {
                    Scanner scan = new Scanner(System.in);
//                    System.out.println("请输入消息");
                    String message = args[0]+scan.nextLine();
                    //发送消息到队列中
                    try {
                        channel.basicPublish(EXCHANGE_NAME, QUEUE_NAME, null, message.getBytes());
                        //channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    System.out.println("B发送消息:" + message);
                }
            }
        });
        //接收消息线程
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                Channel channel = null;
                try {
                    channel = connection.createChannel();
                    //声明要关注的频道
                    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
                    String queueName = channel.queueDeclare().getQueue();
                    //channel.queueDeclare(QUEUE_NAME,false,false,false,null);
                    channel.queueBind(queueName, EXCHANGE_NAME, "chat");
                    //创建消费者 ---- 得到消息后会自动触发
                    Consumer consumer = new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(
                                String consumerTag, Envelope envelope,
                                AMQP.BasicProperties properties, byte[] body
                        ) throws IOException {
                            //body为消息体
                            String message = new String(body, "UTF-8");
                            System.out.println(message);
                        }
                    };
                    //消息消费完成确认
                    channel.basicConsume(queueName, true, consumer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
//        Thread.sleep(10000);
        t2.start();
    }
}

