package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.notification;

import br.com.fiap.techchallenge.hackathonvideo.application.producer.notification.PushNotificationProducer;
import br.com.fiap.techchallenge.hackathonvideo.application.producer.notification.dto.PushNotificationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PushNotificationProducerImpl implements PushNotificationProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationProducerImpl.class);

    @Value("${sqs.queue.notification.push.producer}")
    private String notificationPushQueue;

    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper;

    public PushNotificationProducerImpl(SqsTemplate sqsTemplate, ObjectMapper objectMapper) {
        this.sqsTemplate = sqsTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendToPushNotification(PushNotificationDTO dto) {
        try {
            sqsTemplate.send(notificationPushQueue, objectMapper.writeValueAsString(dto));

            LOGGER.info("Sent video to push notification with status {}", dto.status());
        } catch (JsonProcessingException e) {
            LOGGER.error("Error on push notification for video {}", dto.videoName());
        }
    }
}
