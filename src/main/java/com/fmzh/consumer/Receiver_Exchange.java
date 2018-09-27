package com.fmzh.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Receiver_Exchange {
    /**
     * 交换器名称
     */
//    private static final String EXCHANGE_NAME = "logs";
    private static final String EXCHANGE_NAME = "gnw_rfid_mq_exchange";
    /**
     * 异步接收
     * @throws Exception
     */
    public static void asyncRec() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("127.0.0.1");
        factory.setHost("172.168.50.84");
        factory.setPassword("rfid");
        factory.setUsername("rfid");
        factory.setPort(5672);
        factory.setVirtualHost("rfid");
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        //消费者也需要定义队列 有可能消费者先于生产者启动
        //定义创建一个交换器 参数1 名称  参数2 交换器类型 参数3表示将交换器信息永久保存在服务器磁盘上 关闭rabbitmqserver也不会丢失
//        channel.exchangeDeclare(EXCHANGE_NAME, "fanout",true);
        channel.exchangeDeclare(EXCHANGE_NAME, "topic",true);
        channel.basicQos(1);
        //产生一个随机的队列 该队列用于从交换器获取消息
        String queueName = channel.queueDeclare().getQueue();
        //将队列和某个交换机丙丁 就可以正式获取消息了 routingkey和交换器的一样都设置成空
//        channel.queueBind(queueName, EXCHANGE_NAME, "");
        channel.queueBind(queueName, EXCHANGE_NAME, "gnw_rfid_location_queue");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        //定义回调抓取消息
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                //参数2 true表示确认该队列所有消息  false只确认当前消息 每个消息都有一个消息标记
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        //参数2 表示手动确认
        channel.basicConsume(queueName, false, consumer);

    }



    public static void main(String[] args) throws Exception {
        asyncRec();
    }

}
