package org.dan.kafkademo.controller

import org.dan.kafkademo.dto.KafkaPayload
import org.dan.kafkademo.service.KafkaProducer
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class KafkaController(
    private val kafkaProducer: KafkaProducer
) {

    @PostMapping("send")
    fun send(
        @RequestBody payload: KafkaPayload
    ): Mono<Unit> =
        validateRequest(payload)
            .flatMap { kafkaProducer.send(it.message!!) }

    private fun validateRequest(payload: KafkaPayload): Mono<KafkaPayload> =
        Mono.fromCallable {
            payload.message ?: throw RuntimeException("payload.message is null")
            payload
        }

}