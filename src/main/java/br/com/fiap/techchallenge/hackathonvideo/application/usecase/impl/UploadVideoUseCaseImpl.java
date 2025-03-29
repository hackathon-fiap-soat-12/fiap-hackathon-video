package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.exceptions.DoesNotExistException;
import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.application.usecase.UploadVideoUseCase;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.uploadvideo.dto.UploadVideoDTO;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.process.ProcessProducer;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.process.dto.VideoToProcessDTO;

public class UploadVideoUseCaseImpl implements UploadVideoUseCase {

    private final VideoPersistence videoPersistence;
    private final ProcessProducer processProducer;

    public UploadVideoUseCaseImpl(VideoPersistence videoPersistence, ProcessProducer processProducer) {
        this.videoPersistence = videoPersistence;
        this.processProducer = processProducer;
    }

    @Override
    public void receiveToProcess(UploadVideoDTO dto) {
        var video = videoPersistence.findById(dto.id())
                .orElseThrow(() -> new DoesNotExistException("Video not found"));

        video.setReceived();

        video = videoPersistence.save(video);

        processProducer.sendToProcess(new VideoToProcessDTO(video.getId(), video.getBucketName(), video.getVideoKey()));
    }
}
