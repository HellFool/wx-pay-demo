package com.zyx.controller;



import com.zyx.util.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.*;


@RestController
@RequestMapping(value = "/api/activemq")
public class ActiveMqController {

    @GetMapping(value="/send")
    @ApiOperation(value = "发送消息")
    public Result sendMessage(@RequestParam(value="message",required = false)String message)throws Exception {
        //1、创建连接工厂对象ConnectionFactory对象
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.1.91:61616");
        Connection connection = connectionFactory.createConnection();
        //开启连接，调用Connection对象start方法
        connection.start();
        boolean transacted=false;//不开启事务
        int acknowledgeMode= Session.AUTO_ACKNOWLEDGE;
        Session session = connection.createSession(transacted, acknowledgeMode);

        String queueName="textMessage";//当前消息队列的名称
        Queue queue = session.createQueue(queueName);
        //使用Session对象创建一个Producer对象
        MessageProducer producer = session.createProducer(queue);
        //创建一个TextMessage对象
        TextMessage textMessage = session.createTextMessage(message);
        //发送消息
        producer.send(textMessage);
        return Result.OK;
    }

    @GetMapping(value="/receive")
    @ApiOperation(value = "接收消息")
    public Result receiveMessage()throws Exception {
        //1、创建连接工厂对象ConnectionFactory对象
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.1.91:61616");
        Connection connection = connectionFactory.createConnection();
        //开启连接，调用Connection对象start方法
        connection.start();
        // 第一个参数，是否使用事务，如果设置true，操作消息队列后，必须使用 session.commit();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        String queueName="textMessage";//当前消息队列的名称
        Queue queue = session.createQueue(queueName);

        //接收消息
        MessageConsumer consumer = session.createConsumer(queue);
        MessageListener listener=new MessageListener(){
            @Override
            public void onMessage(Message message){
                //接收事件。当消息到达就可以在这里接收到消息
                //取出消息内容
                if(message instanceof TextMessage){
                    TextMessage textMessage=(TextMessage) message;
                    //打印消息内容
                    try{
                        String text = textMessage.getText();
                        System.out.println(text);
                    }catch (JMSException e){
                        e.printStackTrace();
                    }
                }

            }
        };
        consumer.setMessageListener(listener);
        return Result.OK;
    }
}
