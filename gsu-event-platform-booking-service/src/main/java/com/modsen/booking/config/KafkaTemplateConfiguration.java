package com.modsen.booking.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.booking.dto.event.BookingEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Dudkin
 */
@EnableKafka
@Configuration
public class KafkaTemplateConfiguration {

    public static final String BOOKING_TOPIC = "booking-created";

    @Bean
    public ProducerFactory<String, BookingEvent> producerFactory() throws UnknownHostException {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, InetAddress.getLocalHost().getHostAddress());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, BookingEvent> kafkaTemplate(ProducerFactory<String, BookingEvent> producerFactory) {
        var kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(BOOKING_TOPIC);
        return kafkaTemplate;
    }

    @Bean
    public DefaultKafkaProducerFactoryCustomizer serializerCustomizer(ObjectMapper objectMapper) {
        return producerFactory -> producerFactory.setValueSerializer(new JsonSerializer<>(objectMapper));
    }

}
