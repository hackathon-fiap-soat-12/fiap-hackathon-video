package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.updatevideo;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.UpdateVideoUseCase;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.updatevideo.dto.UpdateVideoDTO;
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
class UpdateVideoConsumerTest {

    @Mock
    private UpdateVideoUseCase updateVideoUseCase;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private UpdateVideoConsumer updateVideoConsumer;

    private UpdateVideoDTO updateVideoDTO;

    @BeforeEach
    void setUp() {
        this.buildArranges();
    }

    @Test
    @DisplayName("Should Call updateVideoConsumer When Receiving message")
    void shouldCallUpdateVideoConsumerWhenReceivingMessage() throws JsonProcessingException {
        when(objectMapper.readValue(updateVideoDTO.toString(), UpdateVideoDTO.class)).thenReturn(updateVideoDTO);

        updateVideoConsumer.receiveMessage(updateVideoDTO.toString());

        verify(updateVideoUseCase, times(1))
                .updateVideo(objectMapper.readValue(updateVideoDTO.toString(), UpdateVideoDTO.class));
        verify(objectMapper, times(2)).readValue(updateVideoDTO.toString(), UpdateVideoDTO.class);
    }

    private void buildArranges(){
        updateVideoDTO = new UpdateVideoDTO(UUID.randomUUID(), ProcessStatus.PROCESSING, 10, 100L);
    }
}