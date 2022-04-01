package org.dan.kafkademo.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.kafka.receiver.KafkaReceiver
import javax.annotation.PostConstruct

@Service
class KafkaConsumer(
    private val kafkaReceiver: KafkaReceiver<String, String>
) {

    companion object {
        private val log = LoggerFactory.getLogger(KafkaConsumer::class.java)
    }

    @PostConstruct
    fun start() {
        log.debug("consumer postConstruct started..")
        kafkaReceiver
            .receive()
            .subscribe {
                log.info("consumer: read message '${it.value()}'")
            }
    }
}