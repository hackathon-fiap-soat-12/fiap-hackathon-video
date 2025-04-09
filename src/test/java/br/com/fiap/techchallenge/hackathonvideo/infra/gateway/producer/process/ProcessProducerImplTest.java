package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.process;

import br.com.fiap.techchallenge.hackathonvideo.application.producer.process.dto.VideoToProcessDTO;
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
import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProcessProducerImplTest {

    @Mock
    private SqsTemplate sqsTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProcessProducerImpl processProducer;

    private VideoToProcessDTO videoToProcessDTO;

    @BeforeEach
    void setUp() {
        this.buildArranges();
        ReflectionTestUtils.setField(processProducer, "processQueue", "sqs.queue.video.process.producer");
    }

    @Test
    @DisplayName("Should Send To Process Queue")
    void shouldSendToProcessQueue() throws JsonProcessingException {
        processProducer.sendToProcess(videoToProcessDTO);

        verify(sqsTemplate).send("sqs.queue.video.process.producer", objectMapper.writeValueAsString(videoToProcessDTO));
        verify(objectMapper, times(2)).writeValueAsString(videoToProcessDTO);
    }

    @Test
    @DisplayName("Should Log Error When JsonProcessingException Occurs")
    void shouldLogErrorWhenJsonProcessingExceptionOccurs() throws JsonProcessingException {
        doThrow(new JsonProcessingException("Erro de serialização") {})
                .when(objectMapper).writeValueAsString(videoToProcessDTO);

        processProducer.sendToProcess(videoToProcessDTO);

        verify(objectMapper).writeValueAsString(videoToProcessDTO);

        verify(sqsTemplate, times(0)).send("sqs.queue.video.process.producer", videoToProcessDTO.toString());
    }

    private void buildArranges(){
        videoToProcessDTO = new VideoToProcessDTO(UUID.randomUUID(), "bucket", "key");
    }

}