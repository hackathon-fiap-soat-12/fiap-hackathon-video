package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.process.impl;

import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.process.ProcessProducer;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.process.dto.VideoToProcessDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProcessProducerImpl implements ProcessProducer {

    private static final Logger logger = LoggerFactory.getLogger(ProcessProducerImpl.class);

    @Value("${sqs.queue.video.process.producer}")
    private String processQueue;

    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper;

    public ProcessProducerImpl(SqsTemplate sqsTemplate, ObjectMapper objectMapper) {
        this.sqsTemplate = sqsTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendToProcess(VideoToProcessDTO dto) {
        try {
            sqsTemplate.send(processQueue, objectMapper.writeValueAsString(dto));

            logger.info("Sent video id {} to process", dto.id());
        } catch (JsonProcessingException e) {
            logger.error("Error on send video {} to process", dto.id());
        }
    }
}
