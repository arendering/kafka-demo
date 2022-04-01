package org.dan.kafkademo.config

data class KafkaProducerProperties(
    var topic: String? = null,
    var bootstrapServers: String? = null,
    var clientId: String? = null,
    var keySerializer: String? = null,
    var valueSerializer: String? = null
)

data class KafkaConsumerProperties(
    var topic: String? = null,
    var bootstrapServers: String? = null,
    var clientId: String? = null,
    var groupId: String? = null,
    var keyDeserializer: String? = null,
    var valueDeserializer: String? = null
)
