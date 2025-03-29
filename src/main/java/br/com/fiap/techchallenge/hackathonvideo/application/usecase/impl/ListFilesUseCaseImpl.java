package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.application.usecase.ListFilesUseCase;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;

import java.util.List;
import java.util.UUID;

public class ListFilesUseCaseImpl implements ListFilesUseCase {

    private final VideoPersistence videoPersistence;

    public ListFilesUseCaseImpl(VideoPersistence videoPersistence) {
        this.videoPersistence = videoPersistence;
    }

    @Override
    public List<Video> getFiles(UUID userId) {
        return videoPersistence.findAllByUserId(userId);
    }
}
