package org.dan.kafkademo.service

import org.assertj.core.api.Assertions.assertThat
import org.dan.kafkademo.KafkademoApplication
import org.dan.kafkademo.config.KafkaConsumerProperties
import org.dan.kafkademo.config.KafkaProducerProperties
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [KafkademoApplication::class])
@EnableKafka
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"])
@ActiveProfiles("test")
@DirtiesContext
@ExtendWith(OutputCaptureExtension::class)
class KafkaProducerTest {

    @Autowired
    private lateinit var producer: KafkaProducer

    @Autowired
    private lateinit var producerProperties: KafkaProducerProperties

    @Autowired
    private lateinit var consumerProperites: KafkaConsumerProperties

    @Test
    fun testProducerAndConsumerTopics() {
        assertThat(producerProperties.topic).isEqualTo("kafka-demo")
        assertThat(consumerProperites.topic).isEqualTo(producerProperties.topic)
    }

    @Test
    fun testProduceMessage(logOutput: CapturedOutput) {
        val message = "Hello, world!"
        producer.send(message).block()
        assertThat(logOutput.out).contains("producer: send message '$message'")
        Thread.sleep(1000L)
        assertThat(logOutput.out).contains("consumer: read message '$message'")
    }
}