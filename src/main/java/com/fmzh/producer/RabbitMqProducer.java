package com.fmzh.producer;

import com.fmzh.common.BasePoint;

import java.io.IOException;

/**
 * Created by letrain on 2017/12/27.
 */
public class RabbitMqProducer extends BasePoint {

    public RabbitMqProducer(String basePointName) {
        super(basePointName);
    }

    public void sendMessage(String message) throws IOException {
        channel.basicPublish("",basePointName,null,message.getBytes() );
    }

}
