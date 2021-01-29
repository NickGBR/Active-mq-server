package com.ngbr.server.config;

import com.ngbr.server.consts.Queue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

@Configuration
public class PublisherConfig {
    @Bean
    public ActiveMQTopic simpleTopic() {
        return new ActiveMQTopic(Queue.RESPONSE_TOPIC);
    }

}
