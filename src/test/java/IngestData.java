import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class IngestData extends TestBase{

    private Logger log = LoggerFactory.getLogger(getClass());

    public void ingestData(String jsonString, String kafkaTopicName){
        log.info("kafkaTopicName={}", kafkaTopicName);
        Properties producerConfig = new Properties();
        producerConfig.put("bootstrap.servers", "localhost" + ":" + "9092");
        producerConfig.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerConfig.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> producer = new KafkaProducer(producerConfig);
        ProducerRecord<String, String> data = new ProducerRecord<>(kafkaTopicName, jsonString);
        try {
            producer.send(data).get();
        }catch (InterruptedException | ExecutionException exp) {
            log.error("error while posting message" + exp.getMessage());
        }
        producer.close();
    }



    public static void main(String[] args) throws IOException {
        IngestData id = new IngestData();
        id.ingestData(id.readPayloadFromFile("smoke/videogame-request.json"), "quickstart-events");
    }
}

