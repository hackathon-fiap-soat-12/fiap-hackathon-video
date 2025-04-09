package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.updatevideo;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.UpdateVideoUseCase;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.updatevideo.dto.UpdateVideoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UpdateVideoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UpdateVideoConsumer.class);

    private final UpdateVideoUseCase updateVideoUseCase;
    private final ObjectMapper objectMapper;

    public UpdateVideoConsumer(UpdateVideoUseCase updateVideoUseCase, ObjectMapper objectMapper) {
        this.updateVideoUseCase = updateVideoUseCase;
        this.objectMapper = objectMapper;
    }

    @SqsListener("${sqs.queue.video.update.listener}")
    public void receiveMessage(String message) throws JsonProcessingException {
        var updateVideoDTO = objectMapper.readValue(message, UpdateVideoDTO.class);

        logger.info("Received Update Status {} for id {}", updateVideoDTO.status(), updateVideoDTO.id());
        logger.info("New Fields qtdFrames -> {} sizeInMB -> {}", updateVideoDTO.qtdFrames(), updateVideoDTO.sizeInBytes());

        updateVideoUseCase.updateVideo(updateVideoDTO);
    }
}
