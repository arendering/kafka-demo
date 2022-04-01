package org.dan.kafkademo.service

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.dan.kafkademo.config.KafkaProducerProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord
import java.util.*

@Service
class KafkaProducer(
    private val kafkaSender: KafkaSender<String, String>,
    private val properties: KafkaProducerProperties
) {
    companion object {
        private val log = LoggerFactory.getLogger(KafkaProducer::class.java)
    }

    fun send(message: String): Mono<Unit> =
        kafkaSender
            .send(createRecord(message))
            .doOnNext { log.info("producer: send message '${message}'") }
            .doOnError { log.error("producer: send failed", it) }
            .then(Mono.empty())

    private fun createRecord(message: String): Mono<SenderRecord<String, String, String>> {
        val key = UUID.randomUUID().toString()
        return Mono.fromCallable {
            SenderRecord.create(ProducerRecord(properties.topic, key, message), key)
        }
    }
}