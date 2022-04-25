package org.dan.kafkademo.service

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.assertj.core.api.Assertions.assertThat
import org.dan.kafkademo.KafkademoApplication
import org.dan.kafkademo.config.KafkaConsumerProperties
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [KafkademoApplication::class])
@EnableKafka
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"])
@ActiveProfiles("test")
@DirtiesContext
@ExtendWith(OutputCaptureExtension::class)
class KafkaConsumerTest {

    @Autowired
    private lateinit var consumerProperties: KafkaConsumerProperties

    @Test
    fun testConsumeTopic() {
        assertThat(consumerProperties.topic).isEqualTo("kafka-demo")
    }

    @Test
    fun testConsumeMessage(logOutput: CapturedOutput) {
        val message = "Hello, world!"
        sendMessage(message)
        Thread.sleep(1000L)
        assertThat(logOutput.out).contains("consumer: read message '$message'")
    }

    private fun sendMessage(message: String) {
        val kafkaTemplate = createKafkaTemplate()
        kafkaTemplate.send(consumerProperties.topic!!, message)
    }

    private fun createKafkaTemplate(): KafkaTemplate<String, String> {
        val props = HashMap<String, Any>()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "PLAINTEXT://localhost:9092"
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        val factory = DefaultKafkaProducerFactory<String, String>(props)
        return KafkaTemplate(factory)
    }
}