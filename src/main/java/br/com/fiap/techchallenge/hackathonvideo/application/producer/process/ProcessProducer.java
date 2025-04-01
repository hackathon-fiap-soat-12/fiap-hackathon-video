package br.com.fiap.techchallenge.hackathonvideo.application.producer.process;

import br.com.fiap.techchallenge.hackathonvideo.application.producer.process.dto.VideoToProcessDTO;

public interface ProcessProducer {
    void sendToProcess(VideoToProcessDTO dto);
}
