package com.fmzh.consumer;

import com.fmzh.common.BasePoint;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import java.io.IOException;

/**
 * Created by letrain on 2017/12/28.
 */
public class RabbitMqConsumer extends BasePoint implements Consumer {

    public RabbitMqConsumer(String basePointName) throws IOException {
        super(basePointName);
        channel.basicConsume(basePointName, true, this);
    }

    // Called when consumer is registered.
    public void handleConsumeOk(String s) {
        System.out.println("consumer " + s + " registered");
    }

    public void handleCancelOk(String s) {

    }

    public void handleCancel(String s) throws IOException {

    }

    public void handleShutdownSignal(String s, ShutdownSignalException e) {

    }

    public void handleRecoverOk(String s) {

    }

    //Called when new message is available.
    public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
        String message =  new String(bytes, "UTF-8");
        System.out.println("Message Number " + message + " reviced");
    }


}
