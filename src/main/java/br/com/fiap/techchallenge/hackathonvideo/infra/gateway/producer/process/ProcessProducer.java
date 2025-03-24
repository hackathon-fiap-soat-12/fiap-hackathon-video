package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.process;

import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.process.dto.VideoToProcessDTO;

public interface ProcessProducer {
    void sendToProcess(VideoToProcessDTO dto);
}
