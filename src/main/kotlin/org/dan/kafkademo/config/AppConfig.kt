package org.dan.kafkademo.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions
import java.util.*

@Configuration
class AppConfig {

    @Bean
    @ConfigurationProperties(prefix = "kafka.producer")
    fun kafkaProducerProperties() = KafkaProducerProperties()

    @Bean
    @ConfigurationProperties(prefix = "kafka.consumer")
    fun kafkaConsumerProperties() = KafkaConsumerProperties()

    @Bean
    fun kafkaSender(
        producerProperties: KafkaProducerProperties
    ): KafkaSender<String, String> {
        val props = Properties()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = producerProperties.bootstrapServers
        props[ProducerConfig.CLIENT_ID_CONFIG] = producerProperties.clientId
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = producerProperties.keySerializer
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = producerProperties.valueSerializer
        return KafkaSender.create(SenderOptions.create(props))
    }

    @Bean
    fun kafkaReceiver(
        consumerProperties: KafkaConsumerProperties
    ): KafkaReceiver<String, String> {
        val props = Properties()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = consumerProperties.bootstrapServers
        props[ConsumerConfig.CLIENT_ID_CONFIG] = consumerProperties.clientId
        props[ConsumerConfig.GROUP_ID_CONFIG] = consumerProperties.groupId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = consumerProperties.keyDeserializer
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = consumerProperties.valueDeserializer

        val options = ReceiverOptions
            .create<String, String>(props)
            .subscription(listOf(consumerProperties.topic))

        return KafkaReceiver.create(options)
    }

}