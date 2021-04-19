package com.zyx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import javax.jms.Topic;

@RestController
@RequestMapping(value = "/api/activemq")
public class PublishController {
    @Autowired
    private JmsMessagingTemplate jms;

    @Autowired
    private Queue queue;

    @Autowired
    private Topic topic;

    @GetMapping("/queue")
    public String queue(@RequestParam(value = "id") String id){
        jms.convertAndSend(queue, id);
        return "queue 发送成功";
    }

//    @JmsListener(destination = "out.queue")
//    public void consumerMsg(String msg){
//        System.out.println(msg);
//    }

    @GetMapping("/topic")
    public String topic(@RequestParam(value = "id") String id){
        jms.convertAndSend(topic, id);
        return "topic 发送成功";
    }
}
