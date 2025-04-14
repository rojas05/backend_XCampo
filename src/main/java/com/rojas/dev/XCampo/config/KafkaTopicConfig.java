package com.rojas.dev.XCampo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic chatTopic() {
        return new NewTopic("chat-messages", 3, (short)1);
    }

    @Bean
    public NewTopic chatTopicD() {
        return new NewTopic("delivery-notifications", 3, (short)1);
    }

    @Bean
    public NewTopic chatTopicS() {
        return new NewTopic("seller-notifications", 3, (short)1);
    }

    @Bean
    public NewTopic chatTopicP() {
        return new NewTopic("product-notifications", 3, (short)1);
    }

}
