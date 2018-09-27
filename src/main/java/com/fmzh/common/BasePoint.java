package com.fmzh.common;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Created by letrain on 2017/12/27.
 */

public class BasePoint {
    @Value("${rabbitmq.host}")
    private String host;
    @Value("${rabbitmq.port}")
    private int port;
    @Value("${rabbitmq.username}")
    private String username;
    @Value("${rabbitmq.password}")
    private String password;


    protected Channel channel;
    protected Connection connection;
    protected String basePointName;

    public BasePoint(String basePointName) {
        this.basePointName = basePointName;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost("rfid");

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(basePointName,false,false,false,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
