package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.updatevideo;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.UpdateVideoUseCase;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.updatevideo.dto.UpdateVideoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class UpdateVideoConsumer {

    private final UpdateVideoUseCase updateVideoUseCase;
    private final ObjectMapper objectMapper;

    public UpdateVideoConsumer(UpdateVideoUseCase updateVideoUseCase, ObjectMapper objectMapper) {
        this.updateVideoUseCase = updateVideoUseCase;
        this.objectMapper = objectMapper;
    }

    @SqsListener("${sqs.queue.video.update.listener}")
    public void receiveMessage(String message) throws JsonProcessingException {
        updateVideoUseCase.updateVideo(objectMapper.readValue(message, UpdateVideoDTO.class));
    }
}
