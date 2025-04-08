package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.uploadvideo;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.UploadVideoUseCase;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.uploadvideo.dto.UploadVideoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UploadVideoConsumerTest {

    @Mock
    private UploadVideoUseCase uploadVideoUseCase;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private UploadVideoConsumer uploadVideoConsumer;

    private UploadVideoDTO uploadVideoDTO;

    @BeforeEach
    void setUp() {
        this.buildArranges();
    }

    @Test
    @DisplayName("Should Call UploadVideoConsumer When Receiving message")
    void shouldCallUploadVideoConsumerWhenReceivingMessage() throws JsonProcessingException {
        when(objectMapper.readValue(uploadVideoDTO.toString(), UploadVideoDTO.class)).thenReturn(uploadVideoDTO);

        uploadVideoConsumer.receiveMessage(uploadVideoDTO.toString());

        verify(uploadVideoUseCase, times(1))
                .receiveToProcess(objectMapper.readValue(uploadVideoDTO.toString(), UploadVideoDTO.class));
        verify(objectMapper, times(2)).readValue(uploadVideoDTO.toString(), UploadVideoDTO.class);
    }

    private void buildArranges(){
        uploadVideoDTO = new UploadVideoDTO(UUID.randomUUID());
    }

}