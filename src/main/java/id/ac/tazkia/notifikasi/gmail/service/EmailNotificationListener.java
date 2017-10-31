package id.ac.tazkia.notifikasi.gmail.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.tazkia.notifikasi.gmail.dto.EmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationListener.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${kafka.topic.email}", groupId = "${spring.kafka.consumer.group-id}")
    public void kafkaToGmail(String message){
        try {
            EmailRequest emailRequest = objectMapper.readValue(message, EmailRequest.class);
            logger.debug("====== Email Request ======");
            logger.debug("To : {}", emailRequest.getTo());
            logger.debug("Subject : {}", emailRequest.getSubject());
            logger.debug("Body : {}", emailRequest.getBody());
            logger.debug("====== Email Request ======");
        } catch (Exception err){
            logger.error(err.getMessage(), err);
        }
    }
}
