package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.notification;

import br.com.fiap.techchallenge.hackathonvideo.application.producer.notification.dto.PushNotificationDTO;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PushNotificationProducerImplTest {

    @Mock
    private SqsTemplate sqsTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PushNotificationProducerImpl pushNotificationProducer;

    private PushNotificationDTO pushNotificationDTO;

    @BeforeEach
    void setUp() {
        this.buildArranges();
        ReflectionTestUtils.setField(pushNotificationProducer, "notificationPushQueue", "sqs.queue.notification.push.producer");
    }

    @Test
    @DisplayName("Should Send To PushNotification Queue")
    void shouldSendToPushNotificationQueue() throws JsonProcessingException {
        pushNotificationProducer.sendToPushNotification(pushNotificationDTO);

        verify(sqsTemplate).send("sqs.queue.notification.push.producer", objectMapper.writeValueAsString(pushNotificationDTO));
        verify(objectMapper, times(2)).writeValueAsString(pushNotificationDTO);
    }

    @Test
    @DisplayName("Should Log Error When JsonProcessingException Occurs")
    void shouldLogErrorWhenJsonProcessingExceptionOccurs() throws JsonProcessingException {
        doThrow(new JsonProcessingException("Erro de serialização") {})
                .when(objectMapper).writeValueAsString(pushNotificationDTO);

        pushNotificationProducer.sendToPushNotification(pushNotificationDTO);

        verify(objectMapper).writeValueAsString(pushNotificationDTO);

        verify(sqsTemplate, times(0)).send("sqs.queue.notification.push.producer", pushNotificationDTO.toString());
    }

    private void buildArranges(){
        pushNotificationDTO = new PushNotificationDTO("email@email.com", ProcessStatus.PROCESSED, "videoName");
    }
}