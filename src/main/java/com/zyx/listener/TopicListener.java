package com.zyx.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TopicListener {

    //监听订单成功消息做优惠券处理
    @JmsListener(destination = "publish.topic", containerFactory = "jmsListenerContainerTopic")
    public void receive(String text){
        System.out.println("TopicListener: consumer-a 11收到一条信息: " + text);
    }

    //监听订单成功消息做积分处理
    @JmsListener(destination = "publish.topic", containerFactory = "jmsListenerContainerTopic")
    public void receive2(String text){
        System.out.println("TopicListener: consumer-a 22收到一条信息: " + text);
    }

    //监听订单成功消息做短信发送处理
    @JmsListener(destination = "publish.topic", containerFactory = "jmsListenerContainerTopic")
    public void receive3(String text){
        System.out.println("TopicListener: consumer-a 33收到一条信息: " + text);
    }
}