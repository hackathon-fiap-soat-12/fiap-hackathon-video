package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.exceptions.DoesNotExistException;
import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.application.usecase.PresignedDownloadUseCase;

import java.util.UUID;

public class PresignedDownloadUseCaseImpl implements PresignedDownloadUseCase {

    private final VideoPersistence videoPersistence;

    public PresignedDownloadUseCaseImpl(VideoPersistence videoPersistence) {
        this.videoPersistence = videoPersistence;
    }

    @Override
    public String presignedDownload(UUID id) {
        var video = videoPersistence.findById(id);

        return video.orElseThrow(() -> new DoesNotExistException("Video not found"))
                .getVideoKey();
    }
}
