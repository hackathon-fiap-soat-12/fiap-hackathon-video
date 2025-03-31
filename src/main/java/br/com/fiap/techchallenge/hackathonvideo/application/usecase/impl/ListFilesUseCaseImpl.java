package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.application.usecase.ListFilesUseCase;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.pageable.CustomPage;

import java.util.UUID;

public class ListFilesUseCaseImpl implements ListFilesUseCase {

    private final VideoPersistence videoPersistence;

    public ListFilesUseCaseImpl(VideoPersistence videoPersistence) {
        this.videoPersistence = videoPersistence;
    }

    @Override
    public CustomPage getFiles(UUID userId, Integer pageSize, String exclusiveStartKey) {
        return videoPersistence.findAllByUserId(userId, pageSize, exclusiveStartKey);
    }
}
