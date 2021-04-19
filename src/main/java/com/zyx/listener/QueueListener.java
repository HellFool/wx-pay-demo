package com.zyx.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class QueueListener {

    @JmsListener(destination = "publish.queue", containerFactory = "jmsListenerContainerQueue")
//    SendTo 会将此方法返回的数据, 写入到 queue : out.queue 中去
//    @SendTo("out.queue")
    public void receive(String text){
        System.out.println("QueueListener: consumer-a 11收到一条信息: " + text);
//        return "consumer-a received : " + text;
    }
}
