## Что это
- В rest-контроллер отправляем json с сообщением, которое будет опубликовано в kafka KafkaProducer'ом. А после публикации прочитано KafkaConsumer'ом

## Зависимости
- Java 11
- Apache Kafka 3.1.0

## Запуск приложения
### Запуск kafka
- $ bin/zookeeper-server-start.sh config/zookeeper.properties
- $ bin/kafka-server-start.sh config/server.properties

### Создание топика
- $ bin/kafka-topics.sh --create --topic kafka-demo --bootstrap-server localhost:9092

### Запуск spring-приложения
- ./gradlew bootRun

### Отправка сообщения в контроллер
- ./send_message.sh "some message"