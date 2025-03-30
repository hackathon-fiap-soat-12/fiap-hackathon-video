package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.uploadvideo;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.UploadVideoUseCase;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.uploadvideo.dto.UploadVideoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UploadVideoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UploadVideoConsumer.class);

    private final UploadVideoUseCase uploadVideoUseCase;
    private final ObjectMapper objectMapper;

    public UploadVideoConsumer(UploadVideoUseCase uploadVideoUseCase, ObjectMapper objectMapper) {
        this.uploadVideoUseCase = uploadVideoUseCase;
        this.objectMapper = objectMapper;
    }

    @SqsListener("${sqs.queue.video.upload.listener}")
    public void receiveMessage(String message) throws JsonProcessingException {
        var uploadVideoDTO = objectMapper.readValue(message, UploadVideoDTO.class);

        logger.info("Received Upload Video for id {}", uploadVideoDTO.id());

        uploadVideoUseCase.receiveToProcess(uploadVideoDTO);
    }
}
