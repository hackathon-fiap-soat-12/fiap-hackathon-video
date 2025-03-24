package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.uploadvideo;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.UploadVideoUseCase;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.uploadvideo.dto.UploadVideoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class UploadVideoConsumer {

    private final UploadVideoUseCase uploadVideoUseCase;
    private final ObjectMapper objectMapper;

    public UploadVideoConsumer(UploadVideoUseCase uploadVideoUseCase, ObjectMapper objectMapper) {
        this.uploadVideoUseCase = uploadVideoUseCase;
        this.objectMapper = objectMapper;
    }

    @SqsListener("${sqs.queue.video.upload.listener}")
    public void receiveMessage(String message) throws JsonProcessingException {
        uploadVideoUseCase.receiveToProcess(objectMapper.readValue(message, UploadVideoDTO.class));
    }
}
